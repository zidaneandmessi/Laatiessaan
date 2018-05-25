package gzotpa.ast;
import gzotpa.type.Type;
import gzotpa.type.TypeRef;

public class NewTypeNode extends ExprNode {
    TypeNode type;

    public NewTypeNode(TypeNode type) {
        this.type = type;
    }

    public TypeNode typeNode() {
        return type;
    }

    public TypeRef typeRef() {
        return type.typeRef();
    }

    public Type type() {
        return type.type();
    }

    public Location location() {
        return type.location();
    }

    public <S,E> E accept(ASTVisitor<S,E> visitor) {
        return visitor.visit(this);
    }

    public boolean isConstant() { return true; }
}