package gzotpa.type;
import gzotpa.ast.Location;

public class StringTypeRef extends TypeRef {
    protected String name;

    public StringTypeRef(String name) {
        this(name, null);
    }

    public StringTypeRef(String name, Location loc) {
        super(loc);
        this.name = name;
    }
    
    public String name() {
        return name;
    }
    
    public boolean equals(Object other) {
        return other instanceof StringTypeRef;
    }

    public String toString() {
        return "string";
    }
}