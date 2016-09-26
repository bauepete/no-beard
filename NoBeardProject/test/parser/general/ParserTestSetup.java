/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import error.ErrorHandler;
import machine.CodeGenerator;
import parser.ParserFactory;
import scanner.Scanner;
import io.SourceReader;
import io.SourceStringReader;
import scanner.NameManagerForCompiler;
import symboltable.SymbolTable;

/**
 *
 * @author peter
 */
public class ParserTestSetup {

    protected static Scanner scanner;
    protected static SymbolTable symListManager;
    protected static CodeGenerator code;
    protected static ErrorHandler errorHandler;

    public static byte[] getByteCode() {
        return code.getByteCode();
    }

    public static Scanner getScanner() {
        return scanner;
    }
    
    public static SymbolTable getSymListManager() {
        return symListManager;
    }

    protected static void setupInfraStructure(String sourceLine) {
        SourceReader sourceReader = new SourceStringReader(sourceLine);
        errorHandler = new ErrorHandler(sourceReader);
        scanner = new Scanner(sourceReader, errorHandler, new NameManagerForCompiler(sourceReader));
        code = new CodeGenerator(256);
        symListManager = new SymbolTable(scanner, errorHandler);

        ParserFactory.setup(sourceReader, errorHandler, scanner, code, symListManager);
    }

    protected static void fillSymList(SymbolTable.ElementType type) {
        symListManager.newUnit(25);
        symListManager.newVar(0, type);
        symListManager.newVar(1, type);
        symListManager.newVar(2, type);

    }
}
