package gzotpa.type;
import gzotpa.ast.Location;

public class IntegerTypeRef extends TypeRef {
    static public IntegerTypeRef intRef(Location loc) {
        return new IntegerTypeRef("int", loc);
    }

    static public IntegerTypeRef intRef() {
        return new IntegerTypeRef("int");
    }

    protected String name;

    public IntegerTypeRef(String name) {
        this(name, null);
    }

    public IntegerTypeRef(String name, Location loc) {
        super(loc);
        this.name = name;
    }
    
    public String name() {
        return name;
    }

    static public IntegerTypeRef charRef() {
        return new IntegerTypeRef("char");
    }
}
