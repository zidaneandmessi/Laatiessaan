package gzotpa.code.nasm;
import gzotpa.asm.*;
import gzotpa.ast.*;
import gzotpa.core.IRGenerator;
import gzotpa.entity.*;
import gzotpa.ir.*;
import gzotpa.type.*;
import java.util.*;

public class CodeGenerator implements IRVisitor<Void,Void> {
    TypeTable typeTable;
    AssemblyCode code;
    DefinedFunction currentFunc;

    public CodeGenerator(TypeTable typeTable) {
        this.typeTable = typeTable;
        code = new AssemblyCode();
        currentFunc = null;
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
        code.db("%ld");
        code.label(new Label("_str_format"));
        code.db("%s");
        code.label(new Label("_str_str_format"));
        code.db("%s%s");
        initFreeRegs();
        generateDataSection(code, ir.defvars());
        generateBssSection(code, ir.defvars());
        locateGlobalVariables(ir.defvars());
        generateTextSection(code, ir);
        return optimize(code);
    }

    private AssemblyCode optimize(AssemblyCode code) {
        for (int i = 0; i < code.assemblies().size(); i++) {
            Assembly as = code.assemblies().get(i);
            if (as instanceof Instruction) {
                Instruction inst = (Instruction)as;
                if (inst.name().equals("jmp")) {
                    Assembly nt = code.assemblies().get(i + 1);
                    if (nt instanceof Label && nt.equals(inst.operand1())) {
                        code.assemblies().remove(i);
                        i--;
                    }
                }
                else if (inst.name().equals("mov")) {
                    Assembly nt = code.assemblies().get(i + 1);
                    if (nt instanceof Instruction
                        && ((Instruction)nt).name().equals("mov")
                        && inst.operand1() instanceof Register
                        && ((Register)inst.operand1()).equals(((Instruction)nt).operand1())) {
                        if ((inst.operand2() instanceof IndirectMemoryReference
                                && (((Register)inst.operand1()).equals(((IndirectMemoryReference)(inst.operand2())).base())
                                    || ((Register)inst.operand1()).equals(((IndirectMemoryReference)(inst.operand2())).mulBase())))
                            || (((Instruction)nt).operand2() instanceof IndirectMemoryReference
                                && (((Register)((Instruction)nt).operand1()).equals(((IndirectMemoryReference)(((Instruction)nt).operand2())).base())
                                || ((Register)((Instruction)nt).operand1()).equals(((IndirectMemoryReference)(((Instruction)nt) .operand2())).mulBase()))));
                        else {
                            code.assemblies().remove(i);
                            i--;
                        }
                    }
                }
            }
        }
        return code;
    }

    private void initFreeRegs() {
        while (!freeRegs.isEmpty()) freeRegs.removeLast();
        freeRegs.addLast(r12());
        freeRegs.addLast(r13());
        freeRegs.addLast(r14());
        freeRegs.addLast(r15());
        freeRegs.addLast(rbx());
    }

    private void generateDataSection(AssemblyCode code, List<DefinedVariable> vars) {
        code.section(".data");
        for (DefinedVariable var : vars) {
            if (!(var.type() instanceof ArrayType)
                && !(var.type() instanceof ClassType)
                && var.hasInitializer()) {
                code.label(new Label("_" + var.name()));
                generateImmediate(code, var);
            }
        }
        code.setDataIndex();
    }

    private void generateImmediate(AssemblyCode code, DefinedVariable var) {
        Expr node = var.ir();
        if (var.type() instanceof IntegerType) {
            if (node instanceof Int) {
                code.dq(((Int)node).value());
            }
            else if (node instanceof Call) {
                code.dq(0);
                var.setWaiting(true);
            }
            else {
                long value = calcImmediate(node);
                code.dq(value);
            }
        }
        else if (var.type() instanceof StringType) {
            String s = ((Str)node).originValue();
            code.setDataIndex();
            generateStringData(code, s);
        }
    }

