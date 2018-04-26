package gzotpa.type;

public class PointerType extends Type {
    protected long size;
    protected Type baseType;

    public PointerType(long size, Type baseType) {
        this.size = size;
        this.baseType = baseType;
    }

    public boolean isPointer() { return true; }
    public boolean isCallable() { return baseType.isFunction(); }
    
    public long size() {
        return size;
    }

    public Type baseType() {
        return baseType;
    }
    
    public boolean isType(Type type) {
        return type instanceof PointerType;
    }
    
    public String toString() {
        return baseType.toString() + "*";
    }
}
