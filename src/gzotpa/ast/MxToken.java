package gzotpa.ast;
import gzotpa.parser.Token;
import gzotpa.parser.ParserConstants;

public class MxToken {
    protected Token token;
    protected boolean isSpecial;

    public MxToken(Token token) {
        this(token, false);
    }

    public MxToken(Token token, boolean isSpecial) {
        this.token = token;
        this.isSpecial = isSpecial;
    }

    public String toString() {
       return token.image;
    }

    public boolean isSpecial() {
        return this.isSpecial;
    }

    public int kindID() {
        return token.kind;
    }

    public String kindName() {
        return ParserConstants.tokenImage[token.kind];
    }

    public int lineno() {
        return token.beginLine;
    }

    public int column() {
        return token.beginColumn;
    }

    public String image() {
        return token.image;
    }

}
