package gzotpa.ir;
import gzotpa.asm.MemoryReference;
import gzotpa.entity.*;

public class Var extends Expr {
    protected Entity entity;

    public Var(Entity entity) {
        entity.refered();
        this.entity = entity;
    }

    public Entity entity() {
        return entity;
    }

    public Expr addressNode() {
        return new Addr(entity);
    }

    public MemoryReference memref() {
        return entity.memref();
    }

    public String name() {
        return entity.name();
    }

    public boolean isIntConstant() {
        return false;
    }

    public boolean isStrConstant() {
        return false;
    }

    public <S,E> E accept(IRVisitor<S,E> visitor) {
        return visitor.visit(this);
    }

    protected void _dump(Dumper d) {
        d.printMember("entity", entity.name());
    }
}
