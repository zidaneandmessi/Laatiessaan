package gzotpa.asm;

public class IntegerLiteral extends Literal {
    protected long value;

    public IntegerLiteral(long n) {
        this.value = n;
    }

    public long value() {
        return value;
    }

    public boolean equals(Object other) {
        return (other instanceof IntegerLiteral)
                && equals((IntegerLiteral)other);
    }

    public boolean equals(IntegerLiteral other) {
        return other.value == this.value;
    }

    public void statisticRegister(AssemblyCode as) {}
    
    public String dump() {
        return "(IntegerLiteral " + new Long(value).toString() + ")";
    }
    
    public String print() {
        return new Long(value).toString();
    }
}
