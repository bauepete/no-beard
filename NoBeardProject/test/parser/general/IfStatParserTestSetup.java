/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import parser.IfStatParser;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class IfStatParserTestSetup extends ParserTestSetup {
    
    public static IfStatParser getSimpleIfTestSetup() {
        return setupTestObjectsAndParser("if x == 0 do put('0'); done");
    }
    
    public static IfStatParser getIfElseTestSetup() {
        return setupTestObjectsAndParser("if x == 0 do put('0'); done else do put('1'); done");
    }
    
    private static IfStatParser setupTestObjectsAndParser(String srcLine) {
        setupInfraStructureOld(srcLine);
        fillSymList(SymListManager.ElementType.INT);
        return new IfStatParser(scanner, symListManager, code, errorHandler);
    }
}
