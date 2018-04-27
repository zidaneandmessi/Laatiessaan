package gzotpa.compiler;
import gzotpa.ast.*;
import gzotpa.entity.*;
import java.util.List;

abstract public class Visitor implements ASTVisitor<Void, Void> {
    public Visitor() {}

    protected void visitStmt(StmtNode stmt) {
        if (stmt != null) stmt.accept(this);
    }

    protected void visitStmts(List<? extends StmtNode> stmts) {
        for (StmtNode stmt : stmts) {
            if (stmt != null)
                visitStmt(stmt);
        }
    }

    protected void visitExpr(ExprNode expr) {
        expr.accept(this);
    }

    protected void visitExprs(List<? extends ExprNode> exprs) {
        for (ExprNode e : exprs) {
            visitExpr(e);
        }
    }
    protected void visitVariables(List<DefinedVariable> vars) {
        for (DefinedVariable var : vars)
            if (var.hasInitializer()) {
                visitExpr(var.initializer());
            }
    }

    public Void visit(BlockNode node) {
        visitVariables(node.variables());
        visitStmts(node.stmts());
        return null;
    }

    public Void visit(BreakNode node) {
        return null;
    }

    public Void visit(ContinueNode node) {
        return null;
    }

    public Void visit(ExprStmtNode node) {
        visitExpr(node.expr());
        return null;
    }

    public Void visit(ForNode node) {
        if (node.init() != null) {
            visitStmt(node.init());
        }
        if (node.cond() != null) {
            visitExpr(node.cond());
        }
        if (node.incr() != null) {
            visitStmt(node.incr());
        }
        visitStmt(node.body());
        return null;
    }

    public Void visit(IfNode node) {
        visitExpr(node.cond());
        visitStmt(node.thenBody());
        if (node.elseBody() != null) {
            visitStmt(node.elseBody());
        }
        return null;
    }

    public Void visit(ReturnNode node) {
        if (node.expr() != null) {
            visitExpr(node.expr());
        }
        return null;
    }

    public Void visit(WhileNode node) {
        visitExpr(node.cond());
        visitStmt(node.body());
        return null;
    }


    public Void visit(AssignNode node) {
        visitExpr(node.lhs());
        visitExpr(node.rhs());
        return null;
    }

    public Void visit(ArefNode node) {
        visitExpr(node.expr());
        visitExpr(node.index());
        return null;
    }

    public Void visit(BinaryOpNode n) {
        visitExpr(n.left());
        visitExpr(n.right());
        return null;
    }

    public Void visit(FuncallNode node) {
        visitExpr(node.expr());
        visitExprs(node.args());
        return null;
    }

    public Void visit(IntegerLiteralNode node) {
        return null;
    }

    public Void visit(LogicalAndNode node) {
        visitExpr(node.left());
        visitExpr(node.right());
        return null;
    }

    public Void visit(LogicalOrNode node) {
        visitExpr(node.left());
        visitExpr(node.right());
        return null;
    }

    public Void visit(MemberNode node) {
        visitExpr(node.expr());
        return null;
    }

    public Void visit(MemberFuncNode node) {
        visitExpr(node.base());
        //visitExpr(node.expr());
        visitExprs(node.args());
        return null;
    }

    public Void visit(NewTypeNode node) {
        return null;
    }

    public Void visit(NullNode node) {
        return null;
    }

    public Void visit(OpAssignNode node) {
        visitExpr(node.lhs());
        visitExpr(node.rhs());
        return null;
    }

    public Void visit(PrefixOpNode node) {
        visitExpr(node.expr());
        return null;
    }

    public Void visit(SuffixOpNode node) {
        visitExpr(node.expr());
        return null;
    }

    public Void visit(StringLiteralNode node) {
        return null;
    }

    public Void visit(UnaryOpNode node) {
        visitExpr(node.expr());
        return null;
    }
    
    public Void visit(VariableNode node) {
        return null;
    }
}
