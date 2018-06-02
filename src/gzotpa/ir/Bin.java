package gzotpa.ir;
import gzotpa.ast.LHSNode;
import gzotpa.entity.Entity;

public class Bin extends Expr {
    protected Op op;
    protected Expr left, right;
    protected boolean stringBin;
    protected LHSNode lhsBase;

    public Bin(Op op, Expr left, Expr right) {
        this.op = op;
        this.left = left;
        this.right = right;
        this.stringBin = false;
        this.lhsBase = null;
    }

    public Bin(Op op, Expr left, Expr right, boolean stringBin) {
        this.op = op;
        this.left = left;
        this.right = right;
        this.stringBin = stringBin;
        this.lhsBase = null;
    }

    public Bin(Op op, Expr left, Expr right, LHSNode lhsBase) {
        this.op = op;
        this.left = left;
        this.right = right;
        this.stringBin = false;
        this.lhsBase = lhsBase;
    }

    public Entity entity() {
        throw new Error("Gzotpa! IR node has no entity!");
    }

    public LHSNode lhsBase() {
        return lhsBase;
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
