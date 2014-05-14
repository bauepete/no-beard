/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import error.ErrorHandler;
import error.Error;
import nbm.Code;
import parser.SimExprParser;
import scanner.Scanner;
import scanner.SrcStringReader;
import symlist.Operand;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class SimExprParserTestSetup extends ParserTestSetup {

    static private Code c;

    public static Code getCode() {
        return c;
    }

    static public SimExprParser getAddTestSetup() {
        return setupTestObjects("a + b");
    }
    
    static public SimExprParser getSubTestSetup() {
        return setupTestObjects("a - b");
    }

    static public SimExprParser getNegAddTestSetup() {
        return setupTestObjects("-a + b");
    }

    static public SimExprParser getNegTestSetup() {
        return setupTestObjects("-b");
    }

    public static SimExprParser getAddMulTestSetup() {
        return setupTestObjects("a - b * 3");
    }

    static public SimExprParser getComplexExprTestSetup() {
        return setupTestObjects("-5 * (a + b)/17");
    }

    static public SimExprParser getNoExprTestSetup() {
        return setupTestObjects("*b");
    }

    static public SimExprParser getOrExprTestSetup() {
        return setupBoolTestObjects("a || b || c");
    }

    static private SimExprParser setupTestObjects(String srcLine) {
        setupScanner(srcLine);
        c = new Code();
        sym = new SymListManager(c, scanner, errorHandler);
        sym.newUnit(25);
        sym.newVar(0, SymListManager.ElementType.INT);
        sym.newVar(1, SymListManager.ElementType.INT);
        Operand.setSymListManager(sym);
        scanner.nextToken();
        return new SimExprParser(scanner, sym, c, errorHandler);
    }

    static private SimExprParser setupBoolTestObjects(String srcLine) {
        setupScanner(srcLine);
        c = new Code();
        sym = new SymListManager(c, scanner, errorHandler);
        sym.newUnit(25);
        sym.newVar(0, SymListManager.ElementType.BOOL);
        sym.newVar(1, SymListManager.ElementType.BOOL);
        sym.newVar(2, SymListManager.ElementType.BOOL);
        Operand.setSymListManager(sym);
        scanner.nextToken();
        return new SimExprParser(scanner, sym, c, errorHandler);
    }
}
