/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import parser.ParserFactory;
import parser.AddExpressionParser;
import symboltable.SymbolTable;

/**
 *
 * @author peter
 */
public class SimpleExpressionParserTestSetup extends ParserTestSetup {

    static public AddExpressionParser getAddTestSetup() {
        return setupTestObjects("a + b");
    }
    
    static public AddExpressionParser getSubTestSetup() {
        return setupTestObjects("a - b");
    }

    static public AddExpressionParser getNegAddTestSetup() {
        return setupTestObjects("-a + b");
    }

    static public AddExpressionParser getNegTestSetup() {
        return setupTestObjects("-b");
    }

    public static AddExpressionParser getAddMulTestSetup() {
        return setupTestObjects("a - b * 3");
    }

    static public AddExpressionParser getComplexExprTestSetup() {
        return setupTestObjects("-5 * (a + b)/17");
    }

    static public AddExpressionParser getNoExprTestSetup() {
        return setupTestObjects("*b");
    }

    static public AddExpressionParser getOrExprTestSetup() {
        return setupBoolTestObjects("a || b || c");
    }

    static private AddExpressionParser setupTestObjects(String srcLine) {
        setupInfraStructure(srcLine);
        ParserTestSetup.fillSymList(SymbolTable.ElementType.INT);
        return ParserFactory.create(AddExpressionParser.class);
    }

    static private AddExpressionParser setupBoolTestObjects(String srcLine) {
        setupInfraStructure(srcLine);
        fillSymList(SymbolTable.ElementType.BOOL);
        return ParserFactory.create(AddExpressionParser.class);
    }

    public static AddExpressionParser getMixedOperandSetup() {
        return setupBoolTestObjects("5 / a");
    }
}
