package gzotpa.type;
import gzotpa.entity.ParamSlots;
import gzotpa.ast.Location;
import java.util.*;

public class ParamTypeRefs extends ParamSlots<TypeRef> {
    public ParamTypeRefs(List<TypeRef> paramDescs) {
        super(paramDescs);
    }
    
    public ParamTypeRefs(Location loc, List<TypeRef> paramDescs, boolean vararg) {
        super(loc, paramDescs, vararg);
    }

    public List<TypeRef> typerefs() {
        return paramDescriptors;
    }
}
