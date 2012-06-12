/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import nbm.Code;
import parser.FactParser;
import scanner.Scanner;
import scanner.SrcStringReader;
import symlist.Operand;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class FactParserTestSetup {

    static private Code c;
    static private SymListManager sym;
    static private Scanner scanner;

    public static FactParser getIdentifierTestSetup() {
        scanner = new Scanner(new SrcStringReader("a25"));
        return setupTestObjects();
    }

    public static FactParser getNumberTestSetup() {
        scanner = new Scanner(new SrcStringReader("42"));
        return setupTestObjects();
    }
    
    public static FactParser getStringTestSetup() {
        scanner = new Scanner(new SrcStringReader("'blablu'"));
        return setupTestObjects();
    }
    
    public static FactParser getExprSetup() {
        scanner = new Scanner(new SrcStringReader("(a + b)"));
        return setupTestObjects();
    }
    
    public static FactParser getNoFactSetup() {
        scanner = new Scanner(new SrcStringReader("{"));
        return setupTestObjects();
    }

    private static FactParser setupTestObjects() {
        c = new Code();
        sym = new SymListManager(c, scanner);
        sym.newUnit(25);
        scanner.nextToken();
        sym.newVar(scanner.getCurrentToken().getValue(), SymListManager.ElementType.INT);
        sym.newVar(1, SymListManager.ElementType.INT);
        Operand.setSymListManager(sym);
        return new FactParser(scanner, sym, c);
    }
}
