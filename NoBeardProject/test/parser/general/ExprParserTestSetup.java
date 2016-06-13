/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import parser.ExprParser;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class ExprParserTestSetup extends ParserTestSetup {
    
    public static ExprParser getSimpleRel() {
        return setupTestObjects("a < b");
    }
        
    private static ExprParser setupTestObjects(String srcLine) {
        setupInfraStructure(srcLine);
        fillSymList(SymListManager.ElementType.INT);
        return new ExprParser(scanner, symListManager, code, errorHandler);
    }
    
    public static ExprParser getAndRel() {
        return setupTestObjects("(a <= b) && (b == 1)");
    }
    
    public static ExprParser getOrRel() {
        return setupTestObjects("(a != b) || (b >= 1)");
    }
    
    public static ExprParser getAndOrRel() {
        return setupTestObjects("((a < b) && (b > 1)) || (c < 0)");
    }
}
