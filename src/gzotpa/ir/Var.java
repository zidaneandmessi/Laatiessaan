package gzotpa.ir;
import gzotpa.entity.*;

public class Var extends Expr {
    protected Entity entity;

    public Var(Entity entity) {
        this.entity = entity;
    }

    public boolean isVar() { return true; }

    public Expr addressNode() {
        return new Addr(entity);
    }

    public <S,E> E accept(IRVisitor<S,E> visitor) {
        return visitor.visit(this);
    }
}
