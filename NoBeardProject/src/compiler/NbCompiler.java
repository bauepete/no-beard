/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

import error.ErrorHandler;
import nbm.CodeGenerator;
import parser.NoBeardParser;
import scanner.Scanner;
import scanner.SrcReader;
import symboltable.Operand;
import symboltable.SymbolTable;
/**
 *
 * @author peter
 */
public class NbCompiler {
    private final  Scanner scanner;
    private final  NoBeardParser parser;
    private final  SymbolTable sym;
    private final  CodeGenerator code;
    private final ErrorHandler errorHandler;
    
    public NbCompiler(SrcReader srcReader) {
        
        errorHandler = new ErrorHandler(srcReader);
        scanner = new Scanner(srcReader, errorHandler);
        code = new CodeGenerator(nbm.Nbm.getMAXPROG());
        sym = new SymbolTable(code, scanner, errorHandler);
        Operand.setSymListManager(sym);
        Operand.setStringManager(scanner.getStringManager());
        parser = new NoBeardParser(scanner, sym, code, errorHandler);
        scanner.nextToken();
    }

    public CodeGenerator getCode() {
        return code;
    }

    public NoBeardParser getParser() {
        return parser;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public SymbolTable getSymListManager() {
        return sym;
    }
    
    public byte[] getStringStorage() {
        return scanner.getStringManager().getStringStorage();
    }
    
    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }
    
}
