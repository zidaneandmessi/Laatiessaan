package gzotpa.asm;

abstract public class Register extends Operand {
    public boolean isRegister() {
        return true;
    }

    public void statisticRegister(AssemblyCode as) {
        as.useRegister(this);
    }
    
    abstract public String dump();
    abstract public String print();
}
