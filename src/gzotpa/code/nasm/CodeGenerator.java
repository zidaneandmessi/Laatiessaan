package gzotpa.code.nasm;
import gzotpa.asm.*;
import gzotpa.ast.Location;
import gzotpa.entity.*;
import gzotpa.ir.*;
import gzotpa.type.*;
import java.util.*;

public class CodeGenerator implements IRVisitor<Void,Void> {
    AssemblyCode code;

    public CodeGenerator() {
        code = new AssemblyCode();
    }

    public AssemblyCode generateAssemblyCode(IR ir) {
        code.global(new Label("main"));
        code.extern(new Label("malloc"));
        code.extern(new Label("printf"));
        code.extern(new Label("puts"));
        code.extern(new Label("sprintf"));
        code.extern(new Label("scanf"));
        code.extern(new Label("strlen"));
        code.extern(new Label("strcmp"));
        code.extern(new Label("strcpy"));
        code.extern(new Label("strncpy"));
        code.label(new Label("_int_format"));
        code.db("%d");
        code.label(new Label("_str_format"));
        code.db("%s");
        code.label(new Label("_str_str_format"));
        code.db("%s%s");
        generateDataSection(code, ir.defvars());
        code.setDataIndex();
        locateGlobalVariables(ir.defvars());
        generateTextSection(code, ir.defuns());
        return code;
    }

    private void generateDataSection(AssemblyCode code, List<DefinedVariable> vars) {
        code.section(".data");
        for (DefinedVariable var : vars) {
            code.label(new Label("_" + var.name()));
            generateImmediate(code, var);
        }
    }

    private void generateImmediate(AssemblyCode code, DefinedVariable var) {
        Expr node = var.ir();
        if (node == null) {
            if (var.type() instanceof StringType) {
                code.db(0);
            }
            else {
                code.dq(0);
            }
        }
        else if (node instanceof Int) {
            code.dq(((Int)node).value());
        }
        else if (node instanceof Str) {
            code.db(((Str)node).value());
        }
    }

    private void generateTextSection(AssemblyCode code, List<DefinedFunction> funcs) {
        code.section(".text");
        for (DefinedFunction func : funcs) 
            compileFunctionBody(code, func);
    }

    static final private long STACK_WORD_SIZE = 8;

    private Register r0() {
        return new Register(RegisterClass.R0, 64);
    }
    private Register r1() {
        return new Register(RegisterClass.R1, 64);
    }
    private Register r2() {
        return new Register(RegisterClass.R2, 64);
    }
    private Register r3() {
        return new Register(RegisterClass.R3, 64);
    }
    private Register r4() {
        return new Register(RegisterClass.R4, 64);
    }
    private Register r5() {
        return new Register(RegisterClass.R5, 64);
    }
    private Register r6() {
        return new Register(RegisterClass.R6, 64);
    }
    private Register r7() {
        return new Register(RegisterClass.R7, 64);
    }
    private Register r8() {
        return new Register(RegisterClass.R8, 64);
    }
    private Register r9() {
        return new Register(RegisterClass.R9, 64);
    }
    private Register r10() {
        return new Register(RegisterClass.R10, 64);
    }
    private Register r11() {
        return new Register(RegisterClass.R11, 64);
    }
    private Register r12() {
        return new Register(RegisterClass.R12, 64);
    }
    private Register r13() {
        return new Register(RegisterClass.R13, 64);
    }
    private Register r14() {
        return new Register(RegisterClass.R14, 64);
    }
    private Register r15() {
        return new Register(RegisterClass.R15, 64);
    }
    private Register rax() {
        return new Register(RegisterClass.RAX, 64);
    }
    private Register rcx() {
        return new Register(RegisterClass.RCX, 64);
    }
    private Register rdx() {
        return new Register(RegisterClass.RDX, 64);
    }
    private Register rbx() {
        return new Register(RegisterClass.RBX, 64);
    }
    private Register rsp() {
        return new Register(RegisterClass.RSP, 64);
    }
    private Register rbp() {
        return new Register(RegisterClass.RBP, 64);
    }
    private Register rsi() {
        return new Register(RegisterClass.RSI, 64);
    }
    private Register rdi() {
        return new Register(RegisterClass.RDI, 64);
    }
    private Register rax(long size) {
        return new Register(RegisterClass.RAX, size);
    }
    private Register rcx(long size) {
        return new Register(RegisterClass.RCX, size);
    }
    private Register rdx(long size) {
        return new Register(RegisterClass.RDX, size);
    }
    private Register rbx(long size) {
        return new Register(RegisterClass.RBX, size);
    }
    private Register rsp(long size) {
        return new Register(RegisterClass.RSP, size);
    }
    private Register rbp(long size) {
        return new Register(RegisterClass.RBP, size);
    }
    private Register rsi(long size) {
        return new Register(RegisterClass.RSI, size);
    }
    private Register rdi(long size) {
        return new Register(RegisterClass.RDI, size);
    }

