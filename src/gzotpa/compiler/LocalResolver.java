package gzotpa.compiler;
import gzotpa.ast.*;
import gzotpa.entity.*;
import gzotpa.exception.*;
import java.util.List;
import java.util.LinkedList;

public class LocalResolver extends Visitor {
    private final LinkedList<Scope> scopeStack;
    private boolean inFunc;

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
        resolveGlobalVarInitializers(ast.definedVariables());
        resolveFunctions(ast.definedFunctions());
        //toplevel.checkReferences();
        ast.setScope(toplevel);
    }

    private void resolveGlobalVarInitializers(List<DefinedVariable> gvars) {
        for (DefinedVariable v : gvars) {
            if (v.hasInitializer()) {
                resolve(v.initializer());
            }
        }
    }

    private void resolveFunctions(List<DefinedFunction> funcs) {
        for (DefinedFunction func : funcs) {
            pushScope(func.parameters());
            inFunc = true;
            resolve(func.body());
            System.err.println(scopeStack.size());
            func.setScope(popScope());
            inFunc = false;
        }
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
    // Not support only break/continue statement like "for(...)if(...)break;".

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
                    throw new Error("Gzotpa! Variable multiple declarations!");
                scope.defineVariable(var);
            }
            scopeStack.addLast(scope);
            super.visit(node);
            node.setScope((LocalScope)currentScope());
        }
        return null;
    }

    private void pushScope(List<? extends DefinedVariable> vars) {
        LocalScope scope = new LocalScope(currentScope(), currentScope().inLoop());
        for (DefinedVariable var : vars) {
            if (scope.isDefinedLocally(var.name()))
                throw new Error("Gzotpa! Variable multiple declarations!");
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

    public Void visit(VariableNode node) {
        try {
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