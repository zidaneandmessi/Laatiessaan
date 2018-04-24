package gzotpa.ast;

public class SizeNode extends UnaryOpNode {

    public SizeNode(ExprNode e) {
        super("size", e);
    }

    public ExprNode expr() {
        return expr;
    }
}