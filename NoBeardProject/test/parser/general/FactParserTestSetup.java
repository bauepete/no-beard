/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import parser.FactParser;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class FactParserTestSetup extends ParserTestSetup {

    public static FactParser getIdentifierTestSetup() {
        return setupTestObjectsAndParser("a25");
    }

    private static FactParser setupTestObjectsAndParser(String srcLine) {
        setupInfraStructure(srcLine);
        fillSymList(SymListManager.ElementType.INT);
        return new FactParser(scanner, symListManager, code, errorHandler);
    }

    public static FactParser getNumberTestSetup() {
        return setupTestObjectsAndParser("42");
    }

    public static FactParser getStringTestSetup() {
        return setupTestObjectsAndParser("'blablu'");
    }

    public static FactParser getExprSetup() {
        return setupTestObjectsAndParser("(a + b)");
    }

    public static FactParser getNoFactSetup() {
        return setupTestObjectsAndParser("{");
    }
}
