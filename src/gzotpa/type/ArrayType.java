package gzotpa.type;

public class ArrayType extends Type {
    protected Type baseType;
    protected long length;
    protected long pointerSize;
    static final protected long undefined = -1;

    public ArrayType(Type baseType, long length, long pointerSize) {
        this.baseType = baseType;
        this.length = length;
        this.pointerSize = pointerSize;
    }

    public ArrayType(Type baseType, long pointerSize) {
        this.baseType = baseType;
        this.length = undefined;
        this.pointerSize = pointerSize;
    }

    public Type baseType() {
        return baseType;
    }

    public boolean isPointer() { return true; }
    public boolean isAllocatedArray() {
        return length != undefined &&
            (!baseType.isArray() || baseType.isAllocatedArray());
    }

    public boolean isIncompleteArray() {
        if (!baseType.isArray()) return false;
        return !baseType.isAllocatedArray();
    }

    public boolean isType(Type type) {
        if (type instanceof NullType) return true;
        if (!(type instanceof ArrayType)) return false;
        return baseType.isType(((ArrayType)type).baseType());
    }
    
    public String toString() {
        if (length < 0) {
            return baseType.toString() + "[]";
        }
        else {
            return baseType.toString() + "[" + length + "]";
        }
    }

    public String typeName() {
        return "array";
    }

    public long size() {
        return pointerSize;

    }
    public boolean equals(Object other) {
        if (!(other instanceof ArrayType)) return false;
        ArrayType type = (ArrayType)other;
        return (baseType.equals(type.baseType) && length == type.length);
    }
}
