/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import nbm.Code;
import parser.PutStatParser;
import scanner.Scanner;
import scanner.SrcStringReader;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class PutStatParserTestSetup {

    private static Scanner scanner;
    private static Code c;
    private static SymListManager sl;

    public static Code getCode() {
        return c;
    }
    
    public static PutStatParser getPutIntSetup() {
        scanner = new Scanner(new SrcStringReader("put(5);"));
        return setupTestObjects();
    }
    
    public static PutStatParser getPutStringSetup() {
        scanner = new Scanner(new SrcStringReader("put('blabla')"));
        return setupTestObjects();
    }
    
    public static PutStatParser getPutlnSetup() {
        scanner = new Scanner(new SrcStringReader("putln"));
        return setupTestObjects();
    }
    
    private static PutStatParser setupTestObjects() {
        scanner.nextToken();
        c = new Code();
        sl = new SymListManager(c, scanner);
        return new PutStatParser(scanner, sl, c);
    }
}
