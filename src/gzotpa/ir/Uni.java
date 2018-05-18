package gzotpa.ir;
public class Uni extends Expr {
    protected Op op;
    protected Expr expr;

    public Uni(Op op, Expr expr) {
        this.op = op;
        this.expr = expr;
    }

    public Op op() {
        return op;
    }
    
    public Expr expr() {
        return expr;
    }

    public <S,E> E accept(IRVisitor<S,E> visitor) {
        return visitor.visit(this);
    }
}
