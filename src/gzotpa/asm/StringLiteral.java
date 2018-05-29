package gzotpa.asm;
public class StringLiteral extends Literal {
    protected String value;

    public StringLiteral(String s) {
        this.value = s;
    }

    public String value() {
        return value;
    }

    public boolean equals(Object other) {
        return (other instanceof StringLiteral)
                && equals((StringLiteral)other);
    }

    public boolean equals(StringLiteral other) {
        return other.value.equals(this.value);
    }

    public String toSource() {
        return value;
    }
    
    public String dump() {
        return "(StringLiteral " + value + ")";
    }
    
    public String print() {
        return "\"" + value + "\"";
    }
}
