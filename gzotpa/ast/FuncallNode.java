package gzotpa.ast;
import gzotpa.type.*;
import gzotpa.exception.*;
import java.util.List;

public class FuncallNode extends ExprNode {
    protected ExprNode expr;
    protected List<ExprNode> args;

    public FuncallNode(ExprNode expr, List<ExprNode> args) {
        this.expr = expr;
        this.args = args;
    }

    public ExprNode expr() {
        return expr;
    }
    public Type type() {
        return new Type();
        /*try {
            return functionType().returnType();
        }
        catch (ClassCastException err) {
            throw new SemanticError(err.getMessage());
        }*/
    }
}
