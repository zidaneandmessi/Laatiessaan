package gzotpa.ast;

public class SizeNode extends UnaryOpNode {

    public SizeNode(ExprNode e) {
        super("size", e);
    }

    public ExprNode expr() {
        return expr;
    }
    
    public <S,E> E accept(ASTVisitor<S,E> visitor) {
        return visitor.visit(this);
    }
}