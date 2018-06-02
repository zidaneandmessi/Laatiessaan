package gzotpa.ir;
import gzotpa.entity.Entity;

public class Int extends Expr {
    protected long value;

    public Int(long value) {
        this.value = value;
    }

    public long value() { 
        return value;
    }

    public Entity entity() {
        throw new Error("Gzotpa! IR node has no entity!");
    }

    public <S,E> E accept(IRVisitor<S,E> visitor) {
        return visitor.visit(this);
    }

    protected void _dump(Dumper d) {
        d.printMember("value", value);
    }
}
