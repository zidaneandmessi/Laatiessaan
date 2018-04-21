package gzotpa.type;
import gzotpa.ast.Location;

public class VoidTypeRef extends TypeRef {
    public VoidTypeRef() {
        super(null);
    }

    public VoidTypeRef(Location loc) {
        super(loc);
    }
}
