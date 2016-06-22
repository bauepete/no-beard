/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import parser.ExpressionParser;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class ExpressionParserTestSetup extends ParserTestSetup {
    
    public static ExpressionParser getSimpleRel() {
        return setupTestObjects("a < b");
    }
        
    private static ExpressionParser setupTestObjects(String srcLine) {
        setupInfraStructureOld(srcLine);
        fillSymList(SymListManager.ElementType.INT);
        return new ExpressionParser(scanner, symListManager, code, errorHandler);
    }
    
    public static ExpressionParser getAndRel() {
        return setupTestObjects("(a <= b) && (b == 1)");
    }
    
    public static ExpressionParser getOrRel() {
        return setupTestObjects("(a != b) || (b >= 1)");
    }
    
    public static ExpressionParser getAndOrRel() {
        return setupTestObjects("((a < b) && (b > 1)) || (c < 0)");
    }
}
