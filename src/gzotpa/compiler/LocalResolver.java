package gzotpa.compiler;
import gzotpa.ast.*;
import gzotpa.entity.*;
import gzotpa.exception.*;
import java.util.List;
import java.util.LinkedList;

public class LocalResolver extends Visitor {
    private final LinkedList<Scope> scopeStack;

    public LocalResolver() {
        this.scopeStack = new LinkedList<Scope>();
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
            resolve(func.body());
            func.setScope(popScope());
        }
    }

    public Void visit(BlockNode node) {
        pushScope(node.variables());
        super.visit(node);
        node.setScope(popScope());
        return null;
    }

    private void pushScope(List<? extends DefinedVariable> vars) {
        LocalScope scope = new LocalScope(currentScope());
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