/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import nbm.CodeGenerator;
import parser.ParserFactory;
import parser.PutStatParser;

/**
 *
 * @author peter
 */
public class PutStatParserTestSetup extends ParserTestSetup {

    public static CodeGenerator getCode() {
        return code;
    }
    
    public static PutStatParser getPutIntSetup() {
        return setupTestObjects("put(5);");
    }
    
    private static PutStatParser setupTestObjects(String srcLine) {
        setupInfraStructure(srcLine);
        return ParserFactory.create(PutStatParser.class);
    }

    public static PutStatParser getPutCharSetup() {
        return setupTestObjects("put('a');");
    }
    
    public static PutStatParser getPutStringSetup() {
        return setupTestObjects("put('blabla');");
    }
    
    public static PutStatParser getPutWithWidthSpecification() {
        return setupTestObjects("put(5, 7);");
    }
    
    public static PutStatParser getPutlnSetup() {
        return setupTestObjects("putln;");
    }

    public static PutStatParser getPutIntWithColumnWidthSetup() {
        return setupTestObjects("put(42, 17);");
    }

    public static PutStatParser getPutStringWithColumnWidthSetup() {
        return setupTestObjects("put('blablu', 17);");
    }
}