    class StackFrame {
        List<Register> saveRegs;
        long localVarSize;
        long tempSize;

        public StackFrame() {}
        public long saveRegsSize() { return saveRegs.size() * STACK_WORD_SIZE; }
        public long localVarOffset() { return saveRegsSize(); }
        public long tempVarOffset() { return saveRegsSize() + localVarSize; }
        public long frameSize() { return saveRegsSize() + localVarSize + tempSize; }
    }   

    private void compileFunctionBody(AssemblyCode code, DefinedFunction func) {
        StackFrame frame = new StackFrame();
        locateParameters(func.parameters());
        frame.localVarSize = locateLocalVariables(func.localVarScope(), 0);
        AssemblyCode body = compileStmts(func);
        frame.saveRegs = usedCalleeSaveRegisters(body);
        frame.tempSize = body.virtualStack.maxSize();
        fixLocalVariableOffsets(func.localVarScope(), frame.localVarOffset());
        fixTempVariableOffsets(body, frame.tempVarOffset());
        if (func.name().equals("main"))
            code.label(new Label(func.name()));
        else
            code.label(new Label("_" + func.name()));
        generateFunctionBody(code, body, frame);
    }

    static final private long PARAM_START_WORD = 2;

    private void locateGlobalVariables(List<DefinedVariable> vars) {
        for (DefinedVariable var : vars) {
            if (var.type() instanceof IntegerType) {
                var.setMemref(new IndirectMemoryReference(new ImmediateValue(new Label("_" + var.name())), 0));
                var.setAddress(new DirectMemoryReference(new LabelLiteral(new Label("_" + var.name()))));
            }
            else 
            {
                DirectMemoryReference memref = new DirectMemoryReference(new LabelLiteral(new Label("_" + var.name())));
                var.setMemref(memref);
                var.setAddress(memref);
            }
        }
    }

    private void locateParameters(List<Parameter> params) {
        long numWords = PARAM_START_WORD;
        for (Parameter param : params) {
            param.setMemref(mem(rbp(), numWords * STACK_WORD_SIZE));
            numWords++;
        }
    }

    static final RegisterClass[] CALLEE_SAVE_REGISTERS = {
        RegisterClass.RBP, RegisterClass.RBX, RegisterClass.R12, RegisterClass.R13, RegisterClass.R14, RegisterClass.R15
    };

    private List<Register> calleeSaveRegistersCache = null;

    private List<Register> calleeSaveRegisters() {
        if (calleeSaveRegistersCache == null) {
            List<Register> regs = new ArrayList<Register>();
            for (RegisterClass c : CALLEE_SAVE_REGISTERS) {
                regs.add(new Register(c, 64));
            }
            calleeSaveRegistersCache = regs;
        }
        return calleeSaveRegistersCache;
    }

    private List<Register> usedCalleeSaveRegisters(AssemblyCode body) {
        List<Register> result = new ArrayList<Register>();
        for (Register reg : calleeSaveRegisters()) {
            if (body.usesRegister(reg)) {
                result.add(reg);
            }
        }
        result.remove(rbp());
        return result;
    }

    private void fixLocalVariableOffsets(LocalScope scope, long len) {
        for (DefinedVariable var : scope.localVariables()) {
            if (var instanceof Parameter) continue;
            var.memref().fixOffset(-len);
        }
    }

    private void fixTempVariableOffsets(AssemblyCode as, long len) {
        as.virtualStack.fixOffset(-len);
    }

    private void generateFunctionBody(AssemblyCode as, AssemblyCode body, StackFrame frame) {
        as.virtualStack.reset();
        prologue(as, frame.saveRegs, frame.frameSize());
        as.addAll(body.assemblies());
        epilogue(as, frame.saveRegs);
        as.virtualStack.fixOffset(0);
    }
    
    private void prologue(AssemblyCode as, List<Register> saveRegs, long frameSize) {
        as.push(rbp());
        as.mov(rbp(), rsp());
        for (Register reg : saveRegs) {
            as.virtualPush(reg);
        }
        extendStack(as, frameSize);
    }

