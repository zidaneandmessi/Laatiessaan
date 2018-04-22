package gzotpa.ast;
import gzotpa.type.Type;

public class NewTypeNode extends ExprNode {
    TypeNode type;

    public NewTypeNode(TypeNode type) {
        this.type = type;
    }
    public Type type() {
        return type.type();
    }

    public Location location() {
        return type.location();
    }
}