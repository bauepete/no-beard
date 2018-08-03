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

import error.ErrorHandler;
import machine.CodeGenerator;
import scanner.Scanner;
import scanner.Scanner.Symbol;
import scanner.Token;
import symboltable.SymbolTable;

/**
 *
 * @author peter
 */
public abstract class Parser {

    @FunctionalInterface
    protected interface SemanticAction {

        public void doSemanticAction();
    }

    protected void sem(SemanticAction semanticAction) {
        if (parsingWasSuccessfulUntilNow) {
            semanticAction.doSemanticAction();
        }
    }

    @FunctionalInterface
    protected interface ContextualConditionFailHandler {

        public void handleErrorCase();
    }

    protected void where(boolean conditionIsTrue, ContextualConditionFailHandler h) {
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
    private final Scanner scanner;
    protected SymbolTable sym;
    protected CodeGenerator code;
    private final ErrorHandler errorHandler;

    public Parser() {
        this.lastParsedToken = new Token();
        this.scanner = ParserFactory.getScanner();
        this.sym = ParserFactory.getSymbolListManager();
        this.code = ParserFactory.getCodeGenerator();
        this.errorHandler = ParserFactory.getErrorHandler();
    }

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
     * @return true if parse() was not called yet or parsing was
     * successful, false otherwise.
     */
    public boolean parsingWasSuccessful() {
        return parsingWasSuccessfulUntilNow;
    }

    /**
     * Temp. protected method for migrating from old parsers.
     *
     * @param wasSuccessful
     * @deprecated
     */
    protected void setWasSuccessful(boolean wasSuccessful) {
        parsingWasSuccessfulUntilNow = wasSuccessful;
    }

    /**
     * Parses a terminal symbol.
     * @param symbol Symbol to be parsed.
     */
    protected void parseSymbol(Symbol symbol) {
        if (parsingWasSuccessfulUntilNow) {
            lastParsedToken = scanner.getCurrentToken();
            parsingWasSuccessfulUntilNow = lastParsedToken.getSymbol() == symbol;
            if (!parsingWasSuccessfulUntilNow) {
                errorHandler.throwSymbolExpectedError(symbol.toString(), lastParsedToken.getSymbol().toString());
            }
        }
        scanner.nextToken();
    }

    /**
     * Parses a non-terminal symbol represented by an extra parser.
     * @param p Parser representing the non-terminal symbol.
     */
    protected void parseSymbol(Parser p) {
        parsingWasSuccessfulUntilNow = parsingWasSuccessfulUntilNow && p.parse();
        lastParsedToken = p.getLastParsedToken();
    }

    /**
     * Parses an identifier.
     *
     * @return The name (spix) of the identifier.
     */
    protected int parseIdentifier() {
        parseSymbol(Symbol.IDENTIFIER);
        int spix = getLastParsedToken().getValue();
        return spix;
    }

    /**
     * Parses a number.
     *
     * @return The value of the parsed number
     */
    protected int parseNumber() {
        parseSymbol(Symbol.NUMBER);
        int val = getLastParsedToken().getValue();
        return val;
    }

    protected void throwSymbolExpected(String expected, String actual) {
        errorHandler.throwSymbolExpectedError(expected, actual);
        parsingWasSuccessfulUntilNow = false;
    }

    protected Scanner getScanner() {
        return scanner;
    }

    
    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    protected Token getLastParsedToken() {
        return lastParsedToken;
    }
}
