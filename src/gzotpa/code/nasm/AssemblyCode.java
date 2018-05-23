package gzotpa.code.nasm;
import gzotpa.asm.*;
import java.util.List;
import java.util.ArrayList;

public class AssemblyCode extends gzotpa.asm.AssemblyCode {

    public AssemblyCode() {
        assemblies = new ArrayList<Assembly>();
    }

    public List<Assembly> assemblies() {
        return assemblies;
    }

    void addAssembly(Assembly as) {
        assemblies.add(as);
        as.statisticRegister(this);
    }

    void addAll(List<Assembly> ass) {
        assemblies.addAll(ass);
        for (Assembly as : ass)
            as.statisticRegister(this);
    }

    void add(Operand a, Operand b) {
        assemblies.add(new Instruction("add", a, b));
    }

    void and(Operand a, Operand b) {
        assemblies.add(new Instruction("and", a, b));
    }

    void call(String name) {
        assemblies.add(new Instruction("call", new Label(name)));
    }

    void cmp(Operand a, Operand b) {
        assemblies.add(new Instruction("cmp", a, b));
    }

    void db(String s) {
        assemblies.add(new Instruction("db", new ImmediateValue(s)));
    }

    void dq(long n) {
        assemblies.add(new Instruction("dq", new ImmediateValue(n)));
    }

    void global(Label label) {
        assemblies.add(new Instruction("global", label));
    }

    void imul(Operand a, Operand b) {
        assemblies.add(new Instruction("imul", a, b));
    }

    void idiv(Operand a, Operand b) {
        assemblies.add(new Instruction("idiv", a, b));
    }

    void jmp(Label label) {
        assemblies.add(new Instruction("jmp", label));
    }

    void jnz(Label label) {
        assemblies.add(new Instruction("jnz", label));
    }

    void label(Label label) {
        assemblies.add(label);
    }

    void lea(Operand dest, Operand src) {
        assemblies.add(new Instruction("lea", dest, src));
    }
    
    void leave() {
        assemblies.add(new Instruction("leave"));
    }

    void mov(Operand dest, Operand src) {
        assemblies.add(new Instruction("mov", dest, src));
    }

    void movzx(Operand dest, Operand src) {
        assemblies.add(new Instruction("movzx", dest, src));
    }

    void neg(Operand a) {
        assemblies.add(new Instruction("neg", a));
    }

    void not(Operand a) {
        assemblies.add(new Instruction("not", a));
    }

    void or(Operand a, Operand b) {
        assemblies.add(new Instruction("or", a, b));
    }

    void push(Register reg) {
        assemblies.add(new Instruction("push", reg));
    }

    void pop(Register reg) {
        assemblies.add(new Instruction("pop", reg));
    }

    void ret() {
        assemblies.add(new Instruction("ret"));
    }

    void section(String name) {
        assemblies.add(new Instruction("section", new Label(name)));
    }

    void sal(Operand a, Operand b) {
        assemblies.add(new Instruction("sal", a, b));
    }

    void sar(Operand a, Operand b) {
        assemblies.add(new Instruction("sar", a, b));
    }

    void sete(Operand a) {
        assemblies.add(new Instruction("sete", a));
    }

    void setg(Operand a) {
        assemblies.add(new Instruction("setg", a));
    }

    void setge(Operand a) {
        assemblies.add(new Instruction("setge", a));
    }

    void setl(Operand a) {
        assemblies.add(new Instruction("setl", a));
    }

    void setle(Operand a) {
        assemblies.add(new Instruction("setle", a));
    }

    void setne(Operand a) {
        assemblies.add(new Instruction("setne", a));
    }

    void setz(Operand a) {
        assemblies.add(new Instruction("setz", a));
    }

    void sub(Operand a, Operand b) {
        assemblies.add(new Instruction("sub", a, b));
    }

    void test(Register a, Register b) {
        assemblies.add(new Instruction("test", a, b));
    }

    void xor(Operand a, Operand b) {
        assemblies.add(new Instruction("xor", a, b));
    }

    class VirtualStack {
        private long size;
        private long maxSize;
        private List<MemoryReference> memrefs;

        VirtualStack() {
            reset();
        }

        void reset() {
            size = 0;
            maxSize = 0;
            if (memrefs == null) memrefs = new ArrayList<MemoryReference>();
            else memrefs.clear();
        }

        long maxSize() {
            return maxSize;
        }

        void extend(long len) {
            size += len;
            maxSize = Math.max(size, maxSize);
        }

        void rewind(long len) {
            size -= len;
        }

        void fixOffset(long diff) {
            for (MemoryReference mem : memrefs) {
                mem.fixOffset(diff);
            }
        }

        MemoryReference top() {
            MemoryReference mem = new MemoryReference(-size, new Register(RegisterClass.RBP, INT_SIZE), false);
            memrefs.add(mem);
            return mem;
        }
    }

    static final private long STACK_WORD_SIZE = 8;
    static final private long INT_SIZE = 32;
    final VirtualStack virtualStack = new VirtualStack();

    void virtualPush(Register reg) { // push by move, not moving stack frame
        virtualStack.extend(STACK_WORD_SIZE);
        mov(virtualStack.top(), reg);
    }

    void virtualPop(Register reg) {
        mov(reg, virtualStack.top());
        virtualStack.rewind(STACK_WORD_SIZE);
    }

    public void dump() {
        for (Assembly asm : assemblies) {
            System.err.println(asm.print());
        }
    }

    public void print() {
        for (Assembly asm : assemblies) {
            System.out.println(asm.print());
        }
    }
}