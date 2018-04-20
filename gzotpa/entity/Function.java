package gzotpa.entity;
import gzotpa.ast.TypeNode;
import gzotpa.type.FunctionType;
import gzotpa.type.Type;

abstract public class Function extends Entity {
    protected Symbol callingSymbol;
    protected Label label;

    public Function(boolean priv, TypeNode t, String name) {
        super(priv, t, name);
    }

    public Type returnType() {
        return type().getFunctionType().returnType();
    }
}
