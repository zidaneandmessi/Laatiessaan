package gzotpa.ast;

public class WhileNode extends StmtNode {
    protected StmtNode body;
    protected ExprNode cond;

    public WhileNode(Location loc, ExprNode cond, StmtNode body) {
        super(loc);
        this.cond = cond;
        this.body = body;
    }
    
    public ExprNode cond() {
        return cond;
    }

    public StmtNode body() {
        return body;
    }
}
