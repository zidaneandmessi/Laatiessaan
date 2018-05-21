package gzotpa.code.nasm;
import gzotpa.asm.*;
import gzotpa.ast.Location;
import gzotpa.entity.*;
import gzotpa.ir.*;
import java.util.*;

public class CodeGenerator {
    public CodeGenerator() {}
    
    public AssemblyCode generate(IR ir) {
        //locateSymbols(ir);
        return generateAssemblyCode(ir);
    }
    
    /*private void locateSymbols(IR ir) {
        SymbolTable constSymbols = new SymbolTable(CONST_SYMBOL_BASE);
        for (ConstantEntry ent : ir.constantTable().entries()) {
            locateStringLiteral(ent, constSymbols);
        }
        for (Variable var : ir.allGlobalVariables()) {
            locateGlobalVariable(var);
        }
        for (Function func : ir.allFunctions()) {
            locateFunction(func);
        }
    }*/

    private AssemblyCode generateAssemblyCode(IR ir) {
        AssemblyCode code = new AssemblyCode();
        for (DefinedFunction func : ir.defuns()) {
            code.global(new Label(func.name()));
        }
        generateTextSection(code, ir.defuns());
        return code;
    }

    private void generateTextSection(AssemblyCode code, List<DefinedFunction> funcs) {
        code.section("text");
        for (DefinedFunction func : funcs) 
        {
            code.label(new Label(func.name()));
            code.leave();
            code.ret();
        }

    }
}