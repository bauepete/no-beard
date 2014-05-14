/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import error.Error;
import error.Error.ErrorType;
import error.ErrorHandler;
import nbm.Code;
import scanner.Scanner;
import scanner.Scanner.Symbol;
import symlist.Operand;
import symlist.Operand.OperandType;
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
    private final ErrorHandler errorHandler;

    public Parser(Scanner s, SymListManager sym, Code c, ErrorHandler errorHandler) {
        this.scanner = s;
        this.sym = sym;
        this.code = c;
        this.errorHandler = errorHandler;
    }

    public abstract boolean parse();

    /**
     * Checks whether the current token is a specific symbol.
     * @param sy The symbol to be checked for.
     * @return true if current token is sy, false otherwise.
     */
    protected boolean tokenIsA(Symbol sy) {
        if (scanner.getCurrentToken().getSy() != sy) {
            getErrorHandler().raise(new Error(ErrorType.SYMBOL_EXPECTED, sy.toString()));
            return false;
        }
        scanner.nextToken();
        return true;
    }
    
    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    protected boolean operandIsA(Operand op, OperandType opType) {
        if (op.getType() != opType) {
            getErrorHandler().raise(new Error(ErrorType.TYPE_EXPECTED, opType.toString()));
            //ErrorHandler.getInstance().raise(new SemErr().new TypeExpected(opType.toString()));
            return false;
        }
        return true;
    }

    /**
     * Checks whether the current token is an identifier.
     * @return The name (spix) of the identifier.
     */
    protected int ident() {
        if (scanner.getCurrentToken().getSy() != Symbol.IDENTSY) {
            getErrorHandler().raise(new Error(ErrorType.SYMBOL_EXPECTED, Symbol.IDENTSY.toString()));
//            ErrorHandler.getInstance().raise(new SynErr().new IdentifierExpected());
            return NOIDENT;
        }
        int spix = scanner.getCurrentToken().getValue();
        scanner.nextToken();
        return spix;
    }

    protected int number() {
        if (scanner.getCurrentToken().getSy() != Symbol.NUMBERSY) {
            getErrorHandler().raise(new Error(ErrorType.SYMBOL_EXPECTED, Symbol.NUMBERSY.toString()));
//            ErrorHandler.getInstance().raise(new SynErr().new SymbolExpected(Symbol.NUMBERSY.toString()));
            return NONUMBER;
        }
        int val = scanner.getCurrentToken().getValue();
        scanner.nextToken();
        return val;
    }
}
