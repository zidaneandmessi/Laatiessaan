package gzotpa.ast;
import gzotpa.type.TypeRef;
import gzotpa.type.Type;

abstract public class LiteralNode extends ExprNode {
    protected Location location;
    protected TypeNode typeNode;

    public LiteralNode(Location loc, TypeRef ref) {
        super();
        this.location = loc;
        this.typeNode = new TypeNode(ref);
    }

    public Location location() {
        return location;
    }

    public Type type() {
        return typeNode.type();
    }

    public TypeNode typeNode() {
        return typeNode;
    }

    public boolean isConstant() {
        return true;
    }
}
