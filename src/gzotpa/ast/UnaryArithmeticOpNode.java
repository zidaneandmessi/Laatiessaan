package gzotpa.ast;

public class UnaryArithmeticOpNode extends UnaryOpNode {
    protected long amount;

    public UnaryArithmeticOpNode(String op, ExprNode expr) {
        super(op, expr);
        amount = 1;
    }

    public void setExpr(ExprNode expr) {
        this.expr = expr;
    }

    public long amount() {
        return this.amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
    
    public <S,E> E accept(ASTVisitor<S,E> visitor) {
        return visitor.visit(this);
    }

    public UnaryArithmeticOpNode clone() {
        UnaryArithmeticOpNode newNode = new UnaryArithmeticOpNode(super.operator, expr.clone());
        newNode.setAmount(amount);
        return newNode;
    }
}
