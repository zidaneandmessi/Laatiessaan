package gzotpa.entity;
import gzotpa.type.Type;
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

    public List<LocalScope> children() {
        return children;
    }
}
