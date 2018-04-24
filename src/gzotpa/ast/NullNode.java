package gzotpa.ast;
import gzotpa.type.Type;
import gzotpa.type.NullType;

public class NullNode extends ExprNode{
    Location location;
    Type type;
    public NullNode(Location loc) {
        location = loc;
        type = new NullType();
    }

    public Location location() {
        return location;
    }

    public Type type() {
        return type;
    }
}