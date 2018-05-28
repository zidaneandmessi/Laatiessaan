package gzotpa.asm;

public class DirectMemoryReference extends MemoryReference {
    protected Literal name;

    public DirectMemoryReference(Literal name) {
        this.name = name;
    }

    public Literal name() {
        return this.name;
    }

    public void fixOffset(long diff) {
        throw new Error("Gzotpa! Cannot fix offset of direct memory reference!");
    }

    public String dump() {
        return "(DirectMemoryReference " + name.dump() + ")";
    }

    public String print() {
        return name.print();
    }
}