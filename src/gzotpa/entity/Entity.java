package gzotpa.entity;
import gzotpa.ast.TypeNode;
import gzotpa.type.Type;
import gzotpa.ast.Location;

abstract public class Entity {
    protected String name;
    protected TypeNode typeNode;
    protected long cntRefered;

    public Entity(TypeNode type, String name) {
        this.name = name;
        this.typeNode = type;
        this.cntRefered = 0;
    }

    public String name() {
        return name;
    }
    
    abstract public boolean isDefined();

    public boolean isParameter() { return false; }

    public TypeNode typeNode() {
        return typeNode;
    }

    public Type type() {
        return typeNode.type();
    }

    public void refered() {
        cntRefered++;
    }

    public boolean isRefered() {
        return (cntRefered > 0);
    }

    public Location location() {
        return typeNode.location();
    }
    
    abstract public <T> T accept(EntityVisitor<T> visitor);
}