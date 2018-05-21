package gzotpa.asm;

abstract public class Register extends Operand {
    public boolean isRegister() {
        return true;
    }
    
    abstract public String dump();
}
