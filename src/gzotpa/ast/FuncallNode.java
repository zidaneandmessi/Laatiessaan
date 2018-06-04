package gzotpa.ast;
import gzotpa.type.*;
import gzotpa.exception.*;
import java.util.List;
import java.util.ArrayList;

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
        if (expr.type() instanceof FunctionType) {
            return (FunctionType)(expr.type());
        }
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

    public long argc() {
        return args.size();
    }

    public void addArg(ExprNode expr) {
        this.args.add(expr);
    }

    public void replaceArgs(List<ExprNode> newArgs) {
        this.args = newArgs;
    }

    public Location location() {
        return expr.location();
    }
    
    public <S,E> E accept(ASTVisitor<S,E> visitor) {
        return visitor.visit(this);
    }

    public FuncallNode clone() {
        List<ExprNode> newArgs = new ArrayList<ExprNode>();
        for (ExprNode arg : args)
            newArgs.add(arg.clone());
        return new FuncallNode(expr.clone(), newArgs, memberFunc);
    }
}
