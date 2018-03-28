static final public String ProgramName = "laatiessan";

static public void main(String[] args)
{
	new Compiler(ProgramName).commandMain(args);
}

private final ErrorHandler errorHandler;

public Compiler(String programName)
{
	this.errorHandler = new ErrorHandler(programName);
}

public void commandMain(String[] args)
{
	Options opts = Options.parse(args);
	List<SourceFile> srcs = opts.sourceFiles();
	build(srcs, opts);
}