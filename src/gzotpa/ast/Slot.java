package gzotpa.ast;
import gzotpa.type.*;

public class Slot extends Node {
    protected TypeNode typeNode;
    protected String name;

    public Slot(TypeNode t, String n) {
        typeNode = t;
        name = n;
    }

    public Type type() {
        return typeNode.type();
    }

    public TypeNode typeNode() {
        return typeNode;
    }
    public String name() {
        return name;
    }
    public Location location() {
        return typeNode.location();
    }
}
