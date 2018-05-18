package gzotpa.asm;
import java.util.*;

public class SymbolTable {
    protected String base;
    protected Map<UnnamedSymbol, String> map;

    public SymbolTable(String base) {
        this.base = base;
        this.map = new HashMap<UnnamedSymbol, String>();
    }
    
    public String symbolString(UnnamedSymbol sym) {
        String str = map.get(sym);
        if (str != null) {
            return str;
        }
        else {
            String newStr = newString();
            map.put(sym, newStr);
            return newStr;
        }
    }
    
    protected long seq = 0;
    
    protected String newString() {
        return base + (seq++);
    }
}