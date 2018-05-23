package gzotpa.asm;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class AssemblyCode {
    protected List<Assembly> assemblies;
    protected Map<Register, Integer> registerUsageCnt;

    public AssemblyCode() {
        registerUsageCnt = new HashMap<Register, Integer>();
    }

    public List<Assembly> assemblies() {
        return assemblies;
    }

    public void useRegister(Register reg) {
        registerUsageCnt.put(reg, registerUsageCnt(reg) + 1);
    }

    public boolean usesRegister(Register reg) {
        Integer n = registerUsageCnt.get(reg);
        if (n == null) n = 0;
        return n > 0;
    }

    public Integer registerUsageCnt(Register reg) {
        Integer n = registerUsageCnt.get(reg);
        if (n == null) n = 0;
        return n;
    }

    void addAssembly(Assembly as) {
        assemblies.add(as);
        as.statisticRegister(this);
    }
}