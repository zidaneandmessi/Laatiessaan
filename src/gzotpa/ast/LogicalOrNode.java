package gzotpa.ast;

public class LogicalOrNode extends BinaryOpNode {
    public LogicalOrNode(ExprNode left, ExprNode right) {
        super(left, "||", right);
    }
    
    public <S,E> E accept(ASTVisitor<S,E> visitor) {
        return visitor.visit(this);
    }

    public LogicalOrNode clone() {
        return new LogicalOrNode(left.clone(), right.clone());
    }
}
