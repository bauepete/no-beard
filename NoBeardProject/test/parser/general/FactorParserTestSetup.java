/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import parser.FactorParser;
import parser.ParserFactory;
import symboltable.SymbolTable;

/**
 *
 * @author peter
 */
public class FactorParserTestSetup extends ParserTestSetup {

    public static FactorParser getIdentifierTestSetup() {
        return setupTestObjectsAndParser("a25");
    }

    private static FactorParser setupTestObjectsAndParser(String srcLine) {
        setupInfraStructure(srcLine);
        fillSymList(SymbolTable.ElementType.INT);
        return ParserFactory.create(FactorParser.class);
    }

    public static FactorParser getNumberTestSetup() {
        return setupTestObjectsAndParser("42");
    }

    public static FactorParser getStringTestSetup() {
        return setupTestObjectsAndParser("'blablu'");
    }

    public static FactorParser getExprSetup() {
        return setupTestObjectsAndParser("(a + b)");
    }

    public static FactorParser getNoFactSetup() {
        return setupTestObjectsAndParser("{");
    }

    public static FactorParser getSingleCharTestSetup() {
        return setupTestObjectsAndParser("'c'");
    }
}
