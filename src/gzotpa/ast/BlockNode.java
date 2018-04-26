package gzotpa.ast;
import gzotpa.entity.*;
import java.util.List;

public class BlockNode extends StmtNode {
    protected List<DefinedVariable> variables;
    protected List<StmtNode> stmts;
    protected LocalScope scope;

    public BlockNode(Location loc, List<DefinedVariable> vars, List<StmtNode> stmts) {
        super(loc);
        this.variables = vars;
        this.stmts = stmts;
    }

    public List<DefinedVariable> variables() {
        return variables;
    }

    public List<StmtNode> stmts() {
        return stmts;
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
