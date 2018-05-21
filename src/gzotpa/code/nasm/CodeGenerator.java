package gzotpa.code.nasm;
import gzotpa.asm.*;
import gzotpa.ast.Location;
import gzotpa.entity.*;
import gzotpa.ir.*;
import java.util.*;

public class CodeGenerator implements IRVisitor<Void,Void> {
    public CodeGenerator() {}
    
    public AssemblyCode generate(IR ir) {
        //locateSymbols(ir);
        return generateAssemblyCode(ir);
    }
    
    /*private void locateSymbols(IR ir) {
        SymbolTable constSymbols = new SymbolTable(CONST_SYMBOL_BASE);
        for (ConstantEntry ent : ir.constantTable().entries()) {
            locateStringLiteral(ent, constSymbols);
        }
        for (Variable var : ir.allGlobalVariables()) {
            locateGlobalVariable(var);
        }
        for (Function func : ir.allFunctions()) {
            locateFunction(func);
        }
    }*/

    private AssemblyCode generateAssemblyCode(IR ir) {
        AssemblyCode code = new AssemblyCode();
        for (DefinedFunction func : ir.defuns()) {
            code.global(new Label(func.name())); 
        }
        generateTextSection(code, ir.defuns());
        return code;
    }

    private void generateTextSection(AssemblyCode code, List<DefinedFunction> funcs) {
        code.section(".text");
        for (DefinedFunction func : funcs) 
            compileFunctionBody(code, func);
    }

    static final private long STACK_WORD_SIZE = 4;
    static final private long INT_SIZE = 32;

    private Register r0() {
        return new Register(RegisterClass.R0, INT_SIZE);
    }
    private Register r1() {
        return new Register(RegisterClass.R1, INT_SIZE);
    }
    private Register r2() {
        return new Register(RegisterClass.R2, INT_SIZE);
    }
    private Register r3() {
        return new Register(RegisterClass.R3, INT_SIZE);
    }
    private Register r4() {
        return new Register(RegisterClass.R4, INT_SIZE);
    }
    private Register r5() {
        return new Register(RegisterClass.R5, INT_SIZE);
    }
    private Register r6() {
        return new Register(RegisterClass.R6, INT_SIZE);
    }
    private Register r7() {
        return new Register(RegisterClass.R7, INT_SIZE);
    }
    private Register r8() {
        return new Register(RegisterClass.R8, INT_SIZE);
    }
    private Register r9() {
        return new Register(RegisterClass.R9, INT_SIZE);
    }
    private Register r10() {
        return new Register(RegisterClass.R10, INT_SIZE);
    }
    private Register r11() {
        return new Register(RegisterClass.R11, INT_SIZE);
    }
    private Register r12() {
        return new Register(RegisterClass.R12, INT_SIZE);
    }
    private Register r13() {
        return new Register(RegisterClass.R13, INT_SIZE);
    }
    private Register r14() {
        return new Register(RegisterClass.R14, INT_SIZE);
    }
    private Register r15() {
        return new Register(RegisterClass.R15, INT_SIZE);
    }
    private Register rax() {
        return new Register(RegisterClass.RAX, INT_SIZE);
    }
    private Register rcx() {
        return new Register(RegisterClass.RCX, INT_SIZE);
    }
    private Register rdx() {
        return new Register(RegisterClass.RDX, INT_SIZE);
    }
    private Register rbx() {
        return new Register(RegisterClass.RBX, INT_SIZE);
    }
    private Register rsp() {
        return new Register(RegisterClass.RSP, INT_SIZE);
    }
    private Register rbp() {
        return new Register(RegisterClass.RBP, INT_SIZE);
    }
    private Register rsi() {
        return new Register(RegisterClass.RSI, INT_SIZE);
    }
    private Register rdi() {
        return new Register(RegisterClass.RDI, INT_SIZE);
    }

    class StackFrame {
        List<Register> saveRegs;
        long localVarSize;
        long tempSize;

        public StackFrame() {}
        public long saveRegsSize() { return saveRegs.size() * STACK_WORD_SIZE; }
        public long lvarOffset() { return saveRegsSize(); }
        public long tempOffset() { return saveRegsSize() + localVarSize; }
        public long frameSize() { return saveRegsSize() + localVarSize + tempSize; }
    }   

    private void compileFunctionBody(AssemblyCode code, DefinedFunction func) {
        StackFrame frame = new StackFrame();
        locateParameters(func.parameters());
        frame.localVarSize = locateLocalVariables(func.localVarScope(), 0);
        AssemblyCode body = compileStmts(func);
        for (Assembly as : body.assemblies()) {
            code.addAssembly(as);
        }
        // frame.saveRegs = usedCalleeSaveRegisters(body);
        // frame.tempSize = body.virtualStack.maxSize();
        // fixLocalVariableOffsets(func.lvarScope(), frame.lvarOffset());
        // fixTempVariableOffsets(body, frame.tempOffset());
        // generateFunctionBody(code, body, frame);
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
    
    private MemoryReference mem(Register reg) {
        return mem(0, reg);
    }

    private MemoryReference mem(long offset, Register reg) {
        return new MemoryReference(offset, reg);
    }

    static public long alignStack(long n, long alignment) {
        return (n + alignment - 1) / alignment * alignment;
    }

    private long locateLocalVariables(LocalScope scope, long parentStackSize) {
        long size = parentStackSize;
        for (DefinedVariable var : scope.localVariables()) {
            size = alignStack(size + var.allocSize(), STACK_WORD_SIZE);
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
        as.label(new Label(func.name()));
        epilogue = new Label("_end_" + func.name());
        for (Stmt stmt : func.ir()) {
            stmt.accept(this);
        }
        as.label(epilogue);
        return as;
    }
    
    private void rewindStack(AssemblyCode code, long len) {
        if (len > 0) {
            code.add(new ImmediateValue(len), rsp());
        }
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
            as.mov(rdx(), left);
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
            as.sal(left, right);
            break;
        case ARITH_RSHIFT:
            as.sar(left, right);
            break;
        case EQ:
            as.cmp(left, right);
            as.mov(r4(), left);
            break;
        case NEQ:
            as.cmp(left, right);
            as.mov(r5(), left);
            break;
        case GT:
            as.cmp(left, right);
            as.mov(r15(), left);
            break;
        case GTEQ:
            as.cmp(left, right);
            as.mov(r13(), left);
            break;
        case LT:
            as.cmp(left, right);
            as.mov(r12(), left);
            break;
        case LTEQ:
            as.cmp(left, right);
            as.mov(r14(), left);
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
            as.mov(r4(), reg);
            as.movzx(reg, reg);
            break;
        }
    }

    private void loadAddress(Entity var, Register dest) {
        if (var.address() != null) {
            as.mov(var.address(), dest);
        }
        else {
        	as.lea(var.memref(), dest);
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
        as.mov(rax(), rcx());
        as.virtualPop(rax());
        as.mov(rax(), mem(rcx()));
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
        as.mov(mem(rax()), rax());
        return null;
    }

    public Void visit(Return node) {
        visit(node.expr());
        as.jmp(epilogue);
        return null;
    }

    public Void visit(Str node) {
    	as.mov(node.imm(), rax());
        return null;
    }

    public Void visit(Uni node) {
        Op op = node.op();
        visit(node.expr());
        compileUnaryOp(op, rax());
        return null;
    }

    public Void visit(Var node) {
        as.mov(node.memref(), rax());
        return null;
    }
}