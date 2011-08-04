/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import error.synerr.IdentifierExpected;
import error.synerr.SymbolExpected;
import error.*;
import nbm.Code;
import scanner.Scanner;
import scanner.Scanner.Symbol;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public abstract class Parser {

    protected final int NOIDENT = -1;
    protected final int NONUMBER = -1;
    protected Scanner scanner;
    protected SymListManager sym;
    protected Code code;
    
    public Parser(Scanner s, SymListManager sym, Code c) {
        this.scanner = s;
        this.sym = sym;
        this.code = c;
    }

    public abstract boolean parse();

    /**
     * Checks whether the current token is a specific symbol.
     * @param sy The symbol to be checked for.
     * @return true if current token is sy, false otherwise.
     */
    protected boolean tokenIsA(Symbol sy) {
        if (scanner.getCurrentToken().getSy() != sy) {
            ErrorHandler.getInstance().raise(new SymbolExpected(sy.toString(), scanner.getCurrentLine()));
            return false;
        }
        scanner.nextToken();
        return true;
    }

    /**
     * Checks whether the current token is an identifier.
     * @return The name (spix) of the identifier.
     */
    protected int ident() {
        if (scanner.getCurrentToken().getSy() != Symbol.IDENTSY) {
            ErrorHandler.getInstance().raise(new IdentifierExpected(scanner.getCurrentLine()));
            return NOIDENT;
        }
        int spix = scanner.getCurrentToken().getValue();
        scanner.nextToken();
        return spix;
    }
    
    protected int number() {
        if (scanner.getCurrentToken().getSy() != Symbol.NUMBERSY) {
            ErrorHandler.getInstance().raise(new SymbolExpected(Symbol.NUMBERSY.toString(), scanner.getCurrentLine()));
            return NONUMBER;
        }
        int val = scanner.getCurrentToken().getValue();
        scanner.nextToken();
        return val;
    }
}
