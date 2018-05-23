package gzotpa.asm;

public class LabelLiteral extends Literal {
    protected String value;

    public LabelLiteral(Label l) {
        this.value = l.name();
    }

    public String value() {
        return value;
    }

    public boolean equals(Object other) {
        return (other instanceof LabelLiteral)
                && equals((LabelLiteral)other);
    }

    public boolean equals(LabelLiteral other) {
        return other.value.equals(this.value);
    }
    
    public String dump() {
        return "(LabelLiteral " + value + ")";
    }
    
    public String print() {
        return value;
    }
}
