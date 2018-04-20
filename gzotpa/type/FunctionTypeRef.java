package gzotpa.type;
import gzotpa.ast.*;
import java.util.*;

public class FunctionTypeRef extends TypeRef {
    protected TypeRef returnType;
    protected ParamTypeRefs params;

    public FunctionTypeRef(TypeRef returnType, ParamTypeRefs params) {
        super(returnType.location());
        this.returnType = returnType;
        this.params = params;
    }

    public TypeRef returnType() {
        return returnType;
    }

    public ParamTypeRefs params() {
        return params;
    }
}
