package gzotpa.asm;

public class Instruction extends Assembly {
    protected String name;
    protected Operand[] operands;

    public Instruction(String name) {
        this(name, new Operand[0]);
    }

    public Instruction(String name, Label label) {
        this(name, new Operand[] { new ImmediateValue(label) });
    }

    public Instruction(String name, Operand a1) {
        this(name, new Operand[] { a1 });
    }

    public Instruction(String name, Operand a1, Operand a2) {
        this(name, new Operand[] { a1, a2 });
    }

    public Instruction(String name, Operand[] operands) {
        this.name = name;
        this.operands = operands;
    }

    public String name() {
        return this.name;
    }

    public boolean isText() {
        return name.equals("section") &&
            operands[0] instanceof ImmediateValue &&
            ((ImmediateValue)operands[0]).val() instanceof LabelLiteral &&
            ((LabelLiteral)((ImmediateValue)operands[0]).val()).value().equals(".text");
    }

    public int numOperands() {
        return this.operands.length;
    }

    public Operand operand1() {
        return this.operands[0];
    }

    public Operand operand2() {
        return this.operands[1];
    }

    public String toString() {
        return "#<Insn " + name + ">";
    }

    public void statisticRegister(AssemblyCode as) {
        for (int i = 0; i < operands.length; i++) {
        	if (operands[i] instanceof Register)
        		((Register)operands[i]).statisticRegister(as);
        }
    }

    public String dump() {
        StringBuffer buf = new StringBuffer();
        buf.append("(Instruction ");
        buf.append(name);
        for (Operand oper : operands) {
            buf.append(" ").append(oper.dump());
        }
        buf.append(")");
        return buf.toString();
    }

    public String print() {
        StringBuffer buf = new StringBuffer();
        if (!name.equals("global") && !name.equals("section") && !name.equals("extern") && !name.equals("ALIGN"))
            buf.append("\t");
        buf.append(name + "\t");
        int cnt = 0;
        for (Operand oper : operands) {
            if (cnt == 0)
                buf.append(oper.print());
            else
                buf.append(", ").append(oper.print());
            cnt++;
        }
        return buf.toString();
    }
}