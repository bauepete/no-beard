/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import error.ErrorHandler;
import nbm.Code;
import parser.ExprParser;
import scanner.Scanner;
import scanner.SrcStringReader;
import symlist.Operand;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class ExprParserTestSetup {
    static private Code c;

    static private SymListManager sym;
    static private Scanner scanner;
    
    public static Code getCode() {
        return c;
    }

    static public ExprParser getAddTestSetup() {
        scanner = new Scanner(new SrcStringReader("a + b"));
        return setupTestObjects();
    }
    
    static public ExprParser getSubTestSetup() {
        scanner = new Scanner(new SrcStringReader("a - b"));
        return setupTestObjects();
    }
    
    static public ExprParser getNegAddTestSetup() {
        scanner = new Scanner(new SrcStringReader("-a + b"));
        return setupTestObjects();
    }
    
    static public ExprParser getNegTestSetup() {
        scanner = new Scanner(new SrcStringReader("-b"));
        return setupTestObjects();
    }
    
    public static ExprParser getAddMulTestSetup() {
        scanner = new Scanner(new SrcStringReader("a - b * 3"));
        return setupTestObjects();
    }
    
    static public ExprParser getComplexExprTestSetup() {
        scanner = new Scanner(new SrcStringReader("-5 * (a + b)/17"));
        return setupTestObjects();
    }
    
    static public ExprParser getNoExprTestSetup() {
        scanner = new Scanner(new SrcStringReader("*b"));
        return setupTestObjects();
    }
    
    static private ExprParser setupTestObjects() {
        ErrorHandler.getInstance().reset();
        c = new Code();
        sym = new SymListManager(c, scanner);
        sym.newUnit(25);
        sym.newVar(0, SymListManager.ElementType.INT);
        sym.newVar(1, SymListManager.ElementType.INT);
        Operand.setSymListManager(sym);
        scanner.nextToken();
        return new ExprParser(scanner, sym, c);
    }
}
