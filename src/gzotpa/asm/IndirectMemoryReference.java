package gzotpa.asm;

public class IndirectMemoryReference extends MemoryReference {
    Operand base;
    Operand mulBase;
    IntegerLiteral offset;
    boolean fixed;

    public IndirectMemoryReference(Operand base, long offset)  {
        this(base, null, new IntegerLiteral(offset), true);
    }

    public IndirectMemoryReference(Operand base, Operand mulBase, long offset) {
        this(base, mulBase, new IntegerLiteral(offset), true);
    }

    public IndirectMemoryReference(Operand base, long offset, boolean fixed) {
        this(base, null, new IntegerLiteral(offset), fixed);
    }

    private IndirectMemoryReference(Operand base, IntegerLiteral offset, boolean fixed) {
        this(base, null, offset, fixed);
    }

    private IndirectMemoryReference(Operand base, Operand mulBase, IntegerLiteral offset, boolean fixed) {
        this.base = base;
        this.mulBase = mulBase;
        this.offset = offset;
        this.fixed = fixed;
    }

    public IntegerLiteral offset() {
        return offset;
    }

    public Operand base() {
        return base;
    }

    public Operand mulBase() {
        return mulBase;
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
        if (mulBase != null) {
            if (base != null && offset != null && offset.value() > 1)
                return "[" + base.print() + "+" + mulBase.print() + "*" + offset.print() + "]";
            if (base != null && offset != null && offset.value() == 1)
                return "[" + base.print() + "+" + mulBase.print() + "]";
            else if (base != null && offset != null && offset.value() < -1)
                return "[" + base.print() + "-" + mulBase.print() + "*" + (-offset.value()) + "]";
            else if (base != null && offset != null && offset.value() == -1)
                return "[" + base.print() + "-" + mulBase.print() + "]";
            else if (base != null && offset != null && offset.value() == 0)
                return "[" + base.print() + "]";
            else if (offset != null)
                return "[" + mulBase.print() + "*" + offset.print() + "]";
            else
                return "[" + base.print() + "]";
        }
        else {
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
}
