package gzotpa.ast;
import gzotpa.entity.*;
import gzotpa.ir.*;
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

    public List<ClassNode> definedClasses() {
        return declarations.defclasses();
    }
    
    public List<DefinedVariable> unusedVariables() {
        return declarations.defvarsaftermain();
    }

    public List<Entity> definitions() {
        List<Entity> result = new ArrayList<Entity>();
        result.addAll(declarations.defvars());
        result.addAll(declarations.defuns());
        return result;
    }

    public List<Entity> entities() {
        List<Entity> result = new ArrayList<Entity>();
        result.addAll(declarations.defvars);
        result.addAll(declarations.defuns);
        return result;
    }

    public List<TypeDefinitionNode> types() {
        List<TypeDefinitionNode> result = new ArrayList<TypeDefinitionNode>();
        result.addAll(declarations.defclasses());
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

    public IR ir() {
        return new IR(definedVariables(), definedFunctions(), scope());
    }
}
