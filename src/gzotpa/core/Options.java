package gzotpa.core;
import gzotpa.exception.*;
import java.util.List;
import java.util.ListIterator;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.PrintStream;

class Options {
    static Options parse(String[] args) {
        Options opts = new Options();
        opts.parseArgs(args);
        return opts;
    }

    private CompilerMode mode;
    private String outputFileName;
    private List<LdArg> ldArgs;
    private List<SourceFile> sourceFiles;
    private boolean verbose = false;

    CompilerMode mode() {
        return mode;
    }

    List<SourceFile> sourceFiles() {
        return sourceFiles;
    }

    boolean isLinkRequired() {
        return mode.requires(CompilerMode.Link);
    }

    List<String> ldArgs() {
        List<String> result = new ArrayList<String>();
        for (LdArg arg : ldArgs) {
            result.add(arg.toString());
        }
        return result;
    }

    String outputFileName() {
        return this.outputFileName;
    }

    void parseArgs(String[] origArgs) {
        sourceFiles = new ArrayList<SourceFile>();
        ldArgs = new ArrayList<LdArg>();
        ListIterator<String> args = Arrays.asList(origArgs).listIterator();
        while (args.hasNext()) {
            String arg = args.next();
            if (arg.equals("--")) {
                // "--" Stops command line processing
                break;
            }
            else if (arg.startsWith("-")) {
                if (CompilerMode.isModeOption(arg)) {
                    if (mode != null) {
                        parseError(mode.toOption() + " option and "
                                   + arg + " option is exclusive");
                    }
                    mode = CompilerMode.fromOption(arg);
                }
                else if (arg.startsWith("-o")) {
                    outputFileName = getOptArg(arg, args);
                }
                else if (arg.equals("--version")) {
                    System.out.printf("%s version %s\n",
                        Compiler.ProgramName, Compiler.Version);
                    System.exit(0);
                }
                else if (arg.equals("--help")) {
                    printUsage(System.out);
                    System.exit(0);
                }
                else if (arg.equals("-v")) {
                    verbose = true;
                }
                else {
                    parseError("unknown option: " + arg);
                }
            }
            else {
                ldArgs.add(new SourceFile(arg));
            }
        }
        // args has more arguments when "--" is appeared.
        while (args.hasNext()) {
            ldArgs.add(new SourceFile(args.next()));
        }

        if (mode == null) {
            mode = CompilerMode.Link;
        }
        sourceFiles = selectSourceFiles(ldArgs);
        if (sourceFiles.isEmpty()) {
            parseError("no input file");
        }
        for (SourceFile src : sourceFiles) {
            if (! src.isKnownFileType()) {
                parseError("unknown file type: " + src.path());
            }
        }
        if (outputFileName != null
                && sourceFiles.size() > 1
                && ! isLinkRequired()) {
            parseError("-o option requires only 1 input (except linking)");
        }
    }

    private String getOptArg(String opt, ListIterator<String> args) {
        String path = opt.substring(2);
        if (path.length() != 0) {       // -Ipath
            return path;
        }
        else {                          // -I path
            return nextArg(opt, args);
        }
    }

    private String nextArg(String opt, ListIterator<String> args) {
        if (! args.hasNext()) {
            parseError("missing argument for " + opt);
        }
        return args.next();
    }

    private List<SourceFile> selectSourceFiles(List<LdArg> args) {
        List<SourceFile> result = new ArrayList<SourceFile>();
        for (LdArg arg : args) {
            if (arg.isSourceFile()) {
                result.add((SourceFile)arg);
            }
        }
        return result;
    }
    
    private void parseError(String msg) {
        throw new OptionParseError(msg);
    }

    boolean isVerboseMode() {
        return this.verbose;
    }

    void printUsage(PrintStream out) {
        out.println("Usage: laatiessaan [options] file...");
        out.println("Global Options:");
        out.println("  --check-syntax   Checks syntax and quit.");
        out.println("  --dump-tokens    Dumps tokens and quit.");
        // --dump-stmt is a hidden option.
        // --dump-expr is a hidden option.
        out.println("  --dump-ast       Dumps AST and quit.");
        out.println("  --dump-semantic  Dumps AST after semantic checks and quit.");
        // --dump-reference is a hidden option.
        out.println("  --dump-ir        Dumps IR and quit.");
        out.println("  --dump-asm       Dumps AssemblyCode and quit.");
        out.println("  --print-asm      Prints assembly code and quit.");
        out.println("  -S               Generates an assembly file and quit.");
        out.println("  -c               Generates an object file and quit.");
        out.println("  -o PATH          Places output in file PATH.");
        out.println("  -v               Turn on verbose mode.");
        out.println("  --version        Shows compiler version and quit.");
        out.println("  --help           Prints this message and quit.");
        out.println("");
        out.println("Optimization Options:");
        out.println("  -O               Enables optimization.");
        out.println("  -O1, -O2, -O3    Equivalent to -O.");
        out.println("  -Os              Equivalent to -O.");
        out.println("  -O0              Disables optimization (default).");
        out.println("");
        out.println("Parser Options:");
        out.println("  -I PATH          Adds PATH as import file directory.");
        out.println("  --debug-parser   Dumps parsing process.");
        out.println("");
        out.println("Code Generator Options:");
        out.println("  -O               Enables optimization.");
        out.println("  -O1, -O2, -O3    Equivalent to -O.");
        out.println("  -Os              Equivalent to -O.");
        out.println("  -O0              Disables optimization (default).");
        out.println("  -fPIC            Generates PIC assembly.");
        out.println("  -fpic            Equivalent to -fPIC.");
        out.println("  -fPIE            Generates PIE assembly.");
        out.println("  -fpie            Equivalent to -fPIE.");
        out.println("  -fverbose-asm    Generate assembly with verbose comments.");
        out.println("");
        out.println("Assembler Options:");
        out.println("  -Wa,OPT          Passes OPT to the assembler (as).");
        out.println("  -Xassembler OPT  Passes OPT to the assembler (as).");
        out.println("");
        out.println("Linker Options:");
        out.println("  -l LIB           Links the library LIB.");
        out.println("  -L PATH          Adds PATH as library directory.");
        out.println("  -shared          Generates shared library rather than executable.");
        out.println("  -static          Linkes only with static libraries.");
        out.println("  -pie             Generates PIE.");
        out.println("  --readonly-got   Generates read-only GOT (ld -z combreloc -z now -z relro).");
        out.println("  -nostartfiles    Do not link startup files.");
        out.println("  -nodefaultlibs   Do not link default libraries.");
        out.println("  -nostdlib        Enables -nostartfiles and -nodefaultlibs.");
        out.println("  -Wl,OPT          Passes OPT to the linker (ld).");
        out.println("  -Xlinker OPT     Passes OPT to the linker (ld).");
    }
}
