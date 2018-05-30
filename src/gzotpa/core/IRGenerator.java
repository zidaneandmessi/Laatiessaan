package gzotpa.core;
import gzotpa.asm.*;
import gzotpa.ast.*;
import gzotpa.entity.*;
import gzotpa.exception.*;
import gzotpa.ir.*;
import gzotpa.type.*;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;

class IRGenerator implements ASTVisitor<Void, Expr> {
    private final TypeTable typeTable;

    public IRGenerator(TypeTable typeTable) {
        this.typeTable = typeTable;
    }

    public IR generate(AST ast) {
        for (DefinedVariable var : ast.definedVariables()) {
            if (var.hasInitializer()) {
                var.setIR(visitExpr(var.initializer()));
            }
        }
        for (DefinedFunction f : ast.definedFunctions()) {
            f.setIR(compileFunctionBody(f));
        }
        return ast.ir();
    }

    List<Stmt> stmts;
    LinkedList<LocalScope> scopeStack;
    LinkedList<Label> breakStack;
    LinkedList<Label> continueStack;

    private int exprNestLevel = 0;

    private void visitStmt(StmtNode node) {
        if (node != null)
            node.accept(this);
    }

    private void visitStmt(ExprNode node) {
        if (node != null)
            node.accept(this);
    }

    private Expr visitExpr(ExprNode node) {
        if (node == null) return null;
        exprNestLevel++;
        Expr e = node.accept(this);
        exprNestLevel--;
        return e;
    }

    public List<Stmt> compileFunctionBody(DefinedFunction func) {
        stmts = new ArrayList<Stmt>();
        scopeStack = new LinkedList<LocalScope>();
        breakStack = new LinkedList<Label>();
        continueStack = new LinkedList<Label>();
        visitStmt(func.body());
        return stmts;
    }

    private boolean isStatement() {
        return (exprNestLevel == 0);
    }

    private Var ref(Entity ent) {
        return new Var(ent);
    }

    private Expr addressOf(Expr expr) {
        return expr.addressNode();
    }

    private void assign(Location loc, Expr lhs, Expr rhs) {
        stmts.add(new Assign(loc, addressOf(lhs), rhs));
    }

    private DefinedVariable tmpVar(Type t) {
        return scopeStack.getLast().allocateTmp(t);
    }

    private void pushBreak(Label label) {
        breakStack.add(label);
    }

    private void popBreak() {
        if (breakStack.isEmpty()) {
            throw new Error("Gzotpa! Break stack empty!");
        }
        breakStack.removeLast();
    }

    private Label currentBreakTarget() {
        if (breakStack.isEmpty()) {
            throw new JumpError("Gzotpa! Break stack empty!");
        }
        return breakStack.getLast();
    }

    private void pushContinue(Label label) {
        continueStack.add(label);
    }

    private void popContinue() {
        if (continueStack.isEmpty()) {
            throw new Error("Gzotpa! Continue stack empty!");
        }
        continueStack.removeLast();
    }
    
    private Label currentContinueTarget() {
        if (continueStack.isEmpty()) {
            throw new JumpError("Gzotpa! Continue stack empty!");
        }
        return continueStack.getLast();
    }

    private void pushScope(LocalScope scope) {
        scopeStack.add(scope);
    }

    private void popScope() {
        if (scopeStack.isEmpty()) {
            throw new Error("Gzotpa! Scope stack empty!");
        }
        scopeStack.removeLast();
    }
    
    private Scope currentScope() {
        if (scopeStack.isEmpty()) {
            throw new JumpError("Gzotpa! Scope stack empty!");
        }
        return scopeStack.getLast();
    }
    
    private Int imm(Type operandType, long n) {
        if (operandType.isPointer()) {
            return new Int(n);
        }
        else {
            return new Int(n);
        }
    }

    private Expr transformIndex(ArefNode node) {
        if (node.isMultiDimension()) {
            return new Bin(Op.ADD,
                            visitExpr(node.index()),
                            new Bin(Op.MUL,
                                    new Int(node.length()),
                                    transformIndex((ArefNode)node.expr())));
        }
        else {
            return visitExpr(node.index());
        }
    }

    private Expr transformOpAssign(Location loc, Op op, Type lhsType, Expr lhs, Expr rhs) {
        assign(loc, lhs, new Bin(op, lhs, rhs));
        if (isStatement()) return null;
        else return lhs;
    }

    public Expr visit(ArefNode node) {
        Expr expr = visitExpr(node.baseExpr());
        Expr offset = new Bin(Op.MUL, new Int(node.elementSize() / 8), transformIndex(node));
        Bin addr = new Bin(Op.ADD, expr, offset);
        return new Mem(addr);
    }

