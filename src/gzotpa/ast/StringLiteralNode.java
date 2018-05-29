package gzotpa.ast;
import gzotpa.type.TypeRef;

public class StringLiteralNode extends LiteralNode {
    protected String value;
    protected String originValue;

    public StringLiteralNode(Location loc, TypeRef ref, String value, String originValue) {
        super(loc, ref);
        this.value = value;
        this.originValue = originValue;
    }

    public StringLiteralNode(TypeRef ref, String value) {
        super(null, ref);
        this.value = value;
        this.originValue = value;
    }

    public String value() {
        return value;
    }

    public String originValue() {
        return originValue;
    }

    public <S,E> E accept(ASTVisitor<S,E> visitor) {
        return visitor.visit(this);
    }
}
