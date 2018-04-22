package gzotpa.compiler;
import gzotpa.parser.LibraryLoader;
import gzotpa.type.TypeTable;
import gzotpa.exception.*;
import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.PrintStream;

class Options {
    static Options parse(String[] args) {
        Options opts = new Options();
        opts.parseArgs(args);
        return opts;
    }

    private CompilerMode mode;
    private String outputFileName;
    private List<SourceFile> sourceFiles;

    CompilerMode mode() {
        return mode;
    }

    List<SourceFile> sourceFiles() {
        return sourceFiles;
    }

    String outputFileName() {
        return this.outputFileName;
    }
}
