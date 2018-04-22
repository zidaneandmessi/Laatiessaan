package gzotpa.compiler;
import gzotpa.parser.Parser;
import gzotpa.ast.AST;
import gzotpa.ast.StmtNode;
import gzotpa.ast.ExprNode;
import gzotpa.exception.*;
import java.util.*;
import java.io.*;

public class Compiler {
    static final public String ProgramName = "laatiessaan";

    static public void main(String[] args) {
        new Compiler(ProgramName).commandMain(args);
    }

    public void commandMain(String[] args) {
        Options opts = parseOptions(args);
        if (opts.mode() == CompilerMode.CheckSyntax) {
            System.exit(checkSyntax(opts) ? 0 : 1);
        }
    }
    private Options parseOptions(String[] args) {
        return Options.parse(args);
    }

    private boolean checkSyntax(Options opts) {
        boolean failed = false;
        for (SourceFile src : opts.sourceFiles()) {
            if (isValidSyntax(src.path(), opts)) {
                System.out.println(src.path() + ": Syntax OK");
            }
            else {
                System.out.println(src.path() + ": Syntax Error");
                failed = true;
            }
        }
        return !failed;
    }

    private boolean isValidSyntax(String path, Options opts) {
        try {
            //parseFile(path, opts);
            return true;
        }
        catch (SyntaxException ex) {
            return false;
        }
        catch (FileException ex) {
            return false;
        }
    }
}
