package gzotpa.asm;

public class Global extends Assembly {
    protected Label label;

    public Global(Label label) {
        this.label = label;
    }

    public Label label() {
        return label;
    }

    public String dump() {
        return "(Global " + label.name() + ")";
    }
}