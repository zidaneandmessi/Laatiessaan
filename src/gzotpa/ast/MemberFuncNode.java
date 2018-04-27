package gzotpa.ast;
import gzotpa.type.*;
import gzotpa.exception.*;
import java.util.List;

public class MemberFuncNode extends FuncallNode {
    private ExprNode base;
    private String member;

    public MemberFuncNode(ExprNode base, ExprNode memberfunc, List<ExprNode> args) {
        super(memberfunc, args);
        this.base = base;
        this.member = ((VariableNode)memberfunc).name();
    }

    public ClassType baseType() {
    	try {
            return expr.type().getClassType();
        }
        catch (ClassCastException err) {
            throw new SemanticError(err.getMessage());
        }
    }

    public ExprNode base() {
        return base;
    }

    public String member() {
        return member;
    }

    protected Type origReturnType() {
        return baseType().memberFunctionReturnType(member);
    }
    
    public <S,E> E accept(ASTVisitor<S,E> visitor) {
        return visitor.visit(this);
    }
}
