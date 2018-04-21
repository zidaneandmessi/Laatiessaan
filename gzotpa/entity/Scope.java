package gzotpa.entity;
import gzotpa.exception.*;
import java.util.List;
import java.util.ArrayList;

abstract public class Scope {
    protected List<LocalScope> children;

    public Scope() {
        children = new ArrayList<LocalScope>();
    }

    protected void addChild(LocalScope s) {
        children.add(s);
    }
}
