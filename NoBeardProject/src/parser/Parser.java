/*
 * Copyright ©2015, 2016. Created by P. Bauer (p.bauer@htl-leonding.ac.at),
 * Department of Informatics and Media Technique, HTBLA Leonding,
 * Limesstr. 12 - 14, 4060 Leonding, AUSTRIA. All Rights Reserved. Permission
 * to use, copy, modify, and distribute this software and its documentation
 * for educational, research, and not-for-profit purposes, without fee and
 * without a signed licensing agreement, is hereby granted, provided that the
 * above copyright notice, this paragraph and the following two paragraphs
 * appear in all copies, modifications, and distributions. Contact the Head of
 * Informatics and Media Technique, HTBLA Leonding, Limesstr. 12 - 14,
 * 4060 Leonding, Austria, for commercial licensing opportunities.
 * 
 * IN NO EVENT SHALL HTBLA LEONDING BE LIABLE TO ANY PARTY FOR DIRECT,
 * INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST
 * PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION,
 * EVEN IF HTBLA LEONDING HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * HTBLA LEONDING SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE. THE SOFTWARE AND ACCOMPANYING DOCUMENTATION, IF ANY,
 * PROVIDED HEREUNDER IS PROVIDED "AS IS". HTBLA LEONDING HAS NO OBLIGATION
 * TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 */
package parser;

import error.Error;
import error.Error.ErrorType;
import error.ErrorHandler;
import nbm.CodeGenerator;
import scanner.Scanner;
import scanner.Scanner.Symbol;
import scanner.Token;
import symboltable.Operand;
import symboltable.Operand.Type;
import symboltable.SymListManager;

/**
 *
 * @author peter
 */
public abstract class Parser {

    @FunctionalInterface
    protected interface SemanticAction {

        public void doSemanticAction();
    }

    public void sem(SemanticAction semanticAction) {
        if (parsingWasSuccessfulUntilNow) {
            semanticAction.doSemanticAction();
        }
    }

    @FunctionalInterface
    protected interface ContextualConditionFailHandler {

        public void handleErrorCase();
    }

    public void where(boolean conditionIsTrue, ContextualConditionFailHandler h) {
        if (parsingWasSuccessfulUntilNow) {
            if (!conditionIsTrue) {
                h.handleErrorCase();
                parsingWasSuccessfulUntilNow = false;
            }
        }
    }
    private boolean parsingWasSuccessfulUntilNow;

    private Token lastParsedToken;
    protected final int NOIDENT = -1;
    protected final int NONUMBER = -1;
    protected Scanner scanner;
    protected SymListManager sym;
    protected CodeGenerator code;
    private final ErrorHandler errorHandler;

    public Parser() {
        this.lastParsedToken = new Token();
        this.scanner = ParserFactory.getScanner();
        this.sym = ParserFactory.getSymbolListManager();
        this.code = ParserFactory.getCodeGenerator();
        this.errorHandler = ParserFactory.getErrorHandler();
    }

    public Parser(Scanner s, SymListManager sym, CodeGenerator c, ErrorHandler errorHandler) {
        this.scanner = s;
        this.sym = sym;
        this.code = c;
        this.errorHandler = errorHandler;
    }

    /**
     * Does the parsing and returns whether this was successful or not
     *
     * @return true if parsing was successful, false otherwise.
     * @deprecated
     */
    public abstract boolean parseOldStyle();

    protected abstract void parseSpecificPart();

    /**
     * 
     * @return 
     */
    public final boolean parse() {
        parsingWasSuccessfulUntilNow = true;
        parseSpecificPart();
        return parsingWasSuccessfulUntilNow;
    }

    /**
     *
     * @return true if parseOldStyle() was not called yet or parsing was
     * successful, false otherwise.
     */
    public boolean parsingWasSuccessful() {
        return parsingWasSuccessfulUntilNow;
    }
    
    /**
     * Temp. protected method for migrating from old parsers.
     * @param wasSuccessful 
     * @deprecated 
     */
    protected void setWasSuccessful(boolean wasSuccessful) {
        parsingWasSuccessfulUntilNow = wasSuccessful;
    }

    protected void parseSymbol(Symbol symbol) {
        if (parsingWasSuccessfulUntilNow) {
            lastParsedToken = scanner.getCurrentToken();
            parsingWasSuccessfulUntilNow = lastParsedToken.getSy() == symbol;
            if (!parsingWasSuccessfulUntilNow) {
                errorHandler.throwSymbolExpectedError(symbol.toString(), lastParsedToken.getSy().toString());
            }
        }
        scanner.nextToken();
    }

    protected void parseSymbol(Parser p) {
        parsingWasSuccessfulUntilNow = parsingWasSuccessfulUntilNow && p.parse();
        lastParsedToken = p.getLastParsedToken();
    }

    protected void throwSymbolExpected(String expected, String actual) {
        errorHandler.throwSymbolExpectedError(expected, actual);
        parsingWasSuccessfulUntilNow = false;
    }

    /**
     * Checks whether the current token is a specific symbol.
     *
     * @param sy The symbol to be checked for.
     * @return true if current token is sy, false otherwise.
     * @deprecated
     */
    protected boolean tokenIsA(Symbol sy) {
        if (scanner.getCurrentToken().getSy() != sy) {
            getErrorHandler().throwSymbolExpectedError(sy.toString(), scanner.getCurrentToken().getSy().toString());
            parsingWasSuccessfulUntilNow = false;
            return false;
        }
        scanner.nextToken();
        return true;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    protected Token getLastParsedToken() {
        return lastParsedToken;
    }

    protected boolean operandIsA(Operand op, Type opType) {
        if (op.getType() != opType) {
            getErrorHandler().raise(new Error(ErrorType.TYPES_EXPECTED, opType.toString()));
            //ErrorHandler.getInstance().raise(new SemErr().new TypeExpected(opType.toString()));
            return false;
        }
        return true;
    }

    /**
     * Checks whether the current token is an identifier.
     *
     * @return The name (spix) of the identifier.
     */
    protected int ident() {
        parseSymbol(Symbol.IDENTIFIER);
        int spix = getLastParsedToken().getValue();
        return spix;
    }

    protected int parseNumber() {
        parseSymbol(Symbol.NUMBER);
        int val = getLastParsedToken().getValue();
        return val;
    }
}
