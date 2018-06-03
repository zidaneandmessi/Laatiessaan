package gzotpa.core;
import gzotpa.parser.Parser;
import gzotpa.ast.AST;
import gzotpa.ast.StmtNode;
import gzotpa.ast.ExprNode;
import gzotpa.code.nasm.*;
import gzotpa.exception.*;
import gzotpa.ir.IR;
import gzotpa.type.TypeTable;
import java.util.*;
import java.io.*;

public class Compiler {
    static final public String ProgramName = "laatiessaan";
    static final public String Version = "0.0.2";

    static public void main(String[] args) {
        Compiler comp = new Compiler(ProgramName);
        comp.commandMain(args);
    }

    public Compiler(String programName) {}
    
    public void commandMain(String[] args) {
        Options opts = parseOptions(args);
        if (opts.mode() == CompilerMode.CheckSyntax) {
            System.exit(checkSyntax(opts) ? 0 : 1);
        }
        else if (opts.mode() == CompilerMode.CheckSemantic) {
            System.exit(checkSemantic(opts) ? 0 : 1);
        }
        else if (opts.mode() == CompilerMode.GenerateIR) {
            System.exit(generateIR(opts) ? 0 : 1);
        }
        else if (opts.mode() == CompilerMode.Assemble) {
            System.exit(Assemble(opts) ? 0 : 1);
        }
    }
    private Options parseOptions(String[] args) {
        return Options.parse(args);
    }

    private boolean checkSyntax(Options opts) {
        boolean failed = false;
        for (SourceFile src : opts.sourceFiles()) {
            if (isValidSyntax(src.path(), opts)) {
                System.err.println(src.path() + ": Syntax OK");
            }
            else {
                System.err.println(src.path() + ": Syntax Error");
                failed = true;
            }
        }
        return !failed;
    }

    private boolean checkSemantic(Options opts) {
        boolean failed = false;
        for (SourceFile src : opts.sourceFiles()) {
            if (isValidSemantic(src.path(), opts)) {
                System.err.println(src.path() + ": Semantic OK");
            }
            else {
                System.err.println(src.path() + ": Semantic Error");
                failed = true;
            }
        }
        return !failed;
    }

    private boolean generateIR(Options opts) {
        boolean failed = false;
        for (SourceFile src : opts.sourceFiles()) {
            if (tryGenerateIR(src.path(), opts)) {
                System.err.println(src.path() + ": Generate IR OK");
            }
            else {
                System.err.println(src.path() + ": Generate IR Error");
                failed = true;
            }
        }
        return !failed;
    }

    private boolean Assemble(Options opts) {
        boolean failed = false;
        for (SourceFile src : opts.sourceFiles()) {
            if (tryAssemble(src.path(), opts)) {
                System.err.println(src.path() + ": Assemble OK");
            }
            else {
                System.err.println(src.path() + ": Assemble Error");
                failed = true;
            }
        }
        return !failed;
    }

    public AST parseFile(String path)
                            throws SyntaxException, FileException {
        return Parser.parseFile(new File(path));
    }

    private boolean isValidSyntax(String path, Options opts) {
        try {
            parseFile(path);
            return true;
        }
        catch (SyntaxException ex) {
            return false;
        }
        catch (FileException ex) {
            return false;
        }
    }

    private boolean isValidSemantic(String path, Options opts) {
        try {
            AST ast = parseFile(path);
            TypeTable types = new TypeTable();
            types.addKnownedTypes();
            new TypeResolver(types).resolve(ast);
            types.semanticCheck();
            new LocalResolver().resolve(ast);
            new DereferenceChecker(types).check(ast);
            new TypeChecker(types).check(ast);
            return true;
        }
        catch (SemanticException ex) {
            return false;
        }
        catch (SyntaxException ex) {
            return false;
        }
        catch (FileException ex) {
            return false;
        }
        catch (Error e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    private boolean tryGenerateIR(String path, Options opts) {
        try {
            AST ast = parseFile(path);
            TypeTable types = new TypeTable();
            types.addKnownedTypes();
            new TypeResolver(types).resolve(ast);
            types.semanticCheck();
            new LocalResolver().resolve(ast);
            new DereferenceChecker(types).check(ast);
            new TypeChecker(types).check(ast);
            IR ir = new IRGenerator(types).generate(ast);
            ir.dump();
            return true;
        }
        catch (SemanticException ex) {
            return false;
        }
        catch (SyntaxException ex) {
            return false;
        }
        catch (FileException ex) {
            return false;
        }
        catch (Error e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    private boolean tryAssemble(String path, Options opts) {
        try {
            AST ast = parseFile(path);
            TypeTable types = new TypeTable();
            types.addKnownedTypes();
            new TypeResolver(types).resolve(ast);
            types.semanticCheck();
            new LocalResolver().resolve(ast);
            new DereferenceChecker(types).check(ast);
            new TypeChecker(types).check(ast);
            IR ir = new IRGenerator(types).generate(ast);
            AssemblyCode code = new CodeGenerator(types).generateAssemblyCode(ir);
            if (opts.isVerboseMode()) code.dump();
            code.print();
            File file = new File("BuiltinFunction.asm");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String s;
            while ((s = reader.readLine()) != null) {  
                System.out.println(s);
            }
            reader.close();
            return true;
        }
        catch (SemanticException ex) {
            return false;
        }
        catch (SyntaxException ex) {
            return false;
        }
        catch (FileException ex) {
            return false;
        }
        catch (FileNotFoundException ex) {  
            return false;  
        }
        catch (IOException ex) {  
            return false;  
        }
        catch (Error e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
