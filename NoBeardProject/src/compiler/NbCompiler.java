/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

import error.ErrorHandler;
import error.Error;
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
    private  Scanner scanner;
    private  NoBeardParser parser;
    private  SymListManager sym;
    private  Code code;
    
    public NbCompiler(SrcReader srcReader) {
        
        scanner = new Scanner(srcReader);
        code = new Code();
        sym = new SymListManager(code, scanner);
        Operand.setSymListManager(sym);
        Operand.setStringManager(scanner.getStringManager());
        parser = new NoBeardParser(scanner, sym, code);
        ErrorHandler.getInstance().reset();
        Error.setScanner(scanner);
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
    
}
