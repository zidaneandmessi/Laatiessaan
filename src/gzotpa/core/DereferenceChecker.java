package gzotpa.core;
import gzotpa.ast.*;
import gzotpa.entity.*;
import gzotpa.type.*;
import gzotpa.exception.*;
import java.util.*;

class DereferenceChecker extends Visitor {
    private final TypeTable typeTable;

    public DereferenceChecker(TypeTable typeTable) {
        this.typeTable = typeTable;
    }

    public void check(AST ast) {
        for (DefinedVariable var : ast.definedVariables()) {
            checkToplevelVariable(var);
        }
        for (DefinedFunction func : ast.definedFunctions()) {
            check(func.body());
        }
        for (ClassNode cls : ast.definedClasses()) {
            for (DefinedFunction func : cls.decls().defuns()) {
                check(func.body());
            }
        }
    }

    private void checkToplevelVariable(DefinedVariable var) {
        checkVariable(var);
        if (var.hasInitializer()) {
            checkConstant(var.initializer());
        }
    }

    private void checkConstant(ExprNode expr) {
        if (!expr.isConstant()) {
            throw new Error("Gzotpa! Not a constant!");
        }
    }

    private void checkVariable(DefinedVariable var) {
        if (var.hasInitializer())
            check(var.initializer());
    }

    private void check(StmtNode node) {
        if (node != null)
            node.accept(this);
    }

    private void check(ExprNode node) {
        if (node != null)
            node.accept(this);
    }


    public Void visit(AssignNode node) {
        super.visit(node);
        checkAssignment(node);
        return null;
    }

    private void checkAssignment(AbstractAssignNode node) {
        if (!node.lhs().isAssignable()) {
            throw new Error("Gzotpa! Invalid lhs expression");
        }
    }

    public Void visit(ArefNode node) {
        super.visit(node);
        if (!node.expr().isPointer()) {
            throw new Error("Gzotpa! Indexing non-array/pointer expression!");
        }
        handleImplicitAddress(node);
        return null;
    }

    public Void visit(BlockNode node) {
        for (DefinedVariable var : node.variables())
            checkVariable(var);
        for (StmtNode stmt : node.stmts())
            check(stmt);
        return null;
    }

    public Void visit(FuncallNode node) {
        super.visit(node);
        if (!node.expr().isCallable()) {
            throw new Error("Gzotpa! Calling object is not a function!");
        }
        return null;
    }

    public Void visit(MemberNode node) {
        super.visit(node);
        checkMemberRef(node.location(), node.expr().type(), node.member());
        handleImplicitAddress(node);
        return null;
    }

    public Void visit(OpAssignNode node) {
        super.visit(node);
        checkAssignment(node);
        return null;
    }

    public Void visit(PrefixOpNode node) {
        super.visit(node);
        if (!node.expr().isAssignable()) {
            throw new Error("Gzotpa! Cannot increment/decrement!");
        }
        return null;
    }

    public Void visit(SuffixOpNode node) {
        super.visit(node);
        if (!node.expr().isAssignable()) {
            throw new Error("Gzotpa! Cannot increment/decrement!");
        }
        return null;
    }

    private void checkMemberRef(Location loc, Type t, String memb) {
        if (!t.isClass()) {
            throw new Error("Gzotpa! " + "Accessing member `" + memb
                                + "' for non-class: " + t);
        }
        ClassType type = t.getClassType();
        if (!type.hasMemberVariable(memb)) {
            throw new Error("Gzotpa! " + type.toString()
                                + " does not have member: " + memb);
        }
    }

    public Void visit(VariableNode node) {
        super.visit(node);
        handleImplicitAddress(node);
        return null;
    }

    private void handleImplicitAddress(LHSNode node) {
        if (!node.isLoadable()) {
            Type t = node.type();
            if (t.isArray()) {
                node.setType(typeTable.pointerTo(t.baseType()));
            }
            else {
                node.setType(typeTable.pointerTo(t));
            }
        }
    }
}