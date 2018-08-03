/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import parser.IfParser;
import parser.ParserFactory;
import symboltable.SymbolTable;

/**
 *
 * @author peter
 */
public class IfParserTestSetup extends ParserTestSetup {
    
    public static IfParser getSimpleIfTestSetup() {
        return setupTestObjectsAndParser("if x == 0 do put('0'); done");
    }
    
    public static IfParser getIfElseTestSetup() {
        return setupTestObjectsAndParser("if x == 0 do put('0'); done else do put('1'); done");
    }
    
    private static IfParser setupTestObjectsAndParser(String srcLine) {
        setupInfraStructure(srcLine);
        fillSymList(SymbolTable.ElementType.INT);
        return ParserFactory.create(IfParser.class);
    }

    public static IfParser getIfWithBadConditionTestSetup() {
        return setupTestObjectsAndParser("if x do put('nope'); done");
    }
}
