package gzotpa.ast;
import gzotpa.entity.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.LinkedHashSet;

public class Declarations {

    Set<DefinedVariable> defvars = new LinkedHashSet<DefinedVariable>();
    Set<DefinedVariable> defvarsaftermain = new LinkedHashSet<DefinedVariable>();
    Set<DefinedFunction> defuns = new LinkedHashSet<DefinedFunction>();
    Set<ClassNode> defclasses = new LinkedHashSet<ClassNode>();

    public void addDefvar(DefinedVariable var) {
        defvars.add(var);
    }

    public void addDefvars(List<DefinedVariable> vars) {
        defvars.addAll(vars);
    }

    public void addDefvaraftermain(DefinedVariable var) {
        defvarsaftermain.add(var);
    }

    public void addDefvarsaftermain(List<DefinedVariable> vars) {
        defvarsaftermain.addAll(vars);
    }

    public void addDefun(DefinedFunction func) {
        defuns.add(func);
    }

    public List<DefinedVariable> defvars() {
        return new ArrayList<DefinedVariable>(defvars);
    }

    public List<DefinedVariable> defvarsaftermain() {
        return new ArrayList<DefinedVariable>(defvarsaftermain);
    }

    public List<DefinedFunction> defuns() {
        return new ArrayList<DefinedFunction>(defuns);
    }

    public List<ClassNode> defclasses() {
        return new ArrayList<ClassNode>(defclasses);
    }

    public void addDefclass(ClassNode n) {
        defclasses.add(n);
    }
}
