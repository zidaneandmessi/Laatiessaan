package gzotpa.asm;

public class Label extends Assembly {
    protected String name;
    static private long nameCnt = 0;

    public Label() {
        this("_unnamed_" + (nameCnt++));
    }

    public Label(String name) {
        this.name = name;
    }
    
    public String name() {
        return name;
    }

    public boolean isLabel() {
        return true;
    }

    public boolean equals(Object other) {
        return (other instanceof Label)
                && name.equals(((Label)other).name());
    }
    
    public void statisticRegister(AssemblyCode as) {}

    public String dump() {
        return "(Label " + name + ")";
    }

    public String print() {
        return name + ":";
    }
}
