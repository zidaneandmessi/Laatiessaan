package gzotpa.asm;

public class ImmediateValue extends Operand {
    protected Literal val;

    public ImmediateValue(long n) {
        this(new IntegerLiteral(n));
    }

    public ImmediateValue(Label l) {
        this(new LabelLiteral(l));
    }

    public ImmediateValue(String s) {
        this(new StringLiteral(s));
    }

    public ImmediateValue(Literal val) {
        this.val = val;
    }

    public Literal val() {
        return this.val;
    }

    public boolean equals(Object other) {
        if (!(other instanceof ImmediateValue)) return false;
        ImmediateValue imm = (ImmediateValue)other;
        return val.equals(imm.val);
    }

    public void statisticRegister(AssemblyCode as) {}

    public String dump() {
        return "(ImmediateValue " + val.dump() + ")";
    }

    public String print() {
        return val.print();
    }
}
