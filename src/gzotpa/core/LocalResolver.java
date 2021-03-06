package gzotpa.core;
import gzotpa.ast.*;
import gzotpa.entity.*;
import gzotpa.exception.*;
import gzotpa.type.*;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

public class LocalResolver extends Visitor {
    private final LinkedList<Scope> scopeStack;
    private boolean inFunc;
    private int inLoop;
    private ClassNode currentClass;
    private DefinedVariable currentThis;

    public LocalResolver() {
        this.scopeStack = new LinkedList<Scope>();
        inFunc = false;
        inLoop = 0;
    }

    private void resolve(StmtNode n) {
        n.accept(this);
    }

    private void resolve(ExprNode n) {
        n.accept(this);
    }

    public void resolve(AST ast) throws SemanticException {
        ToplevelScope toplevel = new ToplevelScope();
        scopeStack.add(toplevel);
        for (Entity def : ast.definitions()) {
            toplevel.defineEntity(def);
        }
        for (Entity decl : ast.unusedVariables()) {
            toplevel.declareEntity(decl);
        }
        for (ClassNode cls : ast.definedClasses()) {
            currentClass = cls;
            declareClassEntities(cls, toplevel);
            currentClass = null;
        }
        for (ClassNode cls : ast.definedClasses()) {
            currentClass = cls;
            resolveClass(cls, toplevel);
            currentClass = null;
        }
        resolveGlobalVarInitializers(ast.definedVariables());
        resolveFunctions(ast.definedFunctions());
        //toplevel.checkReferences();
        ast.setScope(toplevel);
    }

    private void resolveGlobalVarInitializers(List<DefinedVariable> vars) {
        for (DefinedVariable var : vars) {
            if (var.hasInitializer()) {
                resolve(var.initializer());
            }
        }
    }

    private void resolveFunctions(List<DefinedFunction> funcs) {
        for (DefinedFunction func : funcs) {
            pushScope(func.parameters());
            inFunc = true;
            resolve(func.body());
            func.setScope(popScope());
        }
    }

    private void declareClassEntities(ClassNode cl, ToplevelScope toplevel) throws SemanticException {
        List<DefinedVariable> vars = cl.decls().defvars();
        List<DefinedFunction> funcs = cl.decls().defuns();
        for (DefinedVariable var : vars) {
            if (var.hasInitializer()) {
                throw new Error("Gzotpa! Class member variable should not have initializer! " + var.name());
            }
        }
        for (DefinedFunction func : funcs) {
            toplevel.defineEntity(func);
        }
    }

    private void resolveClass(ClassNode cl, ToplevelScope toplevel) throws SemanticException {
        List<DefinedVariable> vars = cl.decls().defvars();
        List<DefinedFunction> funcs = cl.decls().defuns();
        pushScope(vars);
        for (DefinedFunction func : funcs) {
            pushScope(func.parameters());
            for (Parameter param : func.parameters()) {
                if (param.name().equals("this")) {
                    currentThis = (DefinedVariable)param;
                }
            }
            inFunc = true;
            resolve(func.body());
            func.setScope(popScope());
        }
        popScope();
        currentThis = null;
    }

    public Void visit(BreakNode node) {
        if (inLoop == 0)
            throw new Error("Gzotpa! Unreasonable break statement!");
        return null;
    }

    public Void visit(ContinueNode node) {
        if (inLoop == 0)
            throw new Error("Gzotpa! Unreasonable continue statement!");
        return null;
    }

    public Void visit(ForNode node) {
        inLoop++;
        super.visit(node);
        inLoop--;
        return null;
    }

    public Void visit(WhileNode node) {
        inLoop++;
        super.visit(node);
        inLoop--;
        return null;
    }

