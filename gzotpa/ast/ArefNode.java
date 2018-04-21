package	gzotpa.ast;
import gzotpa.type.*;

public class ArefNode extends LHSNode {
    private ExprNode expr, index;

    public ArefNode(ExprNode expr, ExprNode index) {
        this.expr = expr;
        this.index = index;
    }

    public ExprNode expr() { return expr; }
    public ExprNode index() { return index; }

    protected Type origType() {
        return expr.origType().baseType();
    }
    
    public Location location() {
        return expr.location();
    }
}
