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

    public boolean isFunction() { return true; }

    public TypeRef returnType() {
        return returnType;
    }

    public ParamTypeRefs params() {
        return params;
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(returnType.toString());
        buf.append(" (");
        String sep = "";
        for (TypeRef ref : this.params.typerefs()) {
            buf.append(sep);
            buf.append(ref.toString());
            sep = ", ";
        }
        buf.append(")");
        return buf.toString();
    }
}
