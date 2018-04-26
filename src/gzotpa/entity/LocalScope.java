package gzotpa.entity;
import gzotpa.type.Type;
import gzotpa.exception.*;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class LocalScope extends Scope {
    protected Scope parent;
    protected boolean inLoop;
    protected Map<String, DefinedVariable> variables;

    public LocalScope(Scope parent) {
        super();
        this.parent = parent;
        this.inLoop = false;
        parent.addChild(this);
        variables = new LinkedHashMap<String, DefinedVariable>();
    }

    public LocalScope(Scope parent, boolean inLoop) {
        super();
        this.parent = parent;
        this.inLoop = inLoop;
        parent.addChild(this);
        variables = new LinkedHashMap<String, DefinedVariable>();
    }

    public Scope parent() {
        return this.parent;
    }

    public boolean isToplevel() {
        return false;
    }

    public boolean inLoop() {
        return inLoop;
    }

    public ToplevelScope toplevel() {
        return parent.toplevel();
    }

    public List<LocalScope> children() {
        return children;
    }

    public boolean isDefinedLocally(String name) {
        return variables.containsKey(name);
    }

    public void defineVariable(DefinedVariable var) {
        variables.put(var.name(), var);
    }

    public Entity get(String name) throws SemanticException {
        DefinedVariable var = variables.get(name);
        if (var != null) return var;
        else return parent.get(name);
    }
}
