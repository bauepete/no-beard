/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import parser.IfParser;
import symboltable.SymListManager;

/**
 *
 * @author peter
 */
public class IfStatParserTestSetup extends ParserTestSetup {
    
    public static IfParser getSimpleIfTestSetup() {
        return setupTestObjectsAndParser("if x == 0 do put('0'); done");
    }
    
    public static IfParser getIfElseTestSetup() {
        return setupTestObjectsAndParser("if x == 0 do put('0'); done else do put('1'); done");
    }
    
    private static IfParser setupTestObjectsAndParser(String srcLine) {
        setupInfraStructureOld(srcLine);
        fillSymList(SymListManager.ElementType.INT);
        return new IfParser(scanner, symListManager, code, errorHandler);
    }
}
