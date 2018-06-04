package gzotpa.ir;
import gzotpa.asm.MemoryReference;
import gzotpa.entity.Entity;

public class Addr extends Expr {
    Entity entity;

    public Addr(Entity entity) {
        entity.refered();
        this.entity = entity;
    }

    public Entity entity() {
        return entity;
    }

    public MemoryReference memref() {
        return entity.memref();
    }

    public <S,E> E accept(IRVisitor<S,E> visitor) {
        return visitor.visit(this);
    }
    
    protected void _dump(Dumper d) {
        d.printMember("entity", entity.name());
    }
}
