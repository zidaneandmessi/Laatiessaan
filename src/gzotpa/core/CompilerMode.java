package gzotpa.core;
import java.util.Map;
import java.util.HashMap;

enum CompilerMode {
    CheckSyntax ("--check-syntax"),
    CheckSemantic ("--check-semantic"),
    GenerateIR ("--generate-ir"),
    Compile ("-S"),
    Assemble ("-c"),
    Link ("--link");

    static private Map<String, CompilerMode> modes;
    static {
        modes = new HashMap<String, CompilerMode>();
        modes.put("--check-syntax", CheckSyntax);
        modes.put("--check-semantic", CheckSemantic);
        modes.put("--generate-ir", GenerateIR);
        modes.put("-S", Compile);
        modes.put("-c", Assemble);
    }

    static public boolean isModeOption(String opt) {
        return modes.containsKey(opt);
    }

    static public CompilerMode fromOption(String opt) {
        CompilerMode m = modes.get(opt);
        if (m == null) {
            throw new Error("must not happen: unknown mode option: " + opt);
        }
        return m;
    }

    private final String option;

    CompilerMode(String option) {
        this.option = option;
    }

    public String toOption() {
        return option;
    }

    boolean requires(CompilerMode m) {
        return ordinal() >= m.ordinal();
    }
}
