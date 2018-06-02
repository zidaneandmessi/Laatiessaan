package gzotpa.ast;
import gzotpa.entity.Variable;
import gzotpa.type.Type;

abstract public class LHSNode extends ExprNode {
    protected Type type, origType;

    public Type type() {
        return type != null ? type : origType();
    }

    public void setType(Type t) {
        this.type = t;
    }

    abstract public Type origType();

    public boolean isLoadable() {
        Type t = origType();
        return !t.isFunction();
    }
    
    public boolean isAssignable() {
        if (this instanceof VariableNode && ((VariableNode)this).name().equals("this"))
            return false;
        Type t = origType();
        return !t.isFunction();
    }
}
