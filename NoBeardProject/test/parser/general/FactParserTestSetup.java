/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import nbm.Code;
import parser.FactParser;
import scanner.Scanner;
import scanner.SrcStringReader;

/**
 *
 * @author peter
 */
public class FactParserTestSetup extends ParserTestSetup {    

    public static FactParser getIdentifierTestSetup() {
        scanner = new Scanner(new SrcStringReader("a25"));
        return setupTestObjectsAndParser();
    }

    public static FactParser getNumberTestSetup() {
        scanner = new Scanner(new SrcStringReader("42"));
        return setupTestObjectsAndParser();
    }

    public static FactParser getStringTestSetup() {
        scanner = new Scanner(new SrcStringReader("'blablu'"));
        return setupTestObjectsAndParser();
    }

    public static FactParser getExprSetup() {
        scanner = new Scanner(new SrcStringReader("(a + b)"));
        return setupTestObjectsAndParser();
    }

    public static FactParser getNoFactSetup() {
        scanner = new Scanner(new SrcStringReader("{"));
        return setupTestObjectsAndParser();
    }

    private static FactParser setupTestObjectsAndParser() {
        setupTestObjects();
        return new FactParser(scanner, sym, code);
    }
}
