package gzotpa.entity;
import gzotpa.exception.*;
import java.util.List;
import java.util.ArrayList;

abstract public class Scope {
    protected List<LocalScope> children;

    public Scope() {
        children = new ArrayList<LocalScope>();
    }

    abstract public boolean isToplevel();
    abstract public boolean inLoop();
    abstract public ToplevelScope toplevel();
    abstract public Scope parent();

    protected void addChild(LocalScope s) {
        children.add(s);
    }
    
    abstract public boolean has(String name);
    abstract public Entity get(String name) throws SemanticException;
}
