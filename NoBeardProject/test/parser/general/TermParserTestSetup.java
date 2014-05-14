/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import parser.TermParser;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class TermParserTestSetup extends ParserTestSetup {

    public static TermParser getMulTermSetup() {
        return setupTestObjects("a * b * c");
    }
    
    public static TermParser getDivTermSetup() {
        return setupTestObjects("1 / 2 / a");
    }
    
    public static TermParser getModTermSetup() {
        return setupTestObjects("10 % b % c");
    }
    
    public static TermParser getAndTermSetup() {
        return setupBoolTestObjects("a && b && c");
    }

    public static TermParser getNoTermSetup() {
        return setupTestObjects("-b");
    }

    private static TermParser setupTestObjects(String srcLine) {
        setupInfraStructure(srcLine);
        ParserTestSetup.fillSymList(SymListManager.ElementType.INT);
        return new TermParser(scanner, symListManager, code, errorHandler);
    }

    private static TermParser setupBoolTestObjects(String srcLine) {
        setupInfraStructure(srcLine);
        fillSymList(SymListManager.ElementType.BOOL);
        return new TermParser(scanner, symListManager, code, errorHandler);
    }
}
