package gzotpa.code.nasm;

class Register extends gzotpa.asm.Register {
    RegisterClass _class;
    long size;

    Register(RegisterClass _class, long size) {
        this._class = _class;
        this.size = size;
    }

    public RegisterClass registerClass() {
        return _class;
    }

    public long size() {
        return size;
    }

    public boolean isRegister() { return true; }

    public boolean equals(Object other) {
        return (other instanceof Register) && equals((Register)other);
    }

    public boolean equals(Register reg) {
        return _class.equals(reg._class);
    }

    public int hashCode() {
        return _class.hashCode();
    }

    public String dump() {
        return "(Register " + _class.transFormat(size) + ")";
    }

    public String print() {
        return _class.transFormat(size).toLowerCase();
    }
}
