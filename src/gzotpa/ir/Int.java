package gzotpa.ir;

public class Int extends Expr {
    protected long value;

    public Int(long value) {
        this.value = value;
    }

    public long value() { return value; }

    public boolean isConstant() { return true; }

    public <S,E> E accept(IRVisitor<S,E> visitor) {
        return visitor.visit(this);
    }
}
