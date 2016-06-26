/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import error.ErrorHandler;
import error.Error;
import error.Error.ErrorType;
import nbm.CodeGenerator;
import scanner.Scanner;
import scanner.Scanner.Symbol;
import symboltable.Operand;
import symboltable.Operand.Kind;
import symboltable.SymListEntry;
import symboltable.SymListManager;
import symboltable.SymListManager.ElementType;

/**
 *
 * @author peter
 */
public class VariableDeclarationParser extends Parser {

    private ElementType elemType;

    VariableDeclarationParser(Scanner s, SymListManager sym, CodeGenerator c, ErrorHandler eh) {
        super(s, sym, c, eh);
    }

    @Override
    public boolean parseOldStyle() {

        if (!type()) {
            return false;
        }

        int name = ident();
        if (name == NOIDENT) {
            return false;
        }
        // cc
        SymListEntry obj = sym.findObject(name);
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
            sym.newVar(name, elemType, val);
        } else {
            // sem
            sym.newVar(name, elemType);
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
                elemType = ElementType.INT;
                break;

            case CHAR:
                elemType = ElementType.CHAR;
                break;

            case BOOL:
                elemType = ElementType.BOOL;
                break;

            default:
                String[] sList = {Symbol.INT.toString(), Symbol.CHAR.toString(), Symbol.BOOL.toString()};
                getErrorHandler().raise(new Error(ErrorType.SYMBOL_EXPECTED, sList));
                return false;
        }
        scanner.nextToken();
        return true;
    }

    @Override
    protected void parseSpecificPart() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
