package gzotpa.ir;

public class Bin extends Expr {
    protected Op op;
    protected Expr left, right;
    protected boolean stringBin;

    public Bin(Op op, Expr left, Expr right) {
        this.op = op;
        this.left = left;
        this.right = right;
        this.stringBin = false;
    }

    public Bin(Op op, Expr left, Expr right, boolean stringBin) {
        this.op = op;
        this.left = left;
        this.right = right;
        this.stringBin = stringBin;
    }

    public Expr left() { 
        return left;
    }

    public Expr right() {
        return right;
    }

    public Op op() {
        return op;
    }

    public boolean stringBin() {
        return stringBin;
    }

    public <S,E> E accept(IRVisitor<S,E> visitor) {
        return visitor.visit(this);
    }

    protected void _dump(Dumper d) {
        d.printMember("op", op.toString());
        d.printMember("left", left);
        d.printMember("right", right);
    }
}
