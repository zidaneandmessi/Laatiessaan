package gzotpa.type;
import gzotpa.exception.*;

public abstract class Type {
    static final public long sizeUnknown = -1;
    
    public Type() {}

    public Type baseType() {
        throw new SemanticError("#baseType called for undereferable type");
    }

    public IntegerType getIntegerType() { return (IntegerType)this; }
    public PointerType getPointerType() { return (PointerType)this; }
    public FunctionType getFunctionType() { return (FunctionType)this; }
    public CompositeType getCompositeType() { return (CompositeType)this; }
    public ArrayType getArrayType() { return (ArrayType)this; }
}
