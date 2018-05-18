package gzotpa.type;

public class VoidType extends Type {

    public VoidType() {}

    public boolean isVoid() { return true; }

    public long size() {
        return 1;
    }

    public boolean equals(Object other) {
        return (other instanceof VoidType);
    }

    public boolean isType(Type type) {
        return type instanceof VoidType;
    }
    public boolean isEqualType(Type type) {
        return isType(type);
    }
    
    public String toString() {
        return "void";
    }

    public String typeName() {
        return "void";
    }
}
