package gzotpa.asm;

abstract public class MemoryReference extends Operand {
    abstract public void fixOffset(long diff);
}
