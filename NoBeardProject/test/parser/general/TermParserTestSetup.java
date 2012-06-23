/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import error.ErrorHandler;
import error.Error;
import nbm.Code;
import parser.TermParser;
import scanner.Scanner;
import scanner.SrcStringReader;
import symlist.Operand;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class TermParserTestSetup {
    static private Code c;
    static private SymListManager sym;
    static private Scanner scanner;

    public static TermParser getMulTermSetup() {
        scanner = new Scanner(new SrcStringReader("a * b * c"));
        return setupTestObjects();
    }
    
    public static TermParser getDivTermSetup() {
        scanner = new Scanner(new SrcStringReader("1 / 2 / a"));
        return setupTestObjects();
    }
    
    public static TermParser getModTermSetup() {
        scanner = new Scanner(new SrcStringReader("10 % b % c"));
        return setupTestObjects();
    }
    
    public static TermParser getAndTermSetup() {
        scanner = new Scanner(new SrcStringReader("a && b && c"));
        return setupBoolTestObjects();
    }

    public static TermParser getNoTermSetup() {
        scanner = new Scanner(new SrcStringReader("-b"));
        return setupTestObjects();
    }

    public static Code getCode() {
        return c;
    }

    private static TermParser setupTestObjects() {
        ErrorHandler.getInstance().reset();
        Error.setScanner(scanner);
        c = new Code();
        sym = new SymListManager(c, scanner);
        sym.newUnit(25);
        scanner.nextToken();
        sym.newVar(0, SymListManager.ElementType.INT);
        sym.newVar(1, SymListManager.ElementType.INT);
        sym.newVar(2, SymListManager.ElementType.INT);
        Operand.setSymListManager(sym);
        return new TermParser(scanner, sym, c);
    }

    private static TermParser setupBoolTestObjects() {
        ErrorHandler.getInstance().reset();
        Error.setScanner(scanner);
        c = new Code();
        sym = new SymListManager(c, scanner);
        sym.newUnit(25);
        scanner.nextToken();
        sym.newVar(0, SymListManager.ElementType.BOOL);
        sym.newVar(1, SymListManager.ElementType.BOOL);
        sym.newVar(2, SymListManager.ElementType.BOOL);
        Operand.setSymListManager(sym);
        return new TermParser(scanner, sym, c);
    }
}
