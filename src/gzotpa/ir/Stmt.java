package gzotpa.ir;
import gzotpa.asm.*;
import gzotpa.ast.Location;
import gzotpa.entity.*;

abstract public class Stmt implements Dumpable {
    protected Location location;

    Stmt(Location loc) {
        this.location = loc;
    }
    
    public Location location() {
        return location;
    }
    
    abstract public <S,E> S accept(IRVisitor<S,E> visitor);
    
    public void dump(Dumper d) {
        d.printClass(this, location);
        _dump(d);
    }

    abstract protected void _dump(Dumper d);
}