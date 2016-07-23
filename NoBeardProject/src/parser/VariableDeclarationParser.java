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
public class VariableDeclarationParser extends Parser {

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
        parseSymbol(Symbol.SEMICOLON);
    }

    private void parseSimpleType() {
        assertThatCurrentSymbolIsOf(Symbol.INT, Symbol.CHAR, Symbol.BOOL);
        parsedType = scanner.getCurrentToken().getSy();
        parseSymbol(parsedType);
    }

    /**
     * Checks whether the current symbol is one of the symbols given as arguments.
     * @param symbol One of the possible symbols.
     * @param furtherSymbols Further possible symbols.
     */
    protected void assertThatCurrentSymbolIsOf(Symbol symbol, Symbol... furtherSymbols) {
        if (!parsingWasSuccessful()) return;
        
        Symbol currentSymbol = scanner.getCurrentToken().getSy();
        boolean properSymbolFound = currentSymbol == symbol;
        
        setWasSuccessful(properSymbolFound);
        for (Symbol sy : furtherSymbols) {
            properSymbolFound = properSymbolFound || currentSymbol == sy;
            setWasSuccessful(properSymbolFound);
        }
    }

    private void parseArraySpecificationIfNecessary() {
        if (scanner.getCurrentToken().getSy() == Symbol.LBRACKET) {
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
        where(sym.findObject(name).getKind() == Kind.ILLEGAL, () -> getErrorHandler().throwVariableAlreadyDefined(getLastParsedToken().toString()));
        sem(() -> sym.newVar(getLastParsedToken().getValue(), symbolToElementTypeMap.get(parsedType), maxNumberOfElements));
    }

    @Override
    public boolean parseOldStyle() {

        if (!type()) {
            return false;
        }

        int name = parseIdentifier();
        if (name == NOIDENT) {
            return false;
        }
        // cc
        SymbolTableEntry obj = sym.findObject(name);
        if (obj.getKind() != Kind.ILLEGAL) {
            getErrorHandler().raise(new Error(Error.ErrorType.NAME_ALREADY_DEFINED, scanner.getNameManager().getStringName(name)));
            return false;
        }
        // endcc

        if (scanner.getCurrentToken().getSy() == Symbol.LBRACKET) {
            scanner.nextToken();
            // sem
            int val = parseNumber();
            if (val == NONUMBER) {
                getErrorHandler().raise(new Error(Error.ErrorType.SYMBOL_EXPECTED, Symbol.NUMBER.toString()));
                return false;
            }
            // endsem
            // cc
            if (val <= 0) {
                getErrorHandler().raise(new Error(ErrorType.POSITIVE_ARRAY_SIZE_EXPECTED));
                return false;
            }
            // endcc

            if (!tokenIsA(Symbol.RBRACKET)) {
                return false;
            }
            sym.newVar(name, basicType, val);
        } else {
            // sem
            sym.newVar(name, basicType);
            // endsem
        }

        if (scanner.getCurrentToken().getSy() == Symbol.ASSIGN) {
            // sem
            obj = sym.findObject(name);
            Operand destOp = obj.createOperand();
            Operand destAddrOp = destOp.emitLoadAddr(code);
            // endsem
            scanner.nextToken();
            AddExpressionParser exprP = new AddExpressionParser(scanner, sym, code, getErrorHandler());
            if (!exprP.parseOldStyle()) {
                return false;
            }
            // sem
            Operand srcOp = exprP.getOperand();
            // endsem
            // cc
            if (srcOp.getType() != destOp.getType() || srcOp.getSize() != destOp.getSize()) {
                String[] tList = {srcOp.getType().toString(), destOp.getType().toString()};
                getErrorHandler().raise(new Error(ErrorType.INCOMPATIBLE_TYPES, tList));
                return false;
            }
            // endcc
            // sem
            srcOp.emitAssign(code, destAddrOp);
            // endsem
        }
        return true;
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
        switch (scanner.getCurrentToken().getSy()) {
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
