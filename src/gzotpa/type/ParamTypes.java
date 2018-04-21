package gzotpa.type;
import gzotpa.ast.Location;
import gzotpa.entity.ParamSlots;
import java.util.*;

public class ParamTypes extends ParamSlots<Type> {
    protected ParamTypes(Location loc, List<Type> paramDescs, boolean vararg) {
        super(loc, paramDescs, vararg);
    }

    public List<Type> types() {
        return paramDescriptors;
    }
}
