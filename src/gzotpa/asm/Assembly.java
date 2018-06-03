package gzotpa.asm;

abstract public class Assembly {
    abstract public String name();
    abstract public String dump();
    abstract public String print();

    abstract public void statisticRegister(AssemblyCode as);
}
