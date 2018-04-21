package gzotpa.type;
import gzotpa.ast.Slot;
import gzotpa.ast.Location;
import gzotpa.exception.*;
import java.util.List;

abstract public class CompositeType extends NamedType {
    protected List<Slot> members;

    public CompositeType(String name, List<Slot> membs, Location loc) {
        super(name, loc);
        this.members = membs;
    }

    public Type memberType(String name) {
        return fetch(name).type();
    }
    
    protected Slot fetch(String name) {
        Slot s = get(name);
        if (s == null) {
            throw new SemanticError("no such member in "
                                    + toString() + ": " + name);
        }
        return s;
    }

    public Slot get(String name) {
        for (Slot s : members) {
            if (s.name().equals(name)) {
                return s;
            }
        }
        return null;
    }
}
