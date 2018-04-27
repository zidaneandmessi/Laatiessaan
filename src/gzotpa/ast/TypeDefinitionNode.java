package gzotpa.ast;
import gzotpa.type.*;

abstract public class TypeDefinitionNode extends Node {
    protected String name;
    protected Location location;
    protected TypeNode typeNode;

    public TypeDefinitionNode(Location loc, TypeRef ref, String name) {
        this.name = name;
        this.location = loc;
        this.typeNode = new TypeNode(ref);
    }

    abstract public Type definingType();

    public String name() {
        return name;
    }

    public Location location() {
        return location;
    }

    public TypeNode typeNode() {
        return typeNode;
    }

    public TypeRef typeRef() {
        return typeNode.typeRef();
    }

    public Type type() {
        return typeNode.type();
    }

    abstract public <T> T accept(ClassVisitor<T> visitor);
}