    private void epilogue(AssemblyCode as, List<Register> savedRegs) {
        Collections.reverse(savedRegs);
        for (Register reg : savedRegs) {
            as.virtualPop(reg);
        }
        //as.mov(rsp(), rbp());
        //as.pop(rbp());
        as.leave();
        as.ret();
    }

    private IndirectMemoryReference mem(Register reg) {
        return mem(reg, 0);
    }

    private IndirectMemoryReference mem(Register reg, long offset) {
        return new IndirectMemoryReference(reg, offset);
    }

    private IndirectMemoryReference mem(Register reg, Register reg2) {
        return mem(reg, reg2, 1);
    }

    private IndirectMemoryReference mem(Register reg, Register reg2, long offset) {
        return new IndirectMemoryReference(reg, reg2, offset);
    }

    private void extendStack(AssemblyCode code, long len) {
        if (len > 0) {
            code.sub(rsp(), new ImmediateValue(len));
        }
    }
    
    private void rewindStack(AssemblyCode code, long len) {
        if (len > 0) {
            code.add(rsp(), new ImmediateValue(len));
        }
    }

    static public long alignStack(long n, long alignment) {
        return (n + alignment - 1) / alignment * alignment;
    }

    private long locateLocalVariables(LocalScope scope, long parentStackSize) {
        long size = parentStackSize;
        for (DefinedVariable var : scope.localVariables()) {
            if (var instanceof Parameter) continue;
            size = alignStack(size + var.allocSize() / 8, STACK_WORD_SIZE);
            var.setMemref(new IndirectMemoryReference(rbp(), -size, false)); //offset value changeable
        }
        long maxSize = size;
        for (LocalScope s : scope.children()) {
            long childLen = locateLocalVariables(s, size);
            maxSize = Math.max(maxSize, childLen);
        }
        return maxSize;
    }

    private AssemblyCode as;
    private Label epilogue;

    private AssemblyCode compileStmts(DefinedFunction func) {
        as = new AssemblyCode();
        epilogue = new Label("_end_" + func.name());
        for (Stmt stmt : func.ir()) {
            stmt.accept(this);
        }
        as.label(epilogue);
        return as;
    }

    private void compileBinaryOp(Op op, Register left, Operand right, boolean stringBin) {
        switch (op) {
        case ADD:
            if (stringBin) {
                as.push(left);
                as.push(rcx());
                as.mov(rdi(), new ImmediateValue(500));
                as.call("malloc");
                as.pop(rcx());
                as.pop(rdx());
                as.push(rax());
                as.mov(rdi(), rax());
                as.mov(rsi(), new ImmediateValue(new Label("_str_str_format")));
                as.xor(rax(), rax());
                as.call("sprintf");
                as.pop(rax());
            }
            else {
                as.add(left, right);
            }
            break;
        case SUB:
            as.sub(left, right);
            break;
        case MUL:
            as.imul(left, right);
            break;
        case DIV:
            as.cdq();
            as.idiv(rcx());
            break;
        case MOD:
            as.cdq();
            as.idiv(rcx());
            as.mov(left, rdx());
            break;
        case BIT_AND:
            as.and(left, right);
            break;
        case BIT_OR:
            as.or(left, right);
            break;
        case BIT_XOR:
            as.xor(left, right);
            break;
        case BIT_LSHIFT:
            as.sal(left, rcx(8));
            break;
        case ARITH_RSHIFT:
            as.sar(left, rcx(8));
            break;
        case EQ:
            if (stringBin) {
                as.mov(rdi(), left);
                as.mov(rsi(), right);
                as.call("strcmp");
                as.sete(rax(8));
                as.movzx(left, rax(8));
            }
            else {
                as.cmp(left, right);
                as.sete(rax(8));
                as.movzx(left, rax(8));
            }
            break;
        case NEQ:
            if (stringBin) {
                as.mov(rdi(), left);
                as.mov(rsi(), right);
                as.call("strcmp");
                as.setne(rax(8));
                as.movzx(left, rax(8));
            }
            else {
                as.cmp(left, right);
                as.setne(rax(8));
                as.movzx(left, rax(8));
            }
            break;
        case GT:
            if (stringBin) {
                as.mov(rdi(), left);
                as.mov(rsi(), right);
                as.call("strcmp");
                as.setg(rax(8));
                as.movzx(left, rax(8));
            }
            else {
                as.cmp(left, right);
                as.setg(rax(8));
                as.movzx(left, rax(8));
            }
            break;
        case GTEQ:
            if (stringBin) {
                as.mov(rdi(), left);
                as.mov(rsi(), right);
                as.call("strcmp");
                as.setge(rax(8));
                as.movzx(left, rax(8));
            }
            else {
                as.cmp(left, right);
                as.setge(rax(8));
                as.movzx(left, rax(8));
            }
            break;
        case LT:
            if (stringBin) {
                as.mov(rdi(), left);
                as.mov(rsi(), right);
                as.call("strcmp");
                as.setl(rax(8));
                as.movzx(left, rax(8));
            }
            else {
                as.cmp(left, right);
                as.setl(rax(8));
                as.movzx(left, rax(8));
            }
            break;
        case LTEQ:
            if (stringBin) {
                as.mov(rdi(), left);
                as.mov(rsi(), right);
                as.call("strcmp");
                as.setle(rax(8));
                as.movzx(left, rax(8));
            }
            else {
                as.cmp(left, right);
                as.setle(rax(8));
                as.movzx(left, rax(8));
            }
            break;
        }
    }

