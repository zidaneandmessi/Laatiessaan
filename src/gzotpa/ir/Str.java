package gzotpa.ir;
import gzotpa.asm.ImmediateValue;

public class Str extends Expr {
    protected String value;
    protected ImmediateValue imm;

    public Str(String value) {
        this.value = value;
        this.imm = new ImmediateValue(value);
    }

    public String value() { 
        return value;
    }

    public ImmediateValue imm() { 
        return imm;
    }

    public <S,E> E accept(IRVisitor<S,E> visitor) {
        return visitor.visit(this);
    }

    protected void _dump(Dumper d) {
        d.printMember("value", value);
    }
}
