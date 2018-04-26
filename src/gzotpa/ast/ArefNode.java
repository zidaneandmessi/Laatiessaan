package	gzotpa.ast;
import gzotpa.type.*;

public class ArefNode extends LHSNode {
    private ExprNode expr, index;

    public ArefNode(ExprNode expr, ExprNode index) {
        this.expr = expr;
        this.index = index;
    }

    public ExprNode expr() {
        return expr;
    }
    public ExprNode index() {
        return index;
    }

    public boolean isMultiDimension() {
        return (expr instanceof ArefNode) && !expr.origType().isPointer();
    }

    protected Type origType() {
        return expr.origType().baseType();
    }
    
    public Location location() {
        return expr.location();
    }
    
    public <S,E> E accept(ASTVisitor<S,E> visitor) {
        return visitor.visit(this);
    }
}
