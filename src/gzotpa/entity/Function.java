package gzotpa.entity;
import gzotpa.ast.TypeNode;
import gzotpa.type.FunctionType;
import gzotpa.type.Type;

abstract public class Function extends Entity {

    public Function(TypeNode t, String name) {
        super(t, name);
    }

    public Type returnType() {
        return type().getFunctionType().returnType();
    }
    
    public boolean isVoid() {
        return returnType().isVoid();
    }
}