    public Void visit(BlockNode node) {
        if (inFunc == false) {
            LocalScope scope = new LocalScope(currentScope());
            Iterator<DefinedVariable> vars = node.variables().iterator();
            Iterator<StmtNode> stmts = node.stmts().iterator();
            for (Boolean b : node.order()) {
                if (b == true) {
                    DefinedVariable var = vars.next();
                    if (scope.isDefinedLocally(var.name()))
                        throw new Error("Gzotpa! Variable multiple declarations! " + var.name());
                    scopeStack.addLast(scope);
                    super.visitVariable(var);
                    scope.defineVariable(var);
                    scope = popScope();
                }
                else if (b == false) {
                    scopeStack.addLast(scope);
                    StmtNode stmt = stmts.next();
                    if (stmt != null)
                        super.visitStmt(stmt);
                    scope = popScope();
                }
            }
            scopeStack.addLast(scope);
            node.setScope(popScope());
        }
        else if (inFunc == true) {
            LocalScope scope;
            Iterator<DefinedVariable> vars = node.variables().iterator();
            Iterator<StmtNode> stmts = node.stmts().iterator();
            for (Boolean b : node.order()) {
                if (b == true) {
                    inFunc = true;
                    scope = popScope();
                    DefinedVariable var = vars.next();
                    if (scope.isDefinedLocally(var.name()))
                        throw new Error("Gzotpa! Variable multiple declarations! " + var.name());
                    scopeStack.addLast(scope);
                    super.visitVariable(var);
                    scope.defineVariable(var);
                    inFunc = false;
                }
                else if(b == false) {
                    StmtNode stmt = stmts.next();
                    if (stmt != null)
                    {
                        inFunc = false;
                        super.visitStmt(stmt);
                        inFunc = true;
                    }
                }
            }
            node.setScope((LocalScope)currentScope());
        }
        return null;
    }

    private void pushScope(List<? extends DefinedVariable> vars) {
        LocalScope scope = new LocalScope(currentScope());
        for (DefinedVariable var : vars) {
            if (scope.isDefinedLocally(var.name()))
                throw new Error("Gzotpa! Variable multiple declarations! " + var.name());
            scope.defineVariable(var);
        }
        scopeStack.addLast(scope);
    }

    private LocalScope popScope() {
        return (LocalScope)scopeStack.removeLast();
    }

    private Scope currentScope() {
        return scopeStack.getLast();
    }

    public Void visit(FuncallNode node) {
        node.expr().accept(this);
        if (node.expr() instanceof VariableNode) {
            VariableNode var = (VariableNode)node.expr();
            if (var.implicitThis()) {   
                 node.addArg(new VariableNode("this")); 
                 var.setImplicitThis(false);    
             }
        }
        for (ExprNode e : node.args()) {
            e.accept(this);
        }
        return null;
    }

    public Void visit(MemberNode node) {
        super.visit(node);
        node.expr().accept(this);
        return null;
    }

    public Void visit(VariableNode node) {
        super.visit(node);
        try {
            if (node.name().charAt(0) == '.')
            {
                node.memFuncBase().accept(this);
                if (node.memFuncBase() instanceof VariableNode) {
                    node.setName(node.memFuncBase().type().typeName() + node.name());
                }
                else if (node.memFuncBase() instanceof FuncallNode) {
                    node.setName(((FunctionType)(((FuncallNode)(node.memFuncBase())).expr().type())).returnType().typeName() + node.name());
                }
                else if (node.memFuncBase() instanceof StringLiteralNode) {
                    node.setName("string" + node.name());
                }
                else if (node.memFuncBase() instanceof BinaryOpNode) {
                    node.setName(((BinaryOpNode)(node.memFuncBase())).left().type().typeName() + node.name());
                }
                else if (node.memFuncBase() instanceof ArefNode) {
                    node.setName(((ArefNode)node.memFuncBase()).type().typeName() + node.name());
                }
                else if (node.memFuncBase() instanceof MemberNode) {
                    node.setName(((MemberNode)(node.memFuncBase())).type().typeName() + node.name());
                }
                else if (node.memFuncBase() instanceof NewTypeNode) {
                    node.setName(((NewTypeNode)(node.memFuncBase())).type().typeName() + node.name());
                }
            }
            if (currentClass != null && currentScope().has(currentClass.name() + "." + node.name())) {
                node.setName(currentClass.name() + "." + node.name());
                node.setImplicitThis(true);
            }
            else if (currentClass != null &&
                        !node.name().contains(".") &&
                        currentScope() instanceof LocalScope &&
                        !((LocalScope)currentScope()).isDefinedLocally(node.name())
                        && ((ClassType)(currentClass.type())).hasMemberVariable(node.name())) {
                node.setMemVarBase(new MemberNode(new VariableNode(currentThis), node.name()));
            }
            Entity ent = currentScope().get(node.name());
            ent.refered();
            node.setEntity(ent);
        }
        catch(SemanticException ex){
            throw new Error(ex.getMessage());
        }
        return null;
    }
}