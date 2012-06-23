/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import error.ErrorHandler;
import error.Error;
import nbm.Code;
import scanner.Scanner;
import symlist.Operand;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class ParserTestSetup {
    protected static Scanner scanner;
    protected static SymListManager sym;
    protected static Code code;
    
    public static byte[] byteCode() {
        return code.getByteCode();
    }
    
    protected static void setupTestObjects() {
        ErrorHandler.getInstance().reset();
        Error.setScanner(scanner);
        code = new Code();
        sym = new SymListManager(code, scanner);
        sym.newUnit(25);
        scanner.nextToken();
        sym.newVar(0, SymListManager.ElementType.INT);
        sym.newVar(1, SymListManager.ElementType.INT);
        Operand.setSymListManager(sym);
        Operand.setStringManager(scanner.getStringManager());
    }
}
