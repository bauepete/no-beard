/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import parser.IfStatParser;
import scanner.Scanner;
import scanner.SrcStringReader;

/**
 *
 * @author peter
 */
public class IfStatParserTestSetup extends ParserTestSetup {
    
    public static IfStatParser getSimpleIfTestSetup() {
        scanner = new Scanner(new SrcStringReader("if x == 0 do put('0'); done"));
        return setupTestObjectsAndParser();
    }
    
    public static IfStatParser getIfElseTestSetup() {
        scanner = new Scanner(new SrcStringReader("if x == 0 do put('0'); done else do put('1'); done"));
        return setupTestObjectsAndParser();
    }
    
    private static IfStatParser setupTestObjectsAndParser() {
        setupTestObjects();
        return new IfStatParser(scanner, sym, code);
    }
}
