package gzotpa.ast;
import gzotpa.type.*;
import gzotpa.exception.*;

public class MemberNode extends LHSNode {
    private ExprNode expr;
    private String member;

    public MemberNode(ExprNode expr, String member) {
        this.expr = expr;
        this.member = member;
    }

    public ClassType baseType() {
    	try {
            return expr.type().getClassType();
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

    public Type origType() {
        return baseType().memberVariableType(member);
    }

    public Location location() {
        return expr.location();
    }
    
    public <S,E> E accept(ASTVisitor<S,E> visitor) {
        return visitor.visit(this);
    }
}
