package gzotpa.ast;
import gzotpa.parser.Token;

public class Location {
    protected String sourceName;
    protected MxToken token;

    public Location(String sourceName, Token token) {
        this(sourceName, new MxToken(token));
    }

    public Location(String sourceName, MxToken token) {
        this.sourceName = sourceName;
        this.token = token;
    }

    public String sourceName() {
        return sourceName;
    }

    public MxToken token() {
        return token;
    }
}
