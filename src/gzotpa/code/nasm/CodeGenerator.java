package gzotpa.code.nasm;
import gzotpa.asm.*;
import gzotpa.ast.Location;
import gzotpa.entity.*;
import gzotpa.ir.*;
import java.util.*;

public class CodeGenerator implements IRVisitor<Void,Void> {
    public CodeGenerator() {}

    public AssemblyCode generateAssemblyCode(IR ir) {
        AssemblyCode code = new AssemblyCode();
        for (DefinedVariable var : ir.defvars()) {
            code.global(new Label(var.name())); 
        }
        for (DefinedFunction func : ir.defuns()) {
            code.global(new Label(func.name())); 
        }
        generateDataSection(code, ir.defvars());
        generateTextSection(code, ir.defuns());
        return code;
    }

    private void generateDataSection(AssemblyCode code, List<DefinedVariable> vars) {
        code.section(".data");
        for (DefinedVariable var : vars) {
            code.label(new Label(var.name()));
            if (var.hasInitializer()) {
                generateImmediate(code, var.ir());
            }
        }
    }

    private void generateImmediate(AssemblyCode code, Expr node) {
        if (node instanceof Int) {
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
        code.label(new Label(func.name()));
        generateFunctionBody(code, body, frame);
    }

    static final private long PARAM_START_WORD = 2;

    private void locateParameters(List<Parameter> params) {
        long numWords = PARAM_START_WORD;
        for (Parameter param : params) {
            param.setMemref(mem(numWords * STACK_WORD_SIZE, rbp()));
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
        as.mov(rsp(), rbp());
        as.pop(rbp());
        as.ret();
    }

    private MemoryReference mem(Register reg) {
        return mem(0, reg);
    }

    private MemoryReference mem(long offset, Register reg) {
        return new MemoryReference(offset, reg);
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
            var.setMemref(new MemoryReference(-size, rbp(), false)); //offset value changeable
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

    private void compileBinaryOp(Op op, Register left, Operand right) {
        switch (op) {
        case ADD:
            as.add(left, right);
            break;
        case SUB:
            as.sub(left, right);
            break;
        case MUL:
            as.imul(left, right);
            break;
        case DIV:
            as.idiv(left, right);
            break;
        case MOD:
            as.idiv(left, right);
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
            as.cmp(left, right);
            as.sete(rax(8));
            as.movzx(left, rax(8));
            break;
        case NEQ:
            as.cmp(left, right);
            as.setne(rax(8));
            as.movzx(left, rax(8));
            break;
        case GT:
            as.cmp(left, right);
            as.setg(rax(8));
            as.movzx(left, rax(8));
            break;
        case GTEQ:
            as.cmp(left, right);
            as.setge(rax(8));
            as.movzx(left, rax(8));
            break;
        case LT:
            as.cmp(left, right);
            as.setl(rax(8));
            as.movzx(left, rax(8));
            break;
        case LTEQ:
            as.cmp(left, right);
            as.setle(rax(8));
            as.movzx(left, rax(8));
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
            as.setz(rax(8));
            as.movzx(reg, reg);
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
        compileBinaryOp(op, rax(), rcx());
        return null;
    }

    public Void visit(Call node) {
        List<Expr> argList = node.args();
        Collections.reverse(argList);
        for (Expr arg : argList) {
            visit(arg);
            as.push(rax());
        }
        as.call(node.function().name());
        rewindStack(as, node.numArgs() * STACK_WORD_SIZE);
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

    public Void visit(Return node) {
        visit(node.expr());
        as.jmp(epilogue);
        return null;
    }

    public Void visit(Str node) {
    	as.mov(rax(), node.imm());
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