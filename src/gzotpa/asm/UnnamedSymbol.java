package gzotpa.asm;

public class UnnamedSymbol implements Symbol {
    public UnnamedSymbol() {
        super();
    }

    public String name() {
        throw new Error("unnamed symbol");
    }

    public String toSource() {
        throw new Error("UnnamedSymbol#toSource() called");
    }

    public String toSource(SymbolTable table) {
        return table.symbolString(this);
    }

    public String toString() {
        return super.toString();
    }
}
