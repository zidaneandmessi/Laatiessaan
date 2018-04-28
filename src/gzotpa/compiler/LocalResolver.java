package gzotpa.compiler;
import gzotpa.ast.*;
import gzotpa.entity.*;
import gzotpa.exception.*;
import gzotpa.type.*;
import java.util.List;
import java.util.LinkedList;

public class LocalResolver extends Visitor {
    private final LinkedList<Scope> scopeStack;
    private boolean inFunc;
    private String currentClass;

    public LocalResolver() {
        this.scopeStack = new LinkedList<Scope>();
        inFunc = false;
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
        for (Entity decl : ast.declarations()) {
            toplevel.declareEntity(decl);
        }
        for (Entity def : ast.definitions()) {
            toplevel.defineEntity(def);
        }
        for (ClassNode cls : ast.definedClasses()) {
            currentClass = cls.name();
            resolveClass(cls, toplevel);
            currentClass = null;
        }
        resolveGlobalVarInitializers(ast.definedVariables());
        resolveFunctions(ast.definedFunctions());
        //toplevel.checkReferences();
        ast.setScope(toplevel);
    }

    private void resolveGlobalVarInitializers(List<DefinedVariable> vars) {
        for (DefinedVariable v : vars) {
            if (v.hasInitializer()) {
                resolve(v.initializer());
            }
        }
    }

    private void resolveFunctions(List<DefinedFunction> funcs) {
        for (DefinedFunction f : funcs) {
            pushScope(f.parameters());
            inFunc = true;
            resolve(f.body());
            f.setScope(popScope());
        }
    }

    private void resolveClass(ClassNode cl, ToplevelScope toplevel) throws SemanticException {
        List<DefinedVariable> vars = cl.decls().defvars();
        List<DefinedFunction> funcs = cl.decls().defuns();
        for (DefinedVariable v : vars) {
            if (v.hasInitializer()) {
                throw new Error("Gzotpa! Class member variable should not have initializer! " + v.name());
            }
        }
        vars.add(new DefinedVariable(cl.typeNode(), "this"));
        pushScope(vars);
        for (DefinedFunction f : funcs) {
            //f.setName(cl.name() + "." + f.name());
            //f.parameters().add(new Parameter(cl.typeNode(), "class"));
            toplevel.defineEntity(f);
            pushScope(f.parameters());
            inFunc = true;
            resolve(f.body());
            f.setScope(popScope());
        }
        popScope();
    }

    public Void visit(BreakNode node) {
        if (!currentScope().inLoop())
            throw new Error("Gzotpa! Unreasonable break statement!");
        return null;
    }

    public Void visit(ContinueNode node) {
        if (!currentScope().inLoop())
            throw new Error("Gzotpa! Unreasonable continue statement!");
        return null;
    }

    public Void visit(BlockNode node) {
        if (inFunc == false) {
            if (node.inLoop()) {
                pushScope(node.variables(), true);
            }
            else {
                pushScope(node.variables());
            }
            super.visit(node);
            node.setScope(popScope());
        }
        else {
            LocalScope scope = popScope();
            for (DefinedVariable var : node.variables()) {
                if (scope.isDefinedLocally(var.name()))
                    throw new Error("Gzotpa! Variable multiple declarations! " + var.name());
                scope.defineVariable(var);
            }
            scopeStack.addLast(scope);
            inFunc = false;
            super.visit(node);
            node.setScope((LocalScope)currentScope());
        }
        return null;
    }

    private void pushScope(List<? extends DefinedVariable> vars) {
        LocalScope scope = new LocalScope(currentScope(), currentScope().inLoop());
        for (DefinedVariable var : vars) {
            if (scope.isDefinedLocally(var.name()))
                throw new Error("Gzotpa! Variable multiple declarations! " + var.name());
            scope.defineVariable(var);
        }
        scopeStack.addLast(scope);
    }

    private void pushScope(List<? extends DefinedVariable> vars, boolean inLoop) {
        LocalScope scope = new LocalScope(currentScope(), inLoop);
        for (DefinedVariable var : vars) {
            if (scope.isDefinedLocally(var.name()))
                throw new Error("Gzotpa! Variable multiple declarations!");
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
        super.visit(node);
        node.expr().accept(this);
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
                else if (node.memFuncBase() instanceof ArefNode) {
                    node.setName("array" + node.name());
                }
                else if (node.memFuncBase() instanceof MemberNode) {
                    node.setName(((MemberNode)(node.memFuncBase())).type().typeName() + node.name());
                }
            }
            if (!currentScope().has(node.name())) {
                node.setName(currentClass + "." + node.name());
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