/*
 * Copyright ©2012 to 2016. Created by P. Bauer (p.bauer@htl-leonding.ac.at),
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
import error.Error;
import error.Error.ErrorType;
import java.util.HashMap;
import nbm.CodeGenerator;
import nbm.Nbm;
import scanner.Scanner;
import scanner.Scanner.Symbol;
import symboltable.Operand;
import symboltable.Operand.Kind;
import symboltable.SymbolTableEntry;
import symboltable.SymbolTable;
import symboltable.SymbolTable.ElementType;

/**
 *
 * @author peter
 */
public class VariableDeclarationParser extends ParserForAssignment {

    private Symbol parsedType;
    private SymbolTable.ElementType basicType;
    private int maxNumberOfElements;

    private static final HashMap<Symbol, SymbolTable.ElementType> symbolToElementTypeMap;

    static {
        symbolToElementTypeMap = new HashMap<>();
        symbolToElementTypeMap.put(Symbol.BOOL, ElementType.BOOL);
        symbolToElementTypeMap.put(Symbol.INT, ElementType.INT);
        symbolToElementTypeMap.put(Symbol.CHAR, ElementType.CHAR);
    }

    VariableDeclarationParser(Scanner s, SymbolTable sym, CodeGenerator c, ErrorHandler eh) {
        super();
    }

    public VariableDeclarationParser() {
    }

    @Override
    protected void parseSpecificPart() {
        parseSimpleType();
        parseArraySpecificationIfNecessary();
        int name = parseIdentifier();
        validateAndAddIdentifierToSymbolTable(name);
        if (getScanner().getCurrentToken().getSymbol() == Symbol.ASSIGN) {
            parseAssignment(name);
        }
        parseSymbol(Symbol.SEMICOLON);
    }

    private void parseSimpleType() {
        assertThatCurrentSymbolIsOf(Symbol.INT, Symbol.CHAR, Symbol.BOOL);
        parsedType = scanner.getCurrentToken().getSymbol();
        parseSymbol(parsedType);
    }

    /**
     * Checks whether the current symbol is one of the symbols given as
     * arguments.
     *
     * @param symbol One of the possible symbols.
     * @param furtherSymbols Further possible symbols.
     */
    protected void assertThatCurrentSymbolIsOf(Symbol symbol, Symbol... furtherSymbols) {
        if (!parsingWasSuccessful()) {
            return;
        }

        Symbol currentSymbol = scanner.getCurrentToken().getSymbol();
        boolean properSymbolFound = currentSymbol == symbol;

        setWasSuccessful(properSymbolFound);
        for (Symbol sy : furtherSymbols) {
            properSymbolFound = properSymbolFound || currentSymbol == sy;
            setWasSuccessful(properSymbolFound);
        }
        if (!parsingWasSuccessful()) {
            getErrorHandler().throwStatementExpected(currentSymbol.toString());
        }
    }

    private void parseArraySpecificationIfNecessary() {
        if (scanner.getCurrentToken().getSymbol() == Symbol.LBRACKET) {
            parseArraySpecification();
        } else {
            sem(() -> maxNumberOfElements = 1);
        }
    }

    private void parseArraySpecification() {
        parseSymbol(Symbol.LBRACKET);
        maxNumberOfElements = parseNumber();
        where(maxNumberOfElements > 0, () -> getErrorHandler().throwPositiveArraySizeExpected());
        parseSymbol(Symbol.RBRACKET);
    }

    private void validateAndAddIdentifierToSymbolTable(int name) {
        where(!identifierIsDeclared(name), () -> getErrorHandler().throwVariableAlreadyDefined(getLastParsedToken().getClearName()));
        sem(() -> sym.newVar(getLastParsedToken().getValue(), symbolToElementTypeMap.get(parsedType), maxNumberOfElements));
    }

    private boolean identifierIsDeclared(int name) {
        return sym.findObject(name).getKind() != Kind.ILLEGAL;
    }

    private void parseAssignment(int name) {
        emitCodeForLoadingLeftHandSide(name);
        parseSymbol(Symbol.ASSIGN);
        parseRightHandSide();
    }

    private void emitCodeForLoadingLeftHandSide(int name) {
        sem(() -> {
            destOp = sym.findObject(name).createOperand();
            destAddrOp = destOp.emitLoadAddr(code);
        });
    }

    private boolean type() {
        if (!typeSpec()) {
            return false;
        }
        return true;
    }

    private boolean typeSpec() {
        return simpleType();
    }

    private boolean simpleType() {
        switch (scanner.getCurrentToken().getSymbol()) {
            case INT:
                basicType = ElementType.INT;
                break;

            case CHAR:
                basicType = ElementType.CHAR;
                break;

            case BOOL:
                basicType = ElementType.BOOL;
                break;

            default:
                String[] sList = {Symbol.INT.toString(), Symbol.CHAR.toString(), Symbol.BOOL.toString()};
                getErrorHandler().raise(new Error(ErrorType.SYMBOL_EXPECTED, sList));
                return false;
        }
        scanner.nextToken();
        return true;
    }
}
