package gzotpa.entity;
import gzotpa.type.Type;
import gzotpa.exception.*;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class LocalScope extends Scope {
    protected Scope parent;
    protected Map<String, DefinedVariable> variables;

    public LocalScope(Scope parent) {
        super();
        this.parent = parent;
        parent.addChild(this);
        variables = new LinkedHashMap<String, DefinedVariable>();
    }

    public Scope parent() {
        return this.parent;
    }

    public boolean isToplevel() {
        return false;
    }

    public ToplevelScope toplevel() {
        return parent.toplevel();
    }

    public List<LocalScope> children() {
        return children;
    }

    public Map<String, DefinedVariable> variables() {
        return variables;
    }

    public boolean isDefinedLocally(String name) {
        return variables.containsKey(name);
    }

    public void defineVariable(DefinedVariable var) {
        variables.put(var.name(), var);
    }

    public boolean has(String name) {
        DefinedVariable var = variables.get(name);
        if (var != null) return true;
        else return parent.has(name);
    }

    public Entity get(String name) throws SemanticException {
        DefinedVariable var = variables.get(name);
        if (var != null) return var;
        else return parent.get(name);
    }

    public DefinedVariable allocateTmp(Type t) {
        DefinedVariable var = DefinedVariable.tmpVariable(t);
        defineVariable(var);
        return var;
    }
}
