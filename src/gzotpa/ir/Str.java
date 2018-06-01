package gzotpa.ir;
import gzotpa.asm.ImmediateValue;

public class Str extends Expr {
    protected String value;
    protected String originValue;

    public Str(String value, String originValue) {
        this.value = value;
        this.originValue = originValue;
    }

    public String value() { 
        return value;
    }

    public String originValue() { 
        return originValue;
    }

    public <S,E> E accept(IRVisitor<S,E> visitor) {
        return visitor.visit(this);
    }

    protected void _dump(Dumper d) {
        d.printMember("value", value);
    }
}
