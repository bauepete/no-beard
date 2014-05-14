/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

import error.ErrorHandler;
import nbm.Code;
import parser.NoBeardParser;
import scanner.Scanner;
import scanner.SrcReader;
import symlist.Operand;
import symlist.SymListManager;
/**
 *
 * @author peter
 */
public class NbCompiler {
    private final  Scanner scanner;
    private final  NoBeardParser parser;
    private final  SymListManager sym;
    private final  Code code;
    private final ErrorHandler errorHandler;
    
    public NbCompiler(SrcReader srcReader) {
        
        errorHandler = new ErrorHandler(srcReader);
        scanner = new Scanner(srcReader, errorHandler);
        code = new Code();
        sym = new SymListManager(code, scanner, errorHandler);
        Operand.setSymListManager(sym);
        Operand.setStringManager(scanner.getStringManager());
        parser = new NoBeardParser(scanner, sym, code, errorHandler);
        scanner.nextToken();
    }

    public Code getCode() {
        return code;
    }

    public NoBeardParser getParser() {
        return parser;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public SymListManager getSymListManager() {
        return sym;
    }
    
    public byte[] getStringStorage() {
        return scanner.getStringManager().getStringStorage();
    }
    
    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }
    
}
