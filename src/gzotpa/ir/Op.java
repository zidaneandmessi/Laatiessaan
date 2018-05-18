package gzotpa.ir;

public enum Op {
    ADD,
    SUB,
    MUL,
    DIV,
    MOD,
    BIT_AND,
    BIT_OR,
    BIT_XOR,
    BIT_LSHIFT,
    ARITH_RSHIFT,

    EQ,
    NEQ,
    GT,
    GTEQ,
    LT,
    LTEQ,

    NEG,
    BIT_NOT,
    NOT;

    static public Op internBinary(String op) {
        if (op.equals("+")) {
            return Op.ADD;
        }
        else if (op.equals("-")) {
            return Op.SUB;
        }
        else if (op.equals("*")) {
            return Op.MUL;
        }
        else if (op.equals("/")) {
            return Op.DIV;
        }
        else if (op.equals("%")) {
            return Op.MOD;
        }
        else if (op.equals("&")) {
            return Op.BIT_AND;
        }
        else if (op.equals("|")) {
            return Op.BIT_OR;
        }
        else if (op.equals("^")) {
            return Op.BIT_XOR;
        }
        else if (op.equals("<<")) {
            return Op.BIT_LSHIFT;
        }
        else if (op.equals(">>")) {
            return Op.ARITH_RSHIFT;
        }
        else if (op.equals("==")) {
            return Op.EQ;
        }
        else if (op.equals("!=")) {
            return Op.NEQ;
        }
        else if (op.equals("<")) {
            return Op.LT;
        }
        else if (op.equals("<=")) {
            return Op.LTEQ;
        }
        else if (op.equals(">")) {
            return Op.GT;
        }
        else if (op.equals(">=")) {
            return Op.GTEQ;
        }
        else {
            throw new Error("Gzotpa! Unknown binary op: " + op);
        }
    }

    static public Op internUnary(String op) {
        if (op.equals("+")) {
            throw new Error("Gzotpa! Unary plus should not be in IR!");
        }
        else if (op.equals("-")) {
            return Op.NEG;
        }
        else if (op.equals("~")) {
            return Op.BIT_NOT;
        }
        else if (op.equals("!")) {
            return Op.NOT;
        }
        else {
            throw new Error("Gzotpa! Unknown unary op: " + op);
        }
    }
}
