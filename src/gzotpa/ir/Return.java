package gzotpa.ir;
import gzotpa.ast.Location;

public class Return extends Stmt {
    protected Expr expr;

    public Return(Location loc, Expr expr) {
        super(loc);
        this.expr = expr;
    }

    public Expr expr() {
        return expr;
    }

    public <S,E> S accept(IRVisitor<S,E> visitor) {
        return visitor.visit(this);
    }
}