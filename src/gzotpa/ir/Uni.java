package gzotpa.ir;
import gzotpa.entity.Entity;

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

    public Entity entity() {
        throw new Error("Gzotpa! IR node has no entity!");
    }

    public boolean isIntConstant() {
        return expr.isIntConstant();
    }

    public boolean isStrConstant() {
        return false;
    }

    public <S,E> E accept(IRVisitor<S,E> visitor) {
        return visitor.visit(this);
    }

    protected void _dump(Dumper d) {
        d.printMember("op", op.toString());
        d.printMember("expr", expr);
    }
}
