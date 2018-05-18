package gzotpa.ir;
import gzotpa.ast.Location;

public class ExprStmt extends Stmt {
    protected Expr expr;

    public ExprStmt(Location loc, Expr expr) {
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
