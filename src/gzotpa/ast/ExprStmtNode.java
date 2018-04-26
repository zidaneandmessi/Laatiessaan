package gzotpa.ast;

public class ExprStmtNode extends StmtNode {
    protected ExprNode expr;

    public ExprStmtNode(Location loc, ExprNode expr) {
        super(loc);
        this.expr = expr;
    }
    
    public ExprNode expr() {
        return expr;
    }

    public <S,E> S accept(ASTVisitor<S,E> visitor) {
        return visitor.visit(this);
    }
}
