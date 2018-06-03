package gzotpa.asm;

public class RegisterMemoryReference extends MemoryReference {
    protected Register reg;

    public RegisterMemoryReference(Register reg) {
        this.reg = reg;
    }

    public Register reg() {
        return reg;
    }

    public void fixOffset(long diff) {}

    public String dump() {
        return "(DirectMemoryReference " + reg.dump() + ")";
    }

    public String print() {
        return reg.print();
    }
}