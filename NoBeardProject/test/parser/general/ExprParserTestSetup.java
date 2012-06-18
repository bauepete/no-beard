/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import nbm.Code;
import parser.ExprParser;
import scanner.Scanner;
import scanner.SrcStringReader;
import symlist.Operand;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class ExprParserTestSetup {
    private static Scanner scanner;
    private static SymListManager sym;
    private static Code code;

    public static Code getCode() {
        return code;
    }
    
    public static ExprParser getSimpleRel() {
        scanner = new Scanner(new SrcStringReader("a < b"));
        return setupTestObjects();
    }
    
    public static ExprParser getAndRel() {
        scanner = new Scanner(new SrcStringReader("(a <= b) && (b == 1)"));
        return setupTestObjects();
    }
    
    public static ExprParser getOrRel() {
        scanner = new Scanner(new SrcStringReader("(a != b) || (b >= 1)"));
        return setupTestObjects();
    }
    
    public static ExprParser getAndOrRel() {
        scanner = new Scanner(new SrcStringReader("((a < b) && (b > 1)) || (c < 0)"));
        return setupTestObjects();
    }
    
    private static ExprParser setupTestObjects() {
        code = new Code();
        sym = new SymListManager(code, scanner);
        sym.newUnit(25);
        scanner.nextToken();
        sym.newVar(0, SymListManager.ElementType.INT);
        sym.newVar(1, SymListManager.ElementType.INT);
        sym.newVar(2, SymListManager.ElementType.INT);
        Operand.setSymListManager(sym);
        return new ExprParser(scanner, sym, code);
    }
}
