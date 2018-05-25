package gzotpa.type;

public class IntegerType extends Type {
    protected long size;
    protected String name;
    protected boolean isBool;

    public IntegerType(long size, String name) {
        super();
        this.size = size;
        this.name = name;
        this.isBool = false;
    }

    public IntegerType(long size, String name, boolean isBool) {
        super();
        this.size = size;
        this.name = name;
        this.isBool = isBool;
    }

    public long size() {
        return size;
    }

    public boolean isBool() {
        return isBool;
    }

    public boolean isInteger() {
        return true;
    }

    public boolean isType(Type type) {
        return type instanceof IntegerType;
    }
    public boolean isEqualType(Type type) {
        return isType(type);
    }

    public String toString() {
        return name;
    }

    public String typeName() {
        return "int";
    }
}
