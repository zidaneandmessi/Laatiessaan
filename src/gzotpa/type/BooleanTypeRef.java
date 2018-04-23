package gzotpa.type;
import gzotpa.ast.Location;

public class BooleanTypeRef extends TypeRef {
    protected String name;

    public BooleanTypeRef(String name) {
        this(name, null);
    }

    public BooleanTypeRef(String name, Location loc) {
        super(loc);
        this.name = name;
    }
    
    public String name() {
        return name;
    }
}
