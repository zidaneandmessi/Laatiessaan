package gzotpa.ast;
import gzotpa.type.*;
import gzotpa.exception.*;
import java.util.List;

public class FuncallNode extends ExprNode {
    protected ExprNode expr;
    protected List<ExprNode> args;
    protected boolean memberFunc;

    public FuncallNode(ExprNode expr, List<ExprNode> args) {
        this.expr = expr;
        this.args = args;
        this.memberFunc = false;
    }

    public FuncallNode(ExprNode expr, List<ExprNode> args, boolean memberFunc) {
        this.expr = expr;
        this.args = args;
        this.memberFunc = memberFunc;
    }

    public ExprNode expr() {
        return expr;
    }

    public boolean memberFunc() {
        return memberFunc;
    }

    public FunctionType functionType() {
        return expr.type().getPointerType().baseType().getFunctionType();
    }

    public Type type() {
        try {
            return functionType().returnType();
        }
        catch (ClassCastException err) {
            throw new SemanticError(err.getMessage());
        }
    }

    public List<ExprNode> args() {
        return args;
    }

    public long numArgs() {
        return args.size();
    }

    public void replaceArgs(List<ExprNode> args) {
        this.args = args;
    }

    public Location location() {
        return expr.location();
    }
    
    public <S,E> E accept(ASTVisitor<S,E> visitor) {
        return visitor.visit(this);
    }
}
