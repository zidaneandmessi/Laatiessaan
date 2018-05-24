package gzotpa.asm;

public class IndirectMemoryReference extends MemoryReference {
    IntegerLiteral offset;
    Register base;
    boolean fixed;

    public IndirectMemoryReference(long offset, Register base) {
        this(new IntegerLiteral(offset), base, true);
    }

    public IndirectMemoryReference(long offset, Register base, boolean fixed) {
        this(new IntegerLiteral(offset), base, fixed);
    }

    private IndirectMemoryReference(IntegerLiteral offset, Register base, boolean fixed) {
        this.offset = offset;
        this.base = base;
        this.fixed = fixed;
    }

    public IntegerLiteral offset() {
        return offset;
    }

    public Register base() {
        return base;
    }

    public boolean fixed() {
        return fixed;
    }

    public void fixOffset(long diff) {
        if (fixed) {
            throw new Error("Gzotpa! Memory reference already fixed!");
        }
        this.offset = new IntegerLiteral(((IntegerLiteral)offset).value + diff);
        this.fixed = true;
    }

    public String dump() {
        return "(IndirectMemoryReference "
                + (fixed ? "" : "*")
                + offset.dump() + " " + base.dump() + ")";
    }
    
    public String print() {
        if (base != null && offset != null && offset.value() > 0)
            return "[" + base.print() + "+" + offset.print() + "]";
        else if (base != null && offset != null && offset.value() < 0)
            return "[" + base.print() + "-" + (-offset.value()) + "]";
        else if (base != null && offset != null && offset.value() == 0)
            return "[" + base.print() + "]";
        else if (offset != null)
            return "[" + offset.print() + "]";
        else
            return "[" + base.print() + "]";
    }
}
