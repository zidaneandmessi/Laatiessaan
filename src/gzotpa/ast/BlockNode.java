package gzotpa.ast;
import gzotpa.entity.*;
import java.util.List;

public class BlockNode extends StmtNode {
    protected List<DefinedVariable> variables;
    protected List<StmtNode> stmts;
    protected List<Boolean> order;
    protected LocalScope scope;

    public BlockNode(Location loc, List<DefinedVariable> vars, List<StmtNode> stmts, List<Boolean> order) {
        super(loc);
        this.variables = vars;
        this.stmts = stmts;
        this.order = order;
    }

    public BlockNode(List<DefinedVariable> vars, List<StmtNode> stmts, List<Boolean> order) {
        super(null);
        this.variables = vars;
        this.stmts = stmts;
        this.order = order;
    }

    public List<DefinedVariable> variables() {
        return variables;
    }

    public List<StmtNode> stmts() {
        return stmts;
    }

    public List<Boolean> order() {
        return order;
    }
    
    public <S,E> S accept(ASTVisitor<S,E> visitor) {
        return visitor.visit(this);
    }
    
    public void setScope(LocalScope scope) {
        this.scope = scope;
    }

    public LocalScope scope() {
        return scope;
    }
}
