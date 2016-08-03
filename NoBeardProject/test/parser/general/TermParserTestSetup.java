/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import parser.ParserFactory;
import parser.TermParser;
import symboltable.SymbolTable;

/**
 *
 * @author peter
 */
public class TermParserTestSetup extends ParserTestSetup {

    public static TermParser getMulTermSetup() {
        return setupTestObjectsAndParser("a * b * c");
    }
    
    public static TermParser getDivTermSetup() {
        return setupTestObjectsAndParser("1 / 2 / a");
    }
    
    public static TermParser getModTermSetup() {
        return setupTestObjectsAndParser("10 % b % c");
    }
    
    public static TermParser getAndTermSetup() {
        return setupBoolTestObjects("a && b && c");
    }

    public static TermParser getNoTermSetup() {
        return setupTestObjectsAndParser("-b");
    }

    public static TermParser getTypeMismatchSetup() {
        return setupBoolTestObjects("a && 10");
    }

    private static TermParser setupTestObjectsAndParser(String srcLine) {
        setupInfraStructure(srcLine);
        ParserTestSetup.fillSymList(SymbolTable.ElementType.INT);
        return ParserFactory.create(TermParser.class);
    }

    private static TermParser setupBoolTestObjects(String srcLine) {
        setupInfraStructure(srcLine);
        fillSymList(SymbolTable.ElementType.BOOL);
        return ParserFactory.create(TermParser.class);
    }
}