    long calcImmediate(Expr node) {
        if (node instanceof Int) {
            return ((Int)node).value();
        }
        else if (node instanceof Uni) {
            switch (((Uni)node).op()) {
            case NEG:
                return -calcImmediate(((Uni)node).expr());
            case BIT_NOT:
                return ~calcImmediate(((Uni)node).expr());
            case NOT:
                long x = calcImmediate(((Uni)node).expr());
                if (x != 0) return 0;
                else return 1;
            }
        }
        else if (node instanceof Bin) {
            switch (((Bin)node).op()) {
            case ADD:
                return calcImmediate(((Bin)node).left()) + calcImmediate(((Bin)node).right());
            case SUB:
                return calcImmediate(((Bin)node).left()) - calcImmediate(((Bin)node).right());
            case MUL:
                return calcImmediate(((Bin)node).left()) * calcImmediate(((Bin)node).right());
            case DIV:
                return calcImmediate(((Bin)node).left()) / calcImmediate(((Bin)node).right());
            case MOD:
                return calcImmediate(((Bin)node).left()) % calcImmediate(((Bin)node).right());
            case BIT_AND:
                return calcImmediate(((Bin)node).left()) & calcImmediate(((Bin)node).right());
            case BIT_OR:
                return calcImmediate(((Bin)node).left()) | calcImmediate(((Bin)node).right());
            case BIT_XOR:
                return calcImmediate(((Bin)node).left()) ^ calcImmediate(((Bin)node).right());
            case BIT_LSHIFT:
                return calcImmediate(((Bin)node).left()) << calcImmediate(((Bin)node).right());
            case ARITH_RSHIFT:
                return calcImmediate(((Bin)node).left()) >> calcImmediate(((Bin)node).right());
            }
        }
        else if (node instanceof Var) {
            return calcImmediate(((DefinedVariable)(((Var)node).entity())).ir());
        }
        throw new Error("Gzotpa! Unknown global integer initializer!");
    }

    private void generateBssSection(AssemblyCode code, List<DefinedVariable> vars) {
        code.section(".bss");
        for (DefinedVariable var : vars) {
            if (var.type() instanceof ArrayType
                || var.type() instanceof ClassType
                || !(var.hasInitializer())) {
                code.label(new Label("_" + var.name()));
                generateReserveData(code, var);
            }
        }
    }

    private void generateReserveData(AssemblyCode code, DefinedVariable var) {
        if (var.type() instanceof ArrayType && var.hasInitializer()) {
            code.resq(1);
        }
        else {
            code.resq(1);
        }
    }

