package gzotpa.ir;
import gzotpa.asm.ImmediateValue;
import gzotpa.entity.Entity;

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

    public Entity entity() {
        throw new Error("Gzotpa! IR node has no entity!");
    }

    public boolean isIntConstant() {
        return false;
    }

    public boolean isStrConstant() {
        return true;
    }

    public <S,E> E accept(IRVisitor<S,E> visitor) {
        return visitor.visit(this);
    }

    protected void _dump(Dumper d) {
        d.printMember("value", value);
    }
}
