package gzotpa.ir;
import gzotpa.ast.*;
import gzotpa.entity.*;
import java.util.List;

public class IR {
    List<DefinedVariable> defvars;
    List<DefinedFunction> defuns;
    List<ClassNode> defcls; 
    ToplevelScope scope;

    public IR(List<DefinedVariable> defvars,
                List<DefinedFunction> defuns,  
                List<ClassNode> defcls,  
                ToplevelScope scope) {
        this.defvars = defvars;
        this.defuns = defuns;
        this.defcls = defcls;
        this.scope = scope;
    }

    public List<DefinedVariable> defvars() {
        return defvars;
    }

    public List<DefinedFunction> defuns() {
        return defuns;
    }

    public List<ClassNode> defcls() {
        return defcls;
    }

    public ToplevelScope scope() {
        return scope;
    }

    public void dump() {
        Dumper d = new Dumper(System.err);
        d.printClass(this);
        d.printVars("variables", defvars);
        d.printFuncs("functions", defuns);
    }
}