package gzotpa.ast;
import gzotpa.entity.*;
import java.util.List;
import java.util.ArrayList;

public class AST extends Node {
    protected Location source;
    protected Declarations declarations;
    protected ToplevelScope scope;

    public AST(Location source, Declarations declarations) {
        super();
        this.source = source;
        this.declarations = declarations;
    }
    
    public List<DefinedVariable> definedVariables() {
        return declarations.defvars();
    }

    public List<DefinedFunction> definedFunctions() {
        return declarations.defuns();
    }
    
    public List<Entity> declarations() {
        List<Entity> result = new ArrayList<Entity>();
        result.addAll(declarations.funcdecls());
        result.addAll(declarations.vardecls());
        return result;
    }

    public List<Entity> definitions() {
        List<Entity> result = new ArrayList<Entity>();
        result.addAll(declarations.defvars());
        result.addAll(declarations.defuns());
        return result;
    }

    public List<Entity> entities() {
        List<Entity> result = new ArrayList<Entity>();
        result.addAll(declarations.funcdecls);
        result.addAll(declarations.vardecls);
        result.addAll(declarations.defvars);
        result.addAll(declarations.defuns);
        return result;
    }

    public Location location() {
        return source;
    }

    public void setScope(ToplevelScope scope) {
        if (this.scope != null) {
            throw new Error("Gzotpa! Toplevel scope set twice!");
        }
        this.scope = scope;
    }

    public ToplevelScope scope() {
        if (this.scope == null) {
            throw new Error("Gzotpa! AST scope is null!");
        }
        return scope;
    }
}
