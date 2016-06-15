/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import parser.SimExprParser;
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
        setupInfraStructureOld(srcLine);
        ParserTestSetup.fillSymList(SymListManager.ElementType.INT);
        return new SimExprParser(scanner, symListManager, code, errorHandler);
    }

    static private SimExprParser setupBoolTestObjects(String srcLine) {
        setupInfraStructureOld(srcLine);
        fillSymList(SymListManager.ElementType.BOOL);
        return new SimExprParser(scanner, symListManager, code, errorHandler);
    }
}
