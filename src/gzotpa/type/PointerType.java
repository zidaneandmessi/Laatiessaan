package gzotpa.type;

public class PointerType extends Type {
    protected long size;
    protected Type baseType;
    private boolean handled = false;

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

    public boolean handled() {
        return handled;
    }

    public void handle() {
        handled = true;
    }
    
    public boolean isType(Type type) {
        return type instanceof PointerType;
    }
    public boolean isEqualType(Type type) {
        return isType(type);
    }
    
    public String toString() {
        return baseType.toString() + "*";
    }

    public String typeName() {
        return "pointer";
    }
}
