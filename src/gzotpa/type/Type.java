package gzotpa.type;
import gzotpa.exception.*;

public abstract class Type {
    static final public long sizeUnknown = -1;
    
    public Type() {}

    public boolean isVoid() { return false; }
    public boolean isInt() { return false; }
    public boolean isInteger() { return false; }
    public boolean isArray() { return false; }
    public boolean isCompositeType() { return false; }
    public boolean isClass() { return false; }
    public boolean isUserType() { return false; }
    public boolean isFunction() { return false; }
    public boolean isPointer() { return false; }
    public boolean isCallable() { return false; }

    public boolean isAllocatedArray() { return false; }
    public boolean isIncompleteArray() { return false; }
    public boolean isScalar() { return false; }

    public Type baseType() {
        throw new SemanticError("#baseType called for undereferable type");
    }

    public IntegerType getIntegerType() { return (IntegerType)this; }
    public PointerType getPointerType() { return (PointerType)this; }
    public FunctionType getFunctionType() { return (FunctionType)this; }
    public CompositeType getCompositeType() { return (CompositeType)this; }
    public ArrayType getArrayType() { return (ArrayType)this; }
}