    private void generateTextSection(AssemblyCode code, IR ir) {
        code.section(".text");
        for (ClassNode cls : ir.defcls()) {
            for (DefinedFunction func : cls.decls().defuns()) {
                currentFunc = func;
                compileFunctionBody(code, func, ir);
            }
        }
        for (DefinedFunction func : ir.defuns()) {
            currentFunc = func;
            compileFunctionBody(code, func, ir);
        }
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

    LinkedList<Register> freeRegs = new LinkedList<Register>();

    private Register getFreeRegister() {
        if (!freeRegs.isEmpty()) return freeRegs.removeLast();
        return null;
    }

    private Register push(Register op, AssemblyCode as) { // success: reg  fail: null
        Register reg = getFreeRegister();
        if (reg == null) {
            as.virtualPush(op);
            return null;
        }
        as.mov(reg, op);
        return reg;
    }

    private void pop(Register reg, Register op, AssemblyCode as) {
        if (reg == null) {
            as.virtualPop(op);
            return;
        }
        as.mov(op, reg);
        freeRegs.addLast(reg);
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

    static private long forCnt = 0;
    LinkedList<DefinedVariable> localLoopVarStack = new LinkedList<DefinedVariable>();
    LinkedList<DefinedVariable> loopVarStack = new LinkedList<DefinedVariable>();

    private LinkedList<Stmt> generateMultiDimArrayInitStmtList(int index, ArrayType type, DefinedFunction func, ExprNode baseExpr, New ir, boolean global) {
        LinkedList<Stmt> stmts = new LinkedList<Stmt>();
        Expr forTimes = ir.lenStack().get(index);
        LocalScope scope = func.scope();
        Label beginLabel = new Label("_virtual_for_begin_" + forCnt);
        Label bodyLabel = new Label("_virtual_for_body_" + forCnt);
        Label endLabel = new Label("_virtual_for_end_" + (forCnt++));
        DefinedVariable loopVar;
        if (!global) {
            loopVar = localLoopVarStack.removeLast();
        }
        else {
            loopVar = scope.allocateTmp(new IntegerType(64, "_virtual_loop_var"));
        }
        Var loopVarVar = new Var(loopVar);
        loopVarStack.addLast(loopVar);
        stmts.add(new Assign(loopVarVar.addressNode(), new Int(0)));
        stmts.add(new LabelStmt(null, beginLabel));
        stmts.add(new ConditionJump(new Bin(Op.LT, loopVarVar, forTimes), bodyLabel, endLabel));
        stmts.add(new LabelStmt(null, bodyLabel));
        LinkedList<Expr> lenStack = new LinkedList<Expr>();
        lenStack.addLast(ir.lenStack().get(index - 1));
        New n = new New(lenStack);
        ArefNode node = null;
        for (int i = 0; i < loopVarStack.size(); i++) {
            DefinedVariable tmpLoopVar = loopVarStack.get(i);
            if (i == 0) node = new ArefNode(baseExpr, new VariableNode(tmpLoopVar));
            else node = new ArefNode(node, new VariableNode(tmpLoopVar));
        }
        Expr mem = new IRGenerator(typeTable).visit(node);
        stmts.add(new Assign(mem.addressNode(), n));
        if (index > 1) {
            stmts.addAll(generateMultiDimArrayInitStmtList(index - 1, (ArrayType)(type.baseType()), func, baseExpr, ir, global));
        }
        stmts.add(new Assign(loopVarVar.addressNode(), new Bin(Op.ADD, loopVarVar, new Int(1))));
        stmts.add(new Jump(null, beginLabel));
        stmts.add(new LabelStmt(null, endLabel));
        localLoopVarStack.addLast(loopVar);
        return stmts;
    }

    private void compileFunctionBody(AssemblyCode code, DefinedFunction func, IR ir) {
        LinkedList<Stmt> multiDimArrayInitStmtList = null;
        if (func.name().equals("main")) {
            code.label(new Label(func.name()));
            for (DefinedVariable var : ir.defvars()) {
                if (var.type() instanceof ArrayType && var.hasInitializer()) {
                    long val = calcImmediate(((New)(var.ir())).exprLen());
                    code.mov(rdi(), new ImmediateValue(val));
                    Register reg = push(rdi(), code);
                    code.sal(rdi(), new ImmediateValue(3));
                    code.add(rdi(), new ImmediateValue(8));
                    code.call("malloc");
                    pop(reg, rdi(), code);
                    code.mov(mem(rax()), rdi());
                    code.add(rax(), new ImmediateValue(8));
                    code.mov(rcx(), var.address());
                    code.mov(mem(rcx()), rax());
                    ArrayType type = (ArrayType)(var.type());
                    if (((New)(var.ir())).lenStack().size() > 1) {
                        while (!loopVarStack.isEmpty()) loopVarStack.removeLast();
                        multiDimArrayInitStmtList = generateMultiDimArrayInitStmtList(((New)(var.ir())).lenStack().size() - 1, type, func, new VariableNode(var), (New)(var.ir()), true);
                    }
                }
                else if (var.waitingForInit()) {
                    as = new AssemblyCode();
                    visit(var.ir());
                    loadAddress(var, rcx());
                    as.mov(mem(rcx()), rax());
                    code.addAll(as.assemblies());
                    var.setWaiting(false);
                }
            }
        }
        else {
            code.label(new Label("_" + func.name()));
        }
        locateParameters(func.parameters());
        while (!localLoopVarStack.isEmpty()) localLoopVarStack.removeLast();
        for (int i = 0; i < 11; i++) {
            localLoopVarStack.addLast(func.scope().allocateTmp(new IntegerType(64, "_virtual_loop_var")));
        }
        StackFrame frame = new StackFrame();
        frame.localVarSize = locateLocalVariables(func.localVarScope(), 0);
        AssemblyCode body = compileStmts(func, multiDimArrayInitStmtList);
        frame.saveRegs = usedCalleeSaveRegisters(body);
        frame.tempSize = body.virtualStack.maxSize();
        fixLocalVariableOffsets(func.localVarScope(), frame.localVarOffset());
        fixTempVariableOffsets(body, frame.tempVarOffset());
        generateFunctionBody(code, body, frame);
    }

    static final private long PARAM_START_WORD = 2;

    private void locateGlobalVariables(List<DefinedVariable> vars) {
        for (DefinedVariable var : vars) {
            if (var.type() instanceof IntegerType) {
                var.setMemref(new IndirectMemoryReference(new ImmediateValue(new Label("_" + var.name())), 0));
                var.setAddress(new DirectMemoryReference(new LabelLiteral(new Label("_" + var.name()))));
            }
            else if(!(var.type() instanceof ArrayType)
                    && !(var.type() instanceof ClassType)
                    && var.hasInitializer()) {
                DirectMemoryReference memref = new DirectMemoryReference(new LabelLiteral(new Label("_" + var.name())));
                var.setMemref(memref);
                var.setAddress(memref);
            }
            else {
                var.setMemref(new IndirectMemoryReference(new ImmediateValue(new Label("_" + var.name())), 0));
                var.setAddress(new DirectMemoryReference(new LabelLiteral(new Label("_" + var.name()))));
            }
        }
    }

    static final RegisterClass[] ARGS_REGISTERS = {
        RegisterClass.R8, RegisterClass.R9
    };

    private void locateParameters(List<Parameter> params) {
        long numWords = PARAM_START_WORD;
        int cnt = 0;
        int argc = params.size();
        for (Parameter param : params) {
            if (cnt >= argc - 2) {
                param.setMemref(new RegisterMemoryReference(new Register(ARGS_REGISTERS[argc - cnt - 1], 64)));
            }
            else {
                param.setMemref(mem(rbp(), numWords * STACK_WORD_SIZE));
                numWords++;
            }
            cnt++;
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
            size = alignStack(size + STACK_WORD_SIZE, STACK_WORD_SIZE);
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

    private AssemblyCode compileStmts(DefinedFunction func, List<Stmt> multiDimArrayInitStmtList) {
        as = new AssemblyCode();
        epilogue = new Label("_end_" + func.name());
        if (multiDimArrayInitStmtList != null) {
            for (Stmt stmt : multiDimArrayInitStmtList)
                stmt.accept(this);
        }
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
                Register reg1 = push(left, as);
                Register reg2 = push(rcx(), as);
                as.mov(rdi(), new ImmediateValue(500));
                as.call("malloc");
                pop(reg2, rcx(), as);
                pop(reg1, rdx(), as);
                reg1 = push(rax(), as);
                as.mov(rdi(), rax());
                as.mov(rsi(), new ImmediateValue(new Label("_str_str_format")));
                as.xor(rax(), rax());
                Register reg3 = push(r8(), as);
                Register reg4 = push(r9(), as);
                as.call("sprintf");
                pop(reg4, r9(), as);
                pop(reg3, r8(), as);
                pop(reg1, rax(), as);
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

    private void generateStringData(AssemblyCode code, String s) {
        int st = 0, i = 0;
        for (i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '\\' && i < s.length() - 1) {
                char c = s.charAt(i + 1);
                switch(c) {
                case '\"':
                    if(i >= st + 1) code.addData(new Instruction("db\t\"" + s.substring(st, i) + "\""));
                    code.addData(new Instruction("db\t34"));
                    st = i + 2;
                    i++;
                    break;
                case '\\':
                    if(i >= st + 1) code.addData(new Instruction("db\t\"" + s.substring(st, i) + "\""));
                    code.addData(new Instruction("db\t92"));
                    st = i + 2;
                    i++;
                    break;
                case '\'':
                    if(i >= st + 1) code.addData(new Instruction("db\t\"" + s.substring(st, i) + "\""));
                    code.addData(new Instruction("db\t39"));
                    st = i + 2;
                    i++;
                    break;
                case 'n':
                    if(i >= st + 1) code.addData(new Instruction("db\t\"" + s.substring(st, i) + "\""));
                    code.addData(new Instruction("db\t10"));
                    st = i + 2;
                    i++;
                    break;
                default:;
                }
            }
        }
        code.addData(new Instruction("db\t\"" + s.substring(st, i) + "\", 0, 0"));
    }

    static private int constStrCnt = 0;
    
    public Void visit(Addr node) {
        loadAddress(node.entity(), rax());
        return null;
    }

    public Void visit(Assign node) {
        if (node.rhs() instanceof New && ((New)(node.rhs())).lenStack() == null) {
            visit(node.rhs());
            Register reg = push(rax(), as);
            visit(node.lhs());
            as.mov(rcx(), rax());
            pop(reg, rax(), as);
            as.mov(mem(rcx()), rax());
            if (((New)(node.rhs())).type() instanceof ClassType && ((ClassType)(((New)(node.rhs())).type())).hasConstruct()) {
                reg = push(r8(), as);
                as.mov(r8(), rax());
                as.call("_" + ((New)(node.rhs())).type().typeName() + "." + ((New)(node.rhs())).type().typeName());
                //rewindStack(as, STACK_WORD_SIZE);
                pop(reg, r8(), as);
            }
        }
        else if (node.rhs() instanceof New && ((New)(node.rhs())).lenStack() != null && ((New)(node.rhs())).lenStack().size() > 1) {
            visit(((New)(node.rhs())).exprLen());
            as.mov(rdi(), rax());
            Register reg = push(rdi(), as);
            as.sal(rdi(), new ImmediateValue(3));
            as.add(rdi(), new ImmediateValue(8));
            as.call("malloc");
            pop(reg, rdi(), as);
            as.mov(mem(rax()), rdi());
            as.add(rax(), new ImmediateValue(8));
            ArrayType type;
            ExprNode lhs;
            if (node.lhs() instanceof Bin) {
                reg = push(rax(), as);
                lhs = node.lhs().lhsBase();
                visit(node.lhs());
                as.mov(rcx(), rax());
                pop(reg, rax(), as);
                as.mov(mem(rcx()), rax());
                type = (ArrayType)(lhs.type());
            }
            else {
                Entity ent = node.lhs().entity();
                lhs = new VariableNode(ent);
                as.lea(rcx(), ent.memref());
                as.mov(mem(rcx()), rax());
                type = (ArrayType)(ent.type());
            }
            if (((New)(node.rhs())).lenStack().size() > 1) {
                while (!loopVarStack.isEmpty()) loopVarStack.removeLast();
                LinkedList<Stmt> multiDimArrayInitStmtList = generateMultiDimArrayInitStmtList(((New)(node.rhs())).lenStack().size() - 1, type, currentFunc, lhs, (New)(node.rhs()), false);
                for (Stmt stmt : multiDimArrayInitStmtList)
                    stmt.accept(this);
            }
        }
        else if (node.lhs() instanceof Addr && ((Addr)node.lhs()).memref() != null) {
            visit(node.rhs());
            as.mov(((Addr)node.lhs()).memref(), rax());
        }
        else if (node.rhs() instanceof Int) {
            visit(node.lhs());
            as.mov(rcx(), rax());
            as.mov(rax(), new ImmediateValue(((Int)(node.rhs())).value()));
            as.mov(mem(rcx()), rax());
        }
        else {
            visit(node.rhs());
            Register reg = push(rax(), as);
            visit(node.lhs());
            as.mov(rcx(), rax());
            pop(reg, rax(), as);
            as.mov(mem(rcx()), rax());
        }
        return null;
    }

    public Void visit(Bin node) {
        Op op = node.op();
        if (node.right() instanceof Int) {
            visit(node.left());
            as.mov(rcx(), new ImmediateValue(((Int)(node.right())).value()));
            compileBinaryOp(op, rax(), rcx(), node.stringBin());
        }
        else if (node.right() instanceof Var) {
            visit(node.left());
            as.mov(rcx(), ((Var)(node.right())).memref());
            compileBinaryOp(op, rax(), rcx(), node.stringBin());
        }
        else if (node.right() instanceof Addr) {
            visit(node.left());
            loadAddress(((Addr)(node.right())).entity(), rcx());
            compileBinaryOp(op, rax(), rcx(), node.stringBin());
        }
        else if (node.left() instanceof Int
                || node.left() instanceof Var
                || node.left() instanceof Addr) {
            visit(node.right());
            as.mov(rcx(), rax());
            visit(node.left());
            compileBinaryOp(op, rax(), rcx(), node.stringBin());
        }
        else {
            visit(node.right());
            Register reg = push(rax(), as);
            visit(node.left());
            pop(reg, rcx(), as);
            compileBinaryOp(op, rax(), rcx(), node.stringBin());
        }
        return null;
    }

    private boolean toStringAdded = false;
    private boolean parseIntAdded = false;

    public Void visit(Call node) {
        String name = node.function().name();
        if (name.equals("print")) {
            Register reg1 = push(r8(), as);
            Register reg2 = push(r9(), as);
            Expr arg = node.args().get(0);
            visit(arg);
            as.mov(rsi(), rax());
            as.mov(rdi(), new ImmediateValue(new Label("_str_format")));
            as.xor(rax(), rax());
            as.call("printf");
            pop(reg2, r9(), as);
            pop(reg1, r8(), as);
        }
        else if (name.equals("println")) {
            Register reg1 = push(r8(), as);
            Register reg2 = push(r9(), as);
            Expr arg = node.args().get(0);
            visit(arg);
            as.mov(rdi(), rax());
            as.call("puts");
            pop(reg2, r9(), as);
            pop(reg1, r8(), as);
        }
        else if (name.equals("toString")) {
            /*as.mov(rdi(), new ImmediateValue(20));
            as.call("malloc");
            Register reg = push(rax(), as);
            as.mov(rdi(), rax());
            Expr arg = node.args().get(0);
            visit(arg);
            as.mov(rdx(), rax());
            as.mov(rsi(), new ImmediateValue(new Label("_int_format")));
            as.xor(rax(), rax());
            as.call("sprintf");
            pop(reg, rax(), as);*/
            if (!toStringAdded) {
                code.setTextIndex();
                code.addToStringFunction();
                toStringAdded = true;
            }
            Expr arg = node.args().get(0);
            visit(arg);
            Register reg1 = push(r8(), as);
            Register reg2 = push(r9(), as);
            as.mov(rdi(), rax());
            as.call("__toString");
            pop(reg2, r9(), as);
            pop(reg1, r8(), as);
        }
        else if (name.equals("getString")) {
            Register reg1 = push(r8(), as);
            Register reg2 = push(r9(), as);
            as.mov(rdi(), new ImmediateValue(500));
            as.call("malloc");
            pop(reg2, r9(), as);
            pop(reg1, r8(), as);
            Register reg = push(rax(), as);
            as.mov(rsi(), rax());
            as.mov(rdi(), new ImmediateValue(new Label("_str_format")));
            as.xor(rax(), rax());
            reg1 = push(r8(), as);
            reg2 = push(r9(), as);
            as.call("scanf");
            pop(reg2, r9(), as);
            pop(reg1, r8(), as);
            pop(reg, rax(), as);
        }
        else if (name.equals("getInt")) {
            Register reg1 = push(r8(), as);
            Register reg2 = push(r9(), as);
            as.push(rax());
            as.mov(rsi(), rsp());
            as.mov(rdi(), new ImmediateValue(new Label("_int_format")));
            as.xor(rax(), rax());
            as.call("scanf");
            as.pop(rax());
            pop(reg2, r9(), as);
            pop(reg1, r8(), as);
        }
        else if (name.equals("string.length")) {
            Register reg1 = push(r8(), as);
            Register reg2 = push(r9(), as);
            Expr arg = node.args().get(0);
            visit(arg);
            as.mov(rdi(), rax());
            as.call("strlen");
            pop(reg2, r9(), as);
            pop(reg1, r8(), as);
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
            Register reg1 = push(rcx(), as);
            visit(arg);
            pop(reg1, rcx(), as);
            as.sub(rax(), rcx());
            as.add(rax(), new ImmediateValue(1));
            as.mov(rdx(), rax());
            Register reg2 = push(rdx(), as);
            Register reg3 = push(rsi(), as);
            as.mov(rdi(), new ImmediateValue(500));
            as.call("malloc");
            pop(reg3, rsi(), as);
            pop(reg2, rdx(), as);
            reg1 = push(rax(), as);
            as.mov(rdi(), rax());
            as.call("strncpy");
            pop(reg1, rax(), as);
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
        else if (name.equals("string.parseInt")) {
            Register reg1 = push(r8(), as);
            Register reg2 = push(r9(), as);
            if (!parseIntAdded) {
                code.setTextIndex();
                code.addParseIntFunction();
                parseIntAdded = true;
            }
            Expr arg = node.args().get(0);
            visit(arg);
            as.mov(rdi(), rax());
            as.call("__parseInt");
            pop(reg2, r9(), as);
            pop(reg1, r8(), as);
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
            LinkedList<RegisterClass> savedRegisters = new LinkedList<RegisterClass>();
            int cnt = 0;
            Register[] reg = new Register[2];
            for (Expr arg : argList) {
                visit(arg);
                if (cnt < 2) {
                    reg[cnt] = push(new Register(ARGS_REGISTERS[cnt], 64), as);
                    as.mov(new Register(ARGS_REGISTERS[cnt], 64), rax());
                }
                else as.push(rax());
                cnt++;
            }
            as.call("_" + name);
            if (node.numArgs() > 2) rewindStack(as, (node.numArgs() - 2) * STACK_WORD_SIZE); 
            for (int i = 0; i < node.numArgs() && i < 2; i++) {
                pop(reg[i], new Register(ARGS_REGISTERS[i], 64), as);
            }
        }
        return null;
    }

    public Void visit(ConditionJump node) {
        if (node.cond() != null) {
            visit(node.cond());
            as.test(rax(), rax());
            as.jnz(node.thenLabel());
            as.jmp(node.elseLabel());
        }
        else {
            as.jmp(node.thenLabel());
        }
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
            visit(node.exprLen());
            as.mov(rdi(), rax());
            Register reg = push(rdi(), as);
            as.sal(rdi(), new ImmediateValue(3));
            as.add(rdi(), new ImmediateValue(8));
            as.call("malloc");
            pop(reg, rdi(), as);
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
        generateStringData(code, s);
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