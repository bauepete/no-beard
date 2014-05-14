/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import nbm.Code;
import parser.SimExprParser;
import symlist.Operand;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class SimExprParserTestSetup extends ParserTestSetup {

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
        // TODO: rearrange lines, move similarities into setupScanner
        setupScanner(srcLine);
        code = new Code(256);
        sym = new SymListManager(code, scanner, errorHandler);
        sym.newUnit(25);
        sym.newVar(0, SymListManager.ElementType.INT);
        sym.newVar(1, SymListManager.ElementType.INT);
        Operand.setSymListManager(sym);
        scanner.nextToken();
        return new SimExprParser(scanner, sym, code, errorHandler);
    }

    static private SimExprParser setupBoolTestObjects(String srcLine) {
        // TODO: rearrange lines, move similarities into setupScanner
        setupScanner(srcLine);
        code = new Code(256);
        sym = new SymListManager(code, scanner, errorHandler);
        sym.newUnit(25);
        sym.newVar(0, SymListManager.ElementType.BOOL);
        sym.newVar(1, SymListManager.ElementType.BOOL);
        sym.newVar(2, SymListManager.ElementType.BOOL);
        Operand.setSymListManager(sym);
        scanner.nextToken();
        return new SimExprParser(scanner, sym, code, errorHandler);
    }
}
