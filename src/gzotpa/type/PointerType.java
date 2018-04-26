package gzotpa.type;

public class PointerType extends Type {
    protected long size;
    protected Type baseType;

    public PointerType(long size, Type baseType) {
        this.size = size;
        this.baseType = baseType;
    }
    public long size() {
        return size;
    }

    public Type baseType() {
        return baseType;
    }
    
    public String toString() {
        return baseType.toString() + "*";
    }
}
