package gzotpa.ast;
import gzotpa.type.Type;

public class BinaryOpNode extends ExprNode {
    protected String operator;
    protected ExprNode left, right;
    protected Type type;

    public BinaryOpNode(ExprNode left, String op, ExprNode right) {
        super();
        this.operator = op;
        this.left = left;
        this.right = right;
    }

    public BinaryOpNode(Type t, ExprNode left, String op, ExprNode right) {
        super();
        this.operator = op;
        this.left = left;
        this.right = right;
        this.type = t;
    }

    public String operator() {
        return operator;
    }

    public Type type() {
        return (type != null) ? type : left.type();
    }

    public ExprNode left() {
        return left;
    }

    public ExprNode right() {
        return right;
    }
    
    public Location location() {
        return left.location();
    }
    
    public <S,E> E accept(ASTVisitor<S,E> visitor) {
        return visitor.visit(this);
    }
}
