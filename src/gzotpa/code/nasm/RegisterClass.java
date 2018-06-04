package gzotpa.code.nasm;

public enum RegisterClass {
    R0(0), R1(1), R2(2), R3(3), R4(4), R5(5), R6(6), R7(7), R8(8), R9(9), R10(10), R11(11), R12(12), R13(13), R14(14), R15(15),
    RAX(16), RCX(17), RDX(18), RBX(19), RSP(20), RBP(21), RSI(22), RDI(23);
    private int index;

    private RegisterClass(int index) {
        this.index = index;
    }

    public String transFormat(long size) {
        StringBuffer s = new StringBuffer(toString());
        if (size == 32) {
            if (index <= 15) {
                s.append("D");
            }
            else {
                s.replace(0, 1, "E");
            }
        }
        else if (size == 16) {
            if (index <= 15) {
                s.append("W");
            }
            else {
                s.delete(0, 1);
            }
        }
        else if (size == 8) {
            if (index <= 15) {
                s.append("B");
            }
            else {
                s.delete(0, 1);
                if (index <= 19) {
                    s.replace(1, 2 ,"L");
                }
                else {
                    s.append("L");
                }
            }
        }
        else if (size != 64) {
            throw new Error("Gzotpa! Unknown register size!");
        }
        return s.toString();
    }
}
