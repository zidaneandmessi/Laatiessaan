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

    public FunctionType functionType() {
        return expr.type().getFunctionType();
    }

    public Type type() {
        try {
            return functionType().returnType();
        }
        catch (ClassCastException err) {
            throw new SemanticError(err.getMessage());
        }
    }

    public Location location() {
        return expr.location();
    }
}
