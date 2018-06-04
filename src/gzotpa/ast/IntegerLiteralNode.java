package gzotpa.ast;
import gzotpa.type.*;

public class IntegerLiteralNode extends LiteralNode {
    protected long value;

    public IntegerLiteralNode(Location loc, TypeRef ref, long value) {
        super(loc, ref);
        this.value = value;
    }

    public IntegerLiteralNode(TypeRef ref, long value) {
        super(null, ref);
        this.value = value;
    }

    public IntegerLiteralNode(Location loc, Type type, TypeRef ref, long value) {
        super(loc, ref);
        this.typeNode.setType(type);
        this.value = value;
    }
    
    public long value() {
        return value;
    }
     
    public <S,E> E accept(ASTVisitor<S,E> visitor) {
        return visitor.visit(this);
    }

    public IntegerLiteralNode clone() {
        IntegerLiteralNode newNode = new IntegerLiteralNode(null, null, super.typeNode.typeRef(), value);
        newNode.typeNode = this.typeNode;
        return newNode;
    }
}