    public Expr visit(AssignNode node) {
        if (isStatement()) {
            Expr rhs = visitExpr(node.rhs());
            assign(node.lhs().location(), visitExpr(node.lhs()), rhs);
            return null;
        }
        else {
            DefinedVariable var = tmpVar(node.rhs().type());
            assign(node.rhs().location(), ref(var), visitExpr(node.rhs()));
            assign(node.lhs().location(), visitExpr(node.lhs()), ref(var));
            return ref(var);
        }
    }

    public Expr visit(BinaryOpNode node) {
        Expr right = visitExpr(node.right());
        Expr left = visitExpr(node.left());
        Op op = Op.internBinary(node.operator());
        Type t = node.left().type();
        if (t instanceof IntegerType)
            return new Bin(op, left, right);
        else if (t instanceof StringType)
            return new Bin(op, left, right, true);
        else
            throw new Error("Gzotpa! Unknown binary operator expr type! " + t);
    }

    public Void visit(BlockNode node) {
        pushScope(node.scope());
        for (DefinedVariable var : node.variables()) {
            if (var.hasInitializer()) {
                var.setIR(visitExpr(var.initializer()));
            }
        }
        for (StmtNode stmt : node.stmts()) {
            visitStmt(stmt);
        }
        popScope();
        return null;
    }

    public Void visit(BreakNode node) {
        try {
            stmts.add(new Jump(node.location(), currentBreakTarget()));
        }
        catch (JumpError err) {
            throw new Error(err.getMessage());
        }
        return null;
    }

    public Void visit(ContinueNode node) {
        try {
            stmts.add(new Jump(node.location(), currentContinueTarget()));
        }
        catch (JumpError err) {
            throw new Error(err.getMessage());
        }
        return null;
    }

    public Void visit(ExprStmtNode node) {
        if (node.expr() == null) return null;
        Expr e = node.expr().accept(this); // not visitExpr(node.expr()) because it will increase nest level!!!
        return null;
    }

    static private long ifCnt = 0;
    static private long forCnt = 0;
    static private long logicAndCnt = 0;
    static private long logicOrCnt = 0;
    static private long whileCnt = 0;

    public Void visit(IfNode node) {
        Label thenLabel = new Label("_if_then_" + ifCnt);
        Label elseLabel = new Label("_if_else_" + ifCnt);
        Label endLabel = new Label("_if_end_" + (ifCnt++));
        Expr cond = visitExpr(node.cond());
        if (node.elseBody() == null) {
            stmts.add(new ConditionJump(node.location(), cond, thenLabel, endLabel));
            stmts.add(new LabelStmt(null, thenLabel));
            visitStmt(node.thenBody());
            stmts.add(new LabelStmt(null, endLabel));
        }
        else {
            stmts.add(new ConditionJump(node.location(), cond, thenLabel, elseLabel));
            stmts.add(new LabelStmt(null, thenLabel));
            visitStmt(node.thenBody());
            stmts.add(new Jump(null, endLabel));
            stmts.add(new LabelStmt(null, elseLabel));
            visitStmt(node.elseBody());
            stmts.add(new LabelStmt(null, endLabel));
        }
        return null;
    }

    public Void visit(ForNode node) {
        Label beginLabel = new Label("_for_begin_" + forCnt);
        Label bodyLabel = new Label("_for_body_" + forCnt);
        Label continueLabel = new Label("_for_continue_" + forCnt);
        Label endLabel = new Label("_for_end_" + (forCnt++));

        visitStmt(node.init());
        stmts.add(new LabelStmt(null, beginLabel));
        stmts.add(new ConditionJump(node.location(), visitExpr(node.cond()), bodyLabel, endLabel));
        stmts.add(new LabelStmt(null, bodyLabel));
        pushContinue(continueLabel);
        pushBreak(endLabel);
        visitStmt(node.body());
        popBreak();
        popContinue();
        stmts.add(new LabelStmt(null, continueLabel));
        visitStmt(node.incr());
        stmts.add(new Jump(null, beginLabel));
        stmts.add(new LabelStmt(null, endLabel));
        return null;
    }

    public Expr visit(FuncallNode node) {
        List<ExprNode> argList = node.args();
        List<Expr> args = new ArrayList<Expr>();
        Collections.reverse(argList);
        for (ExprNode arg : argList) {
            args.add(0, visitExpr(arg));
        }
        Expr call = new Call(visitExpr(node.expr()), args);
        if (isStatement()) {
            stmts.add(new ExprStmt(node.location(), call));
            return null;
        }
        else {
            DefinedVariable tmp = tmpVar(node.type());
            assign(node.location(), ref(tmp), call);
            return ref(tmp);
        }
    }

    public Expr visit(IntegerLiteralNode node) {
        return new Int(node.value());
    }

