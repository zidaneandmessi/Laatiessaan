package gzotpa.asm;

public interface Literal {
    public String toSource();
    public String toSource(SymbolTable table);
}