/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import error.ErrorHandler;
import nbm.Code;
import parser.PutStatParser;
import scanner.Scanner;
import scanner.SrcReader;
import scanner.SrcStringReader;
import symlist.Operand;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class PutStatParserTestSetup {

    private static Scanner scanner;
    private static ErrorHandler errorHandler;
    private static Code c;
    private static SymListManager sl;

    public static Code getCode() {
        return c;
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
        SrcReader sourceReader = new SrcStringReader(srcLine);
        errorHandler = new ErrorHandler(sourceReader);
        scanner = new Scanner(sourceReader, errorHandler);
        scanner.nextToken();
        
        c = new Code();
        sl = new SymListManager(c, scanner, errorHandler);
        Operand.setStringManager(scanner.getStringManager());
        return new PutStatParser(scanner, sl, c, errorHandler);
    }
}
