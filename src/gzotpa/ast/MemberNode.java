package gzotpa.ast;
import gzotpa.type.Type;
import gzotpa.type.CompositeType;
import gzotpa.exception.*;

public class MemberNode extends LHSNode {
    private ExprNode expr;
    private String member;

    public MemberNode(ExprNode expr, String member) {
        this.expr = expr;
        this.member = member;
    }

    public CompositeType baseType() {
    	try {
            return expr.type().getCompositeType();
        }
        catch (ClassCastException err) {
            throw new SemanticError(err.getMessage());
        }
    }

    public ExprNode expr() {
        return expr;
    }

    public String member() {
        return member;
    }

    protected Type origType() {
        return baseType().memberType(member);
    }

    public Location location() {
        return expr.location();
    }
}
