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

    public IntegerLiteralNode(Type type, TypeRef ref, long value) {
        super(null, ref);
        this.typeNode.setType(type);
        this.value = value;
    }
    
    public long value() {
        return value;
    }
     
    public <S,E> E accept(ASTVisitor<S,E> visitor) {
        return visitor.visit(this);
    }
}
