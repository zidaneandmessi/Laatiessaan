package gzotpa.entity;
import gzotpa.ast.TypeNode;
import gzotpa.type.Type;
import gzotpa.ast.Location;

abstract public class Entity {
    protected String name;
    protected boolean isPrivate;
    protected TypeNode typeNode;
    protected long cntRefered;

    public Entity(boolean priv, TypeNode type, String name) {
        this.name = name;
        this.isPrivate = priv;
        this.typeNode = type;
        this.cntRefered = 0;
    }

    public String name() {
        return name;
    }

    public boolean isPrivate() {
        return isPrivate;
    }
    
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
}