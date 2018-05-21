package gzotpa.ir;

public class Mem extends Expr {
    protected Expr expr;

    public Mem(Expr expr) {
        this.expr = expr;
    }

    public Expr expr() {
        return expr;
    }

    public Expr addressNode() {
        return expr;
    }
    
    public <S,E> E accept(IRVisitor<S,E> visitor) {
        return visitor.visit(this);
    }

    protected void _dump(Dumper d) {
        d.printMember("expr", expr);
    }
}
