/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import machine.CodeGenerator;
import parser.ParserFactory;
import parser.PutParser;
import symboltable.SymbolTable;

/**
 *
 * @author peter
 */
public class PutParserTestSetup extends ParserTestSetup {

    public static CodeGenerator getCode() {
        return code;
    }
    
    public static PutParser getPutIntSetup() {
        return setupTestObjects("put(5);");
    }
    
    private static PutParser setupTestObjects(String srcLine) {
        setupInfraStructure(srcLine);
        return ParserFactory.create(PutParser.class);
    }

    public static PutParser getPutCharSetup() {
        return setupTestObjects("put('a');");
    }
    
    public static PutParser getPutStringSetup() {
        return setupTestObjects("put('blabla');");
    }
    
    public static PutParser getPutWithWidthSpecification() {
        return setupTestObjects("put(5, 7);");
    }
    
    public static PutParser getPutlnSetup() {
        return setupTestObjects("putln;");
    }

    public static PutParser getPutIntWithColumnWidthSetup() {
        return setupTestObjects("put(42, 17);");
    }

    public static PutParser getPutStringWithColumnWidthSetup() {
        return setupTestObjects("put('blablu', 17);");
    }

    public static PutParser getPutWithUndefinedIdentifier() {
        return setupTestObjects("put(x);");
    }

    public static PutParser getPutWithNonPuttableIdentifier() {
        PutParser p = setupTestObjects("put(x);");
        fillSymList(SymbolTable.ElementType.BOOL);
        return p;
    }
}
