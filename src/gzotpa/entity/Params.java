package gzotpa.entity;
import gzotpa.type.*;
import gzotpa.ast.Location;
import java.util.List;
import java.util.ArrayList;

public class Params extends ParamSlots<Parameter> {
    public Params(Location loc, List<Parameter> paramDescs) {
        super(loc, paramDescs, false);
    }

    public Params(List<Parameter> paramDescs) {
        super(null, paramDescs, false);
    }

    public List<Parameter> parameters() {
        return paramDescriptors;
    }
    
    public void addParameter(Parameter para) {
        paramDescriptors.add(para);
    }

    public long size() {
        return paramDescriptors.size();
    }

    public ParamTypeRefs parametersTypeRef() {
        List<TypeRef> typerefs = new ArrayList<TypeRef>();
        for (Parameter param : paramDescriptors) {
            typerefs.add(param.typeNode().typeRef());
        }
        return new ParamTypeRefs(location, typerefs, vararg);
    }
}
