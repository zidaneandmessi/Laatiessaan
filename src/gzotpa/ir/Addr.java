package gzotpa.ir;
import gzotpa.entity.Entity;

public class Addr extends Expr {
    Entity entity;

    public Addr(Entity entity) {
        this.entity = entity;
    }

    public Entity entity() {
        return entity;
    }

    public <S,E> E accept(IRVisitor<S,E> visitor) {
        return visitor.visit(this);
    }
    
    protected void _dump(Dumper d) {
        d.printMember("entity", entity.name());
    }
}
