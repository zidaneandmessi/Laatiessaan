package gzotpa.asm;

public class Label extends Assembly {
    protected String name;

    public Label() {
        this("Unnamed");
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

    public String dump() {
        return "(Label " + name + ")";
    }
}
