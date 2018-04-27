package gzotpa.ast;
import gzotpa.entity.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.LinkedHashSet;

public class Declarations {

    Set<DefinedVariable> defvars = new LinkedHashSet<DefinedVariable>();
    Set<DefinedFunction> defuns = new LinkedHashSet<DefinedFunction>();
    Set<UndefinedVariable> vardecls = new LinkedHashSet<UndefinedVariable>();
    Set<UndefinedFunction> funcdecls = new LinkedHashSet<UndefinedFunction>();
    Set<ClassNode> defclasses = new LinkedHashSet<ClassNode>();

    public void addDefvar(DefinedVariable var) {
        defvars.add(var);
    }

    public void addDefvars(List<DefinedVariable> vars) {
        defvars.addAll(vars);
    }

    public void addDefun(DefinedFunction func) {
        defuns.add(func);
    }

    public List<DefinedVariable> defvars() {
        return new ArrayList<DefinedVariable>(defvars);
    }

    public List<DefinedFunction> defuns() {
        return new ArrayList<DefinedFunction>(defuns);
    }

    public List<UndefinedVariable> vardecls() {
        return new ArrayList<UndefinedVariable>(vardecls);
    }

    public List<UndefinedFunction> funcdecls() {
        return new ArrayList<UndefinedFunction>(funcdecls);
    }

    public void addVardecl(UndefinedVariable var) {
        vardecls.add(var);
    }

    public void addFuncdecl(UndefinedFunction func) {
        funcdecls.add(func);
    }

    public void addDefclass(ClassNode n) {
        defclasses.add(n);
    }

    public List<ClassNode> defclasses() {
        return new ArrayList<ClassNode>(defclasses);
    }
}
