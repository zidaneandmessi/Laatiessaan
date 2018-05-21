package gzotpa.ir;
import gzotpa.entity.Function;
import java.util.List;

public class Call extends Expr {
    private Expr expr;
    private List<Expr> args;

    public Call(Expr expr, List<Expr> args) {
        this.expr = expr;
        this.args = args;
    }

    public Expr expr() {
        return expr;
    }

    public long numArgs() {
        return args.size();
    }

    public List<Expr> args() {
        return args;
    }

    public Function function() {
        if (expr instanceof Var) {
            return (Function)(((Var)expr).entity());
        }
        else if (expr instanceof Addr) {
            return (Function)(((Addr)expr).entity());
        }
        else {
            throw new Error("Gzotpa! Not a funcall!");
        }
    }

    public <S,E> E accept(IRVisitor<S,E> visitor) {
        return visitor.visit(this);
    }

    protected void _dump(Dumper d) {
        d.printMember("expr", expr);
        d.printMembers("args", args);
    }
}