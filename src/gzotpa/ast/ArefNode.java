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
        return expr instanceof ArefNode;
    }

    public ExprNode baseExpr() {
        if (isMultiDimension()) return ((ArefNode)expr).baseExpr();
        else return expr;
    }

    public Type origType() {
        return expr.origType().baseType();
    }
    
    public long elementSize() {
        return origType().allocSize();
    }
    
    public long length() {
        return 64;
    }
    
    public Location location() {
        return expr.location();
    }
    
    public <S,E> E accept(ASTVisitor<S,E> visitor) {
        return visitor.visit(this);
    }

    public ArefNode clone() {
        return new ArefNode(expr.clone(), index.clone());
    }
}