    private void compileUnaryOp(Op op, Register reg) {
        switch (op) {
        case NEG:
            as.neg(reg);
            break;
        case BIT_NOT:
            as.not(reg);
            break;
        case NOT:
            as.test(reg, reg);
            as.sete(rax(8));
            as.movzx(reg, rax(8));
            break;
        }
    }

    private void loadAddress(Entity var, Register dest) {
        if (var.address() != null) {
            as.mov(dest, var.address());
        }
        else {
        	as.lea(dest, var.memref());
        }
    }

    static private int constStrCnt = 0;
    
    public Void visit(Addr node) {
        loadAddress(node.entity(), rax());
        return null;
    }

    public Void visit(Assign node) {
        visit(node.rhs());
        as.virtualPush(rax());
        visit(node.lhs());
        as.mov(rcx(), rax());
        as.virtualPop(rax());
        as.mov(mem(rcx()), rax());
        return null;
    }

    public Void visit(Bin node) {
        Op op = node.op();
        visit(node.right());
        as.virtualPush(rax());
        visit(node.left());
        as.virtualPop(rcx());
        compileBinaryOp(op, rax(), rcx(), node.stringBin());
        return null;
    }

    public Void visit(Call node) {
        String name = node.function().name();
        if (name.equals("print")) {
            Expr arg = node.args().get(0);
            visit(arg);
            as.mov(rsi(), rax());
            as.mov(rdi(), new ImmediateValue(new Label("_str_format")));
            as.xor(rax(), rax());
            as.call("printf");
        }
        else if (name.equals("println")) {
            Expr arg = node.args().get(0);
            visit(arg);
            as.mov(rdi(), rax());
            as.call("puts");
        }
        else if (name.equals("toString")) {
            as.mov(rdi(), new ImmediateValue(20));
            as.call("malloc");
            as.push(rax());
            as.mov(rdi(), rax());
            Expr arg = node.args().get(0);
            visit(arg);
            as.mov(rdx(), rax());
            as.mov(rsi(), new ImmediateValue(new Label("_int_format")));
            as.xor(rax(), rax());
            as.call("sprintf");
            as.pop(rax());
        }
        else if (name.equals("getString")) {
            as.mov(rdi(), new ImmediateValue(500));
            as.call("malloc");
            as.push(rax());
            as.mov(rsi(), rax());
            as.mov(rdi(), new ImmediateValue(new Label("_str_format")));
            as.xor(rax(), rax());
            as.call("scanf");
            as.pop(rax());
        }
        else if (name.equals("getInt")) {
            as.push(rax());
            as.mov(rsi(), rsp());
            as.mov(rdi(), new ImmediateValue(new Label("_int_format")));
            as.xor(rax(), rax());
            as.call("scanf");
            as.pop(rax());
        }
        else if (name.equals("string.length")) {
            Expr arg = node.args().get(0);
            visit(arg);
            as.mov(rdi(), rax());
            as.call("strlen");
        }
        else if (name.equals("string.substring")) {
            Expr arg = node.args().get(2);
            visit(arg);
            as.mov(rsi(), rax());
            arg = node.args().get(0);
            visit(arg);
            as.add(rsi(), rax());
            as.mov(rcx(), rax());
            arg = node.args().get(1);
            visit(arg);
            as.sub(rax(), rcx());
            as.add(rax(), new ImmediateValue(1));
            as.mov(rdx(), rax());
            as.push(rdx());
            as.push(rsi());
            as.mov(rdi(), new ImmediateValue(500));
            as.call("malloc");
            as.pop(rsi());
            as.pop(rdx());
            as.push(rax());
            as.mov(rdi(), rax());
            as.call("strncpy");
            as.pop(rax());
        }
        else if (name.equals("string.ord")) {
            Expr arg = node.args().get(0);
            visit(arg);
            as.mov(rcx(), rax());
            arg = node.args().get(1);
            visit(arg);
            as.mov(rax(), mem(rax(), rcx()));
            as.movzx(rax(), rax(8));
        }
        else if (name.equals("_array.size")) {
            Expr arg = node.args().get(0);
            visit(arg);
            as.mov(rax(), mem(rax(), -8));
        }
        else
        {
            List<Expr> argList = node.args();
            Collections.reverse(argList);
            for (Expr arg : argList) {
                visit(arg);
                as.push(rax());
            }
            as.call("_" + name);
            rewindStack(as, node.numArgs() * STACK_WORD_SIZE);
        }
        return null;
    }

