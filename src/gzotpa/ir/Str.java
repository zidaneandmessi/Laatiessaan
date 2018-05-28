package gzotpa.ir;
import gzotpa.asm.ImmediateValue;

public class Str extends Expr {
    protected String value;

    public Str(String value) {
        this.value = value;
    }

    public String value() { 
        return value;
    }
    
    public <S,E> E accept(IRVisitor<S,E> visitor) {
        return visitor.visit(this);
    }

    protected void _dump(Dumper d) {
        d.printMember("value", value);
    }
}
