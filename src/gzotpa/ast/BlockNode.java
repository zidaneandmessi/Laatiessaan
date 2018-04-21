package gzotpa.ast;
import gzotpa.entity.DefinedVariable;
import java.util.List;

public class BlockNode extends StmtNode {
    protected List<DefinedVariable> variables;
    protected List<StmtNode> stmts;

    public BlockNode(Location loc, List<DefinedVariable> vars, List<StmtNode> stmts) {
        super(loc);
        this.variables = vars;
        this.stmts = stmts;
    }
}