    public Expr visit(LogicalAndNode node) {
        Label rightLabel = new Label("_logic_and_right_" + logicAndCnt);
        Label endLabel = new Label("_logic_and_end_" + (logicAndCnt++));
        DefinedVariable var = tmpVar(node.type());
        assign(node.left().location(), ref(var), visitExpr(node.left()));
        stmts.add(new ConditionJump(node.location(), ref(var), rightLabel, endLabel));
        stmts.add(new LabelStmt(null, rightLabel));
        assign(node.right().location(), ref(var), visitExpr(node.right()));
        stmts.add(new LabelStmt(null, endLabel));
        if (isStatement())
            return null;
        else
            return ref(var);
    }

    public Expr visit(LogicalOrNode node) {
        Label rightLabel = new Label("_logic_or_right_" + logicOrCnt);
        Label endLabel = new Label("_logic_or_end_" + logicOrCnt);
        DefinedVariable var = tmpVar(node.type());
        assign(node.left().location(), ref(var), visitExpr(node.left()));
        stmts.add(new ConditionJump(node.location(), ref(var), rightLabel, endLabel));
        stmts.add(new LabelStmt(null, rightLabel));
        assign(node.right().location(), ref(var), visitExpr(node.right()));
        stmts.add(new LabelStmt(null, endLabel));
        if (isStatement())
            return null;
        else
            return ref(var);
    }

    public Expr visit(MemberNode node) {
        Expr expr = addressOf(visitExpr(node.expr()));  
        Expr offset = new Int(node.offset());
        Expr addr = new Bin(Op.ADD, expr, offset);
        if (node.isLoadable()) return new Mem(addr);
        else return addr;
    }

    public Expr visit(NewTypeNode node) {
        if (node.type() instanceof ArrayType) {
            ArrayType type = (ArrayType)(node.type());
            System.err.println(type.length());
            Expr exprSize = visitExpr(type.exprAllocSize());
            Expr exprLen = visitExpr(type.exprLen());
            return new New(exprSize, exprLen);
        }
        else return new New(node.type().allocSize());
    }
    
    public Expr visit(NullNode node) {
        return null;
    }
    
    public Expr visit(OpAssignNode node) {
        Expr rhs = visitExpr(node.rhs());
        Expr lhs = visitExpr(node.lhs());
        Type t = node.lhs().type();
        Op op = Op.internBinary(node.operator());
        return transformOpAssign(node.location(), op, t, lhs, rhs);
    }

    public Expr visit(PrefixOpNode node) { //++x -> x+=1
        Type t = node.expr().type();
        if (node.operator().equals("++"))
        	return transformOpAssign(node.location(), Op.ADD, t, visitExpr(node.expr()), imm(t, 1));
        else
        	return transformOpAssign(node.location(), Op.SUB, t, visitExpr(node.expr()), imm(t, 1));
    }

    public Void visit(ReturnNode node) {
        if (node.expr() == null) {
            stmts.add(new Return(node.location(), null));
        }
        else {
            stmts.add(new Return(node.location(), visitExpr(node.expr())));
        }
        return null;
    }

    public Expr visit(StringLiteralNode node) {
        return new Str(node.value(), node.originValue());
    }

    public Expr visit(SuffixOpNode node) {
        Expr expr = visitExpr(node.expr());
        Type t = node.expr().type();
        Op op;
        if (node.operator().equals("++")) op = Op.ADD;
        else op = Op.SUB;
        Location loc = node.location();
        if (isStatement()) { // x++ -> x+=1
            transformOpAssign(loc, op, t, expr, imm(t, 1));
            return null;
        }
        else /*if (expr.isVar())*/ { // f(x++) -> v=x, x=v+1, f(v)
            DefinedVariable v = tmpVar(t);
            assign(loc, ref(v), expr);
            assign(loc, expr, new Bin(op, ref(v), imm(t, 1)));
            return ref(v);
        }
    }

    public Expr visit(UnaryOpNode node) {
        if (node.operator().equals("+"))
            return visitExpr(node.expr());
        else
            return new Uni(Op.internUnary(node.operator()), visitExpr(node.expr()));
    }

    public Expr visit(VariableNode node) {
        Var var = ref(node.entity());
        if (node.isLoadable()) return var;
        else return addressOf(var);
    }

    public Void visit(WhileNode node) {
        Label beginLabel = new Label("_while_begin_" + whileCnt);
        Label bodyLabel = new Label("_while_body_" + whileCnt);
        Label endLabel = new Label("_while_end_" + (whileCnt++));
        stmts.add(new LabelStmt(null, beginLabel));
        stmts.add(new ConditionJump(node.location(), visitExpr(node.cond()), bodyLabel, endLabel));
        stmts.add(new LabelStmt(null, bodyLabel));
        pushContinue(beginLabel);
        pushBreak(endLabel);
        visitStmt(node.body());
        popBreak();
        popContinue();
        stmts.add(new Jump(null, beginLabel));
        stmts.add(new LabelStmt(null, endLabel));
        return null;
    }
}