/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import nbm.Code;
import parser.PutStatParser;

/**
 *
 * @author peter
 */
public class PutStatParserTestSetup extends ParserTestSetup {

    public static Code getCode() {
        return code;
    }
    
    public static PutStatParser getPutIntSetup() {
        return setupTestObjects("put(5);");
    }
    
    public static PutStatParser getPutCharSetup() {
        return setupTestObjects("put('a')");
    }
    
    public static PutStatParser getPutStringSetup() {
        return setupTestObjects("put('blabla')");
    }
    
    public static PutStatParser getPutlnSetup() {
        return setupTestObjects("putln");
    }
    
    private static PutStatParser setupTestObjects(String srcLine) {
        setupInfraStructure(srcLine);
        return new PutStatParser(scanner, symListManager, code, errorHandler);
    }
}
