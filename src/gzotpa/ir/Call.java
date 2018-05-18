package gzotpa.ir;
import java.util.List;

public class Call extends Expr {
    private Expr expr;
    private List<Expr> args;

    public Call(Expr expr, List<Expr> args) {
        this.expr = expr;
        this.args = args;
    }

    public Expr expr() {
        return expr;
    }

    public List<Expr> args() {
        return args;
    }

    public <S,E> E accept(IRVisitor<S,E> visitor) {
        return visitor.visit(this);
    }
}