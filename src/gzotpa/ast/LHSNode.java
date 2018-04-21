package gzotpa.ast;
import gzotpa.type.Type;

abstract public class LHSNode extends ExprNode {
    protected Type type, origType;

    public Type type() {
        return type != null ? type : origType();
    }

    public void setType(Type t) {
        this.type = t;
    }

    abstract protected Type origType();
}
