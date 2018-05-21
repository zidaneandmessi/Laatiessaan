package gzotpa.code.nasm;
import gzotpa.asm.*;
import java.util.List;
import java.util.ArrayList;

public class AssemblyCode {
    private List<Assembly> assemblies = new ArrayList<Assembly>();

    public AssemblyCode() {}

    void mov(Register src, Register dest) {
        assemblies.add(new Instruction("mov", src, dest));
    }

    void section(String name) {
        assemblies.add(new Instruction("section ." + name));
    }

    void label(Label label) {
        assemblies.add(label);
    }

    void global(Label label) {
        assemblies.add(new Global(label));
    }

    void leave() {
        assemblies.add(new Instruction("leave"));
    }

    void ret() {
        assemblies.add(new Instruction("ret"));
    }

    public void dump() {
        for (Assembly asm : assemblies) {
            System.err.println(asm.dump());
        }
    }
}