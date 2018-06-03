package gzotpa.code.nasm;
import gzotpa.asm.*;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class AssemblyCode extends gzotpa.asm.AssemblyCode {
    private int dataIndex = 0;
    private int textIndex = 0;

    public AssemblyCode() {
        assemblies = new LinkedList<Assembly>();
    }

    public List<Assembly> assemblies() {
        return assemblies;
    }

    void addAssembly(Assembly as) {
        assemblies.add(as);
        as.statisticRegister(this);
    }

    void setDataIndex() {
        dataIndex = 0;
        for (Assembly as : assemblies) dataIndex++;
    }

    void setTextIndex() {
        textIndex = 0;
        for (Assembly as : assemblies) {
            if (as instanceof Instruction && ((Instruction)as).isText()) {
                textIndex++;
                break;
            }
            textIndex++;
        }
    }

    void addData(Assembly as) {
        assemblies.add((dataIndex++), as);
    }

    void addAll(List<Assembly> ass) {
        assemblies.addAll(ass);
        for (Assembly as : ass)
            as.statisticRegister(this);
    }

    void addToStringFunction() {
        assemblies.add((textIndex++), new Label("__toString"));
        assemblies.add((textIndex++), new Instruction(("push"), new Label("rbx")));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("ebx, edi")));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("edi, 10")));
        assemblies.add((textIndex++), new Instruction(("call"), new Label("malloc")));
        assemblies.add((textIndex++), new Instruction(("test"), new Label("ebx, ebx")));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("rsi, rax")));
        assemblies.add((textIndex++), new Instruction(("je"), new Label("__toString_004")));
        assemblies.add((textIndex++), new Instruction(("js"), new Label("__toString_005")));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("rcx, rax")));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("rdi, rax")));
        assemblies.add((textIndex++), new Instruction(("xor"), new Label("r8d, r8d")));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("r9d, 1717986919")));
        assemblies.add((textIndex++), new Instruction(("ALIGN"), new Label("8")));
        assemblies.add((textIndex++), new Label("__toString_001"));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("eax, ebx")));
        assemblies.add((textIndex++), new Instruction(("add"), new Label("r8d, 1")));
        assemblies.add((textIndex++), new Instruction(("add"), new Label("rdi, 1")));
        assemblies.add((textIndex++), new Instruction(("imul"), new Label("r9d")));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("eax, ebx")));
        assemblies.add((textIndex++), new Instruction(("sar"), new Label("eax, 31")));
        assemblies.add((textIndex++), new Instruction(("sar"), new Label("edx, 2")));
        assemblies.add((textIndex++), new Instruction(("sub"), new Label("edx, eax")));
        assemblies.add((textIndex++), new Instruction(("lea"), new Label("eax, [rdx+rdx*4]")));
        assemblies.add((textIndex++), new Instruction(("add"), new Label("eax, eax")));
        assemblies.add((textIndex++), new Instruction(("sub"), new Label("ebx, eax")));
        assemblies.add((textIndex++), new Instruction(("add"), new Label("ebx, 48")));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("byte [rdi-1H], bl")));
        assemblies.add((textIndex++), new Instruction(("test"), new Label("edx, edx")));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("ebx, edx")));
        assemblies.add((textIndex++), new Instruction(("jnz"), new Label("__toString_001")));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("edx, r8d")));
        assemblies.add((textIndex++), new Instruction(("movsxd"), new Label("r8, r8d")));
        assemblies.add((textIndex++), new Instruction(("sar"), new Label("edx, 1")));
        assemblies.add((textIndex++), new Instruction(("jz"), new Label("__toString_003")));
        assemblies.add((textIndex++), new Instruction(("sub"), new Label("edx, 1")));
        assemblies.add((textIndex++), new Instruction(("lea"), new Label("rax, [rsi+r8-1H]")));
        assemblies.add((textIndex++), new Instruction(("lea"), new Label("r9, [rsi+rdx+1H]")));
        assemblies.add((textIndex++), new Instruction(("ALIGN"), new Label("8")));
        assemblies.add((textIndex++), new Label("__toString_002"));
        assemblies.add((textIndex++), new Instruction(("movzx"), new Label("edi, byte [rax]")));
        assemblies.add((textIndex++), new Instruction(("movzx"), new Label("edx, byte [rcx]")));
        assemblies.add((textIndex++), new Instruction(("add"), new Label("rcx, 1")));
        assemblies.add((textIndex++), new Instruction(("sub"), new Label("rax, 1")));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("byte [rcx-1H], dil")));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("byte [rax+1H], dl")));
        assemblies.add((textIndex++), new Instruction(("cmp"), new Label("rcx, r9")));
        assemblies.add((textIndex++), new Instruction(("jnz"), new Label("__toString_002")));
        assemblies.add((textIndex++), new Label("__toString_003"));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("byte [rsi+r8], 0")));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("rax, rsi")));
        assemblies.add((textIndex++), new Instruction(("pop"), new Label("rbx")));
        assemblies.add((textIndex++), new Instruction(("ret")));
        assemblies.add((textIndex++), new Instruction(("ALIGN"), new Label("8")));
        assemblies.add((textIndex++), new Label("__toString_004"));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("byte [rax], 48")));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("byte [rax+1H], 0")));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("rax, rsi")));
        assemblies.add((textIndex++), new Instruction(("pop"), new Label("rbx")));
        assemblies.add((textIndex++), new Instruction(("ret")));
        assemblies.add((textIndex++), new Instruction(("ALIGN"), new Label("8")));
        assemblies.add((textIndex++), new Label("__toString_005"));
        assemblies.add((textIndex++), new Instruction(("lea"), new Label("rcx, [rax+1H]")));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("byte [rax], 45")));
        assemblies.add((textIndex++), new Instruction(("neg"), new Label("ebx")));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("r8d, 1")));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("r10d, 1717986919")));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("rdi, rcx")));
        assemblies.add((textIndex++), new Instruction(("jmp"), new Label("__toString_007")));
        assemblies.add((textIndex++), new Instruction(("ALIGN"), new Label("8")));
        assemblies.add((textIndex++), new Label("__toString_006"));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("r8d, r9d")));
        assemblies.add((textIndex++), new Label("__toString_007"));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("eax, ebx")));
        assemblies.add((textIndex++), new Instruction(("add"), new Label("rdi, 1")));
        assemblies.add((textIndex++), new Instruction(("lea"), new Label("r9d, [r8+1H]")));
        assemblies.add((textIndex++), new Instruction(("imul"), new Label("r10d")));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("eax, ebx")));
        assemblies.add((textIndex++), new Instruction(("sar"), new Label("eax, 31")));
        assemblies.add((textIndex++), new Instruction(("sar"), new Label("edx, 2")));
        assemblies.add((textIndex++), new Instruction(("sub"), new Label("edx, eax")));
        assemblies.add((textIndex++), new Instruction(("lea"), new Label("eax, [rdx+rdx*4]")));
        assemblies.add((textIndex++), new Instruction(("add"), new Label("eax, eax")));
        assemblies.add((textIndex++), new Instruction(("sub"), new Label("ebx, eax")));
        assemblies.add((textIndex++), new Instruction(("add"), new Label("ebx, 48")));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("byte [rdi-1H], bl")));
        assemblies.add((textIndex++), new Instruction(("test"), new Label("edx, edx")));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("ebx, edx")));
        assemblies.add((textIndex++), new Instruction(("jnz"), new Label("__toString_006")));
        assemblies.add((textIndex++), new Instruction(("sar"), new Label("r8d, 1")));
        assemblies.add((textIndex++), new Instruction(("movsxd"), new Label("r9, r9d")));
        assemblies.add((textIndex++), new Instruction(("jz"), new Label("__toString_009")));
        assemblies.add((textIndex++), new Instruction(("lea"), new Label("edx, [r8-1H]")));
        assemblies.add((textIndex++), new Instruction(("lea"), new Label("rax, [rsi+r9-1H]")));
        assemblies.add((textIndex++), new Instruction(("lea"), new Label("r8, [rsi+rdx+2H]")));
        assemblies.add((textIndex++), new Instruction(("ALIGN"), new Label("8")));
        assemblies.add((textIndex++), new Label("__toString_008"));
        assemblies.add((textIndex++), new Instruction(("movzx"), new Label("edi, byte [rax]")));
        assemblies.add((textIndex++), new Instruction(("movzx"), new Label("edx, byte [rcx]")));
        assemblies.add((textIndex++), new Instruction(("add"), new Label("rcx, 1")));
        assemblies.add((textIndex++), new Instruction(("sub"), new Label("rax, 1")));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("byte [rcx-1H], dil")));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("byte [rax+1H], dl")));
        assemblies.add((textIndex++), new Instruction(("cmp"), new Label("r8, rcx")));
        assemblies.add((textIndex++), new Instruction(("jnz"), new Label("__toString_008")));
        assemblies.add((textIndex++), new Label("__toString_009"));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("byte [rsi+r9], 0")));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("rax, rsi")));
        assemblies.add((textIndex++), new Instruction(("pop"), new Label("rbx")));
        assemblies.add((textIndex++), new Instruction(("ret")));
    }

    void addParseIntFunction() {
        assemblies.add((textIndex++), new Label("__parseInt"));
        assemblies.add((textIndex++), new Instruction(("movzx"), new Label("edx, byte [rdi]")));
        assemblies.add((textIndex++), new Instruction(("lea"), new Label("eax, [rdx-30H]")));
        assemblies.add((textIndex++), new Instruction(("cmp"), new Label("al, 9")));
        assemblies.add((textIndex++), new Instruction(("ja"), new Label("__parseInt_002")));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("ecx, 0")));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("eax, 0")));
        assemblies.add((textIndex++), new Label("__parseInt_001"));
        assemblies.add((textIndex++), new Instruction(("lea"), new Label("eax, [rax+rax*4]")));
        assemblies.add((textIndex++), new Instruction(("movsx"), new Label("edx, dl")));
        assemblies.add((textIndex++), new Instruction(("lea"), new Label("eax, [rdx+rax*2-30H]")));
        assemblies.add((textIndex++), new Instruction(("add"), new Label("ecx, 1")));
        assemblies.add((textIndex++), new Instruction(("movsxd"), new Label("rdx, ecx")));
        assemblies.add((textIndex++), new Instruction(("movzx"), new Label("edx, byte [rdi+rdx]")));
        assemblies.add((textIndex++), new Instruction(("lea"), new Label("esi, [rdx-30H]")));
        assemblies.add((textIndex++), new Instruction(("cmp"), new Label("sil, 9")));
        assemblies.add((textIndex++), new Instruction(("jbe"), new Label("__parseInt_001")));
        assemblies.add((textIndex++), new Instruction(("DB"), new Label("0F3H")));
        assemblies.add((textIndex++), new Instruction("ret"));
        assemblies.add((textIndex++), new Label("__parseInt_002"));
        assemblies.add((textIndex++), new Instruction(("mov"), new Label("eax, 0")));
        assemblies.add((textIndex++), new Instruction("ret"));
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

    void cdq() {
        assemblies.add(new Instruction("cdq"));
    }

    void db(String s) {
        assemblies.add(new Instruction("db\t\"" + s + "\", 0, 0"));
    }

    void db(long n) {
        assemblies.add(new Instruction("db\t" + Long.toString(n) + ", 0, 0"));
    }

    void dq(long n) {
        assemblies.add(new Instruction("dq", new ImmediateValue(n)));
    }

    void extern(Label label) {
        assemblies.add(new Instruction("extern", label));
    }

    void global(Label label) {
        assemblies.add(new Instruction("global", label));
    }

    void imul(Operand a, Operand b) {
        assemblies.add(new Instruction("imul", a, b));
    }

    void idiv(Operand a) {
        assemblies.add(new Instruction("idiv", a));
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

    void resb(long n) {
        assemblies.add(new Instruction("resb", new ImmediateValue(n)));
    }

    void resq(long n) {
        assemblies.add(new Instruction("resq", new ImmediateValue(n)));
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
        private List<IndirectMemoryReference> memrefs;

        VirtualStack() {
            reset();
        }

        void reset() {
            size = 0;
            maxSize = 0;
            if (memrefs == null) memrefs = new ArrayList<IndirectMemoryReference>();
            else memrefs.clear();
        }

        long maxSize() {
            return maxSize;
        }

        void extend(long len) {
            size += len;
            maxSize = Math.max(maxSize, size);
        }

        void rewind(long len) {
            size -= len;
        }

        void fixOffset(long diff) {
            for (IndirectMemoryReference mem : memrefs) {
                mem.fixOffset(diff);
            }
        }

        IndirectMemoryReference top() {
            IndirectMemoryReference mem = new IndirectMemoryReference(new Register(RegisterClass.RBP, 64), -size, false);
            memrefs.add(mem);
            return mem;
        }
    }

    static final private long STACK_WORD_SIZE = 8;
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