package gzotpa.compiler;
import gzotpa.ast.*;
import gzotpa.entity.*;
import gzotpa.type.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

class TypeChecker extends Visitor {
    private final TypeTable typeTable;
    DefinedFunction currentFunction;

    public TypeChecker(TypeTable typeTable) {
        this.typeTable = typeTable;
    }

    private void check(StmtNode node) {
        visitStmt(node);
    }

    private void check(ExprNode node) {
        visitExpr(node);
    }

    public void check(AST ast) {
        for (DefinedVariable var : ast.definedVariables()) {
            checkVariable(var);
        }
        for (DefinedFunction func : ast.definedFunctions()) {
            currentFunction = func;
            checkReturnType(func);
            checkParamTypes(func);
            check(func.body());
        }
        for (ClassNode cls : ast.definedClasses()) {
            for (DefinedVariable var : cls.decls().defvars()) {
                checkVariable(var);
            }
            for (DefinedFunction func : cls.decls().defuns()) {
                currentFunction = func;
                checkReturnType(func);
                checkParamTypes(func);
                check(func.body());
            }
        }
    }

    private void checkReturnType(DefinedFunction f) {
        if (isInvalidReturnType(f.returnType())) {
            throw new Error("Gzotpa! Return type invalid!");
        }
    }
    private boolean isInvalidReturnType(Type t) {
        return t.isArray();
    }
    private void checkParamTypes(DefinedFunction f) {
        for (Parameter param : f.parameters()) {
            if (isInvalidParameterType(param.type())) {
                throw new Error("Gzotpa! Parameter type invalid!");
            }
        }
    }
    private boolean isInvalidParameterType(Type t) {
        return t.isVoid() || t.isIncompleteArray();
    }
	protected void visitExprs(List<? extends ExprNode> exprs) {
        for (ExprNode e : exprs) {
            visitExpr(e);
        }
    }
    protected void visitVariables(List<DefinedVariable> vars) {
        for (DefinedVariable var : vars)
            checkVariable(var);
    }
    protected void checkVariable(DefinedVariable var) {
    	if (isInvalidVariableType(var.type())) {
            throw new Error("Gzotpa! Variable type invalid! " + var.type());
        }
        if (var.hasInitializer()) {
            if (isInvalidLHSType(var.type())) {
                throw new Error("Gzotpa! Variable is not a valid LHS type! " + var.type());
            }
            check(var.initializer());
            if (!var.type().isType(var.initializer().type())) {
                throw new Error("Gzotpa! Variable cannot be initialized from " + var.type() + " to " + var.initializer().type() + "!");
            }
            var.setInitializer(var.initializer());
        }
    }
    private boolean isInvalidVariableType(Type t) {
        return t.isVoid() || (t.isArray() && ! t.isAllocatedArray());
    }
    private boolean isInvalidLHSType(Type t) {
        return t.isVoid() || t.isArray();
    }
    private boolean isInvalidRHSType(Type t) {
        return t.isVoid();
    }
    private boolean isInvalidStatementType(Type t) {
        return false;
    }
    
	public Void visit(BlockNode node) {
        visitVariables(node.variables());
        for (StmtNode n : node.stmts()) {
            check(n);
        }
        return null;
    }

    public Void visit(ExprStmtNode node) {
        check(node.expr());
        if (node.expr() == null) return null;
        if (isInvalidStatementType(node.expr().type())) {
            throw new Error("Gzotpa! Statement type invalid!");
        }
        return null;
    }

    public Void visit(ForNode node) {
        super.visit(node);
        checkCond(node.cond());
        return null;
    }

    public Void visit(IfNode node) {
        super.visit(node);
        checkCond(node.cond());
        return null;
    }

    public Void visit(ReturnNode node) {
        super.visit(node);
        if (currentFunction.isVoid()) {
            if (node.expr() != null) {
                throw new Error("Gzotpa! Returning value from void function!");
            }
        }
        else {
            if (node.expr() == null) {
                throw new Error("Gzotpa! No return value!");
            }
            if (node.expr().type().isVoid()) {
                throw new Error("Gzotpa! Returning void value!");
            }
            if (!node.expr().type().isType(currentFunction.returnType())){
                throw new Error("Gzotpa! Wrong return type!");
            }
            node.setExpr(node.expr());
        }
        return null;
    }

