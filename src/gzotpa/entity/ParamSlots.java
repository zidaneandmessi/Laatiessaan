package gzotpa.entity;
import gzotpa.ast.Location;
import java.util.List;

abstract public class ParamSlots<T> {
    protected Location location;
    protected List<T> paramDescriptors;
    protected boolean vararg;

    public ParamSlots(List<T> paramDescs) {
        this(null, paramDescs);
    }

    public ParamSlots(Location loc, List<T> paramDescs) {
        this(loc, paramDescs, false);
    }

    protected ParamSlots(Location loc, List<T> paramDescs, boolean vararg) {
        super();
        this.location = loc;
        this.paramDescriptors = paramDescs;
        this.vararg = vararg;
    }

    public int argc() {
        return paramDescriptors.size();
    }

    public Location location() {
        return location;
    }
}
