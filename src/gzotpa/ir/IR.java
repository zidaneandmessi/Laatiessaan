package gzotpa.ir;
import gzotpa.ast.*;
import gzotpa.entity.*;
import java.util.List;

public class IR {
    List<DefinedVariable> defvars;
    List<DefinedFunction> defuns;
    List<ClassNode> defcls; 
    ToplevelScope scope;
    long maxLenStackSize;

    public IR(List<DefinedVariable> defvars, List<DefinedFunction> defuns, List<ClassNode> defcls, ToplevelScope scope, long maxLenStackSize) {
        this.defvars = defvars;
        this.defuns = defuns;
        this.defcls = defcls;
        this.scope = scope;
        this.maxLenStackSize = maxLenStackSize;
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

    public void setMaxLenStackSize(long maxLenStackSize) {
        this.maxLenStackSize = maxLenStackSize;
    }

    public long maxLenStackSize() {
        return maxLenStackSize;
    }

    public void dump() {
        Dumper d = new Dumper(System.err);
        d.printClass(this);
        d.printVars("variables", defvars);
        d.printFuncs("functions", defuns);
    }
}