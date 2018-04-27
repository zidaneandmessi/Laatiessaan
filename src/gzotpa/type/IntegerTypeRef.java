package gzotpa.type;
import gzotpa.ast.Location;

public class IntegerTypeRef extends TypeRef {
    boolean isBool;

    static public IntegerTypeRef intRef(Location loc) {
        return new IntegerTypeRef("int", loc);
    }

    static public IntegerTypeRef intRef(Location loc, boolean isBool) {
        return new IntegerTypeRef("int", loc, isBool);
    }

    static public IntegerTypeRef intRef() {
        return new IntegerTypeRef("int");
    }

    static public IntegerTypeRef intRef(boolean isBool) {
        return new IntegerTypeRef("int", isBool);
    }

    static public IntegerTypeRef charRef() {
        return new IntegerTypeRef("char");
    }

    protected String name;

    public IntegerTypeRef(String name) {
        this(name, null);
        this.isBool = false;
    }

    public IntegerTypeRef(String name, boolean isBool) {
        this(name, null);
        this.isBool = isBool;
    }

    public IntegerTypeRef(String name, Location loc) {
        super(loc);
        this.name = name;
        this.isBool = false;
    }

    public IntegerTypeRef(String name, Location loc, boolean isBool) {
        super(loc);
        this.name = name;
        this.isBool = isBool;
    }
    
    public boolean equals(Object other) {
        if (!(other instanceof IntegerTypeRef)) return false;
        IntegerTypeRef ref = (IntegerTypeRef)other;
        return name.equals(ref.name) && (isBool == ref.isBool);
    }

    public String name() {
        return name;
    }

    public String toString() {
        return name;
    }
}
