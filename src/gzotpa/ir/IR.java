package gzotpa.ir;
import gzotpa.ast.*;
import gzotpa.entity.*;
import java.util.List;
import java.util.ArrayList;

public class IR {
    List<DefinedVariable> defvars;
    List<DefinedVariable> gvars, comms;
    List<DefinedFunction> defuns;
    ToplevelScope scope;

    public IR(List<DefinedVariable> defvars,
                List<DefinedFunction> defuns,  
                ToplevelScope scope) {
        this.defvars = defvars;
        this.defuns = defuns;
        this.scope = scope;
    }

    public List<DefinedVariable> defvars() {
        return defvars;
    }

    public List<DefinedFunction> defuns() {
        return defuns;
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