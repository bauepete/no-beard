/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import error.ErrorHandler;
import nbm.Code;
import parser.ExprParser;
import scanner.Scanner;
import scanner.SrcReader;
import scanner.SrcStringReader;
import symlist.Operand;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class ExprParserTestSetup {
    private static Scanner scanner;
    private static SymListManager sym;
    private static Code code;
    private static ErrorHandler errorHandler;

    public static Code getCode() {
        return code;
    }
    
    public static ExprParser getSimpleRel() {
        prepareScanner("a < b");
        return setupTestObjects();
    }
    
    private static void prepareScanner(String srcLine) {
        SrcReader sourceReader = new SrcStringReader(srcLine);
        errorHandler = new ErrorHandler(sourceReader);
        scanner = new Scanner(sourceReader, errorHandler);
    }
    
    private static ExprParser setupTestObjects() {
        code = new Code();
        sym = new SymListManager(code, scanner, errorHandler);
        sym.newUnit(25);
        scanner.nextToken();
        sym.newVar(0, SymListManager.ElementType.INT);
        sym.newVar(1, SymListManager.ElementType.INT);
        sym.newVar(2, SymListManager.ElementType.INT);
        Operand.setSymListManager(sym);
        return new ExprParser(scanner, sym, code, errorHandler);
    }
    
    public static ExprParser getAndRel() {
        prepareScanner("(a <= b) && (b == 1)");
        return setupTestObjects();
    }
    
    public static ExprParser getOrRel() {
        prepareScanner("(a != b) || (b >= 1)");
        return setupTestObjects();
    }
    
    public static ExprParser getAndOrRel() {
        prepareScanner("((a < b) && (b > 1)) || (c < 0)");
        return setupTestObjects();
    }
}
