package gzotpa.type;
import gzotpa.entity.Params;
import java.util.*;

public class FunctionType extends Type {
    protected Type returnType;
    protected ParamTypes paramTypes;

    public FunctionType(Type ret, ParamTypes partypes) {
        returnType = ret;
        paramTypes = partypes;
    }

    public Type returnType() {
        return returnType;
    }
    
    public List<Type> paramTypes() {
        return paramTypes.types();
    }

    public long argc() {
        return paramTypes.argc();
    }
}