    public Void visit(WhileNode node) {
        super.visit(node);
        checkCond(node.cond());
        return null;
    }

    public void checkCond(ExprNode cond) {
        if (cond == null) return;
        if (!cond.type().isInteger() || ((IntegerType)(cond.type())).isBool() == false) {
            throw new Error("Gzotpa! Condition not bool!");
        }
    }


    public Void visit(AssignNode node) {
        super.visit(node);
        if (!node.lhs().isParameter() && isInvalidLHSType(node.lhs().type())) {
            throw new Error("Gzotpa! Invalid LHS type!");
        }
        if (!node.rhs().isParameter() && isInvalidRHSType(node.rhs().type())) {
            throw new Error("Gzotpa! Invalid RHS type!");
        }
        if (!node.lhs().type().isType(node.rhs().type())) {
            throw new Error("Gzotpa! Cannot assign from a different type!");
        }
        node.setRHS(node.rhs());
        return null;
    }

    public Void visit(ArefNode node) {
        super.visit(node);
        if (!node.index().type().isInteger()) {
            throw new Error("Gzotpa! Array index not integer!");
        }
        return null;
    }

    public Void visit(BinaryOpNode node) {
        super.visit(node);
        if (!node.left().type().isType(node.right().type())
            || node.left().type() instanceof ClassType && !(node.right().type() instanceof NullType)
            || node.left().type() instanceof ArrayType && !(node.right().type() instanceof NullType)
            || node.left().type() instanceof NullType) {
            throw new Error("Gzotpa! Binary operator LHS or RHS type wrong!");
        }
        return null;
    }

    public Void visit(FuncallNode node) {
        super.visit(node);
        FunctionType type = node.functionType();
        if (type.argc() != node.argc()) {
            throw new Error("Gzotpa! Wrong number of function arguments!");
        }
        List<ExprNode> newArgs = new ArrayList<ExprNode>();
        Iterator<ExprNode> args = node.args().iterator();
        for (Type paramType : type.paramTypes()) {
            ExprNode arg = args.next();
            if (!arg.type().isType(paramType)) {
                throw new Error("Gzotpa! Parameter type doesn't match!");
            }
            newArgs.add(arg);
        }
        node.replaceArgs(newArgs);
        return null;
    }

    public Void visit(LogicalAndNode node) {
        super.visit(node);
        if (!node.left().type().isInteger() || !node.right().type().isInteger()) {
            throw new Error("Gzotpa! Logical and operator LHS or RHS not integer!");
        }
        return null;
    }

    public Void visit(LogicalOrNode node) {
        super.visit(node);
        if (!node.left().type().isInteger() || !node.right().type().isInteger()) {
            throw new Error("Gzotpa! Logical or operator LHS or RHS not integer!");
        }
        return null;
    }

    public Void visit(NewTypeNode node) {
        return null;
    }

    public Void visit(NullNode node) {
        return null;
    }
    
    public Void visit(OpAssignNode node) {
        super.visit(node);
        if (!node.lhs().isParameter() && isInvalidLHSType(node.lhs().type())) {
            throw new Error("Gzotpa! Invalid LHS type!");
        }
        if (!node.rhs().isParameter() && isInvalidRHSType(node.rhs().type())) {
            throw new Error("Gzotpa! Invalid RHS type!");
        }
        if (!node.lhs().type().isInteger()) {
            throw new Error("Gzotpa! Operator assign LHS not integer!");
        }
        if (!node.rhs().type().isInteger()) {
            throw new Error("Gzotpa! Operator assign RHS not integer!");
        }
        return null;
    }

    public Void visit(PrefixOpNode node) {
        super.visit(node);
        if (node.expr().isParameter()) {
            return null;
        }
        else if (!node.expr().type().isInteger()) {
            throw new Error("Gzotpa! Prefix operator LHS not integer!");
        }
        return null;
    }

    public Void visit(SuffixOpNode node) {
        super.visit(node);
        if (node.expr().isParameter()) {
            return null;
        }
        else if (!node.expr().type().isInteger()) {
            throw new Error("Gzotpa! Suffix operator LHS not integer!");
        }
        return null;
    }

    public Void visit(UnaryOpNode node) {
        super.visit(node);
        if (!node.expr().type().isInteger()) {
            throw new Error("Gzotpa! Unary operator LHS not integer!");
        }
        return null;
    }
}