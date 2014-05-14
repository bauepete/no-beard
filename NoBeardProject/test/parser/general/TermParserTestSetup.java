/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.general;

import nbm.Code;
import parser.TermParser;
import symlist.Operand;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class TermParserTestSetup extends ParserTestSetup {

    public static TermParser getMulTermSetup() {
        return setupTestObjects("a * b * c");
    }
    
    public static TermParser getDivTermSetup() {
        return setupTestObjects("1 / 2 / a");
    }
    
    public static TermParser getModTermSetup() {
        return setupTestObjects("10 % b % c");
    }
    
    public static TermParser getAndTermSetup() {
        return setupBoolTestObjects("a && b && c");
    }

    public static TermParser getNoTermSetup() {
        return setupTestObjects("-b");
    }

    private static TermParser setupTestObjects(String srcLine) {
        setupScanner(srcLine);
        code = new Code(256);
        sym = new SymListManager(code, scanner, errorHandler);
        sym.newUnit(25);
        scanner.nextToken();
        sym.newVar(0, SymListManager.ElementType.INT);
        sym.newVar(1, SymListManager.ElementType.INT);
        sym.newVar(2, SymListManager.ElementType.INT);
        Operand.setSymListManager(sym);
        return new TermParser(scanner, sym, code, errorHandler);
    }

    private static TermParser setupBoolTestObjects(String srcLine) {
        setupScanner(srcLine);
        code = new Code(256);
        sym = new SymListManager(code, scanner, errorHandler);
        sym.newUnit(25);
        scanner.nextToken();
        sym.newVar(0, SymListManager.ElementType.BOOL);
        sym.newVar(1, SymListManager.ElementType.BOOL);
        sym.newVar(2, SymListManager.ElementType.BOOL);
        Operand.setSymListManager(sym);
        return new TermParser(scanner, sym, code, errorHandler);
    }
}
