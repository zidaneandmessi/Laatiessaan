package gzotpa.type;
import gzotpa.ast.Location;
import gzotpa.entity.ParamSlots;
import java.util.*;

public class ParamTypes extends ParamSlots<Type> {
    public ParamTypes(Location loc, List<Type> paramDescs, boolean vararg) {
        super(loc, paramDescs, vararg);
    }

    public ParamTypes(List<Type> paramDescs, boolean vararg) {
        super(null, paramDescs, vararg);
    }

    public void add(Type type) {
        paramDescriptors.add(type);
    }

    public List<Type> types() {
        return paramDescriptors;
    }
}
