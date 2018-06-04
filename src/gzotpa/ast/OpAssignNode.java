package gzotpa.ast;

public class OpAssignNode extends AbstractAssignNode {
    protected String operator;

    public OpAssignNode(ExprNode lhs, String op, ExprNode rhs) {
        super(lhs, rhs);
        this.operator = op;
    }

    public String operator() {
        return operator;
    }
    
    public <S,E> E accept(ASTVisitor<S,E> visitor) {
        return visitor.visit(this);
    }

    public OpAssignNode clone() {
        return new OpAssignNode(lhs.clone(), operator, rhs.clone());
    }
}