    public Void visit(ConditionJump node) {
        visit(node.cond());
        as.test(rax(), rax());
        as.jnz(node.thenLabel());
        as.jmp(node.elseLabel());
        return null;
    }

    public Void visit(Expr node) {
        if (node != null)
            node.accept(this);
        return null;
    }

    public Void visit(ExprStmt node) {
        visit(node.expr());
        return null;
    }

    public Void visit(Int node) {
        as.mov(rax(), new ImmediateValue(node.value()));
        return null;
    }

    public Void visit(Jump node) {
        as.jmp(node.targetLabel());
        return null;
    }

    public Void visit(LabelStmt node) {
        as.label(node.label());
        return null;
    }

    public Void visit(Mem node) {
        visit(node.expr());
        as.mov(rax(), mem(rax()));
        return null;
    }

    public Void visit(New node) {
        if (node.sizeKnown()) {
            as.mov(rdi(), new ImmediateValue(node.length() / 8));
            as.call("malloc");
        }
        else {
            visit(node.exprSize());
            as.sar(rax(), new ImmediateValue(3));
            as.mov(rdi(), rax());
            visit(node.exprLen());
            as.push(rax());
            as.add(rdi(), new ImmediateValue(8));
            as.call("malloc");
            as.pop(rdi());
            as.mov(mem(rax()), rdi());
            as.add(rax(), new ImmediateValue(8));
        }
        return null;
    }

    public Void visit(Return node) {
        visit(node.expr());
        as.jmp(epilogue);
        return null;
    }

    public Void visit(Str node) {
    	code.addData(new Label("_const_string_" + constStrCnt));
        String s = node.originValue();
        int st = 0, i = 0;
        for (i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '\\' && i < s.length() - 1) {
                char c = s.charAt(i + 1);
                switch(c) {
                case '\"':
                    if(i > st + 1) code.addData(new Instruction("db\t\"" + s.substring(st, i) + "\""));
                    code.addData(new Instruction("db\t34"));
                    st = i + 2;
                    i++;
                    break;
                case '\\':
                    if(i > st + 1) code.addData(new Instruction("db\t\"" + s.substring(st, i) + "\""));
                    code.addData(new Instruction("db\t92"));
                    st = i + 2;
                    i++;
                    break;
                case '\'':
                    if(i > st + 1) code.addData(new Instruction("db\t\"" + s.substring(st, i) + "\""));
                    code.addData(new Instruction("db\t39"));
                    st = i + 2;
                    i++;
                    break;
                case 'n':
                    if(i > st + 1) code.addData(new Instruction("db\t\"" + s.substring(st, i) + "\""));
                    code.addData(new Instruction("db\t10"));
                    st = i + 2;
                    i++;
                    break;
                default:;
                }
            }
        }
        code.addData(new Instruction("db\t\"" + s.substring(st, i) + "\", 0, 0"));
        as.mov(rax(), new ImmediateValue(new Label("_const_string_" + constStrCnt)));
        constStrCnt++;
        return null;
    }

    public Void visit(Uni node) {
        Op op = node.op();
        visit(node.expr());
        compileUnaryOp(op, rax());
        return null;
    }

    public Void visit(Var node) {
        as.mov(rax(), node.memref());
        return null;
    }
}