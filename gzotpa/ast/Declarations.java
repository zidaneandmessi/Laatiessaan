package gzotpa.ast;
import gzotpa.entity.*;
import java.util.List;
import java.util.Set;
import java.util.LinkedHashSet;

public class Declarations {

    Set<DefinedVariable> defvars = new LinkedHashSet<DefinedVariable>();
    Set<DefinedFunction> defuns = new LinkedHashSet<DefinedFunction>();

    public void addDefvar(DefinedVariable var) {
        defvars.add(var);
    }

    public void addDefvars(List<DefinedVariable> vars) {
        defvars.addAll(vars);
    }

    public void addDefun(DefinedFunction func) {
        defuns.add(func);
    }
}
