/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import error.ErrorHandler;
import error.semerr.IncompatibleTypes;
import error.semerr.NameAlreadyDefined;
import error.synerr.SymbolExpected;
import error.synerr.SynErr;
import nbm.Code;
import scanner.Scanner;
import scanner.Scanner.Symbol;
import symlist.Operand;
import symlist.Operand.OperandKind;
import symlist.SymListEntry;
import symlist.SymListManager;
import symlist.SymListManager.ElementType;

/**
 *
 * @author peter
 */
public class VarDeclParser extends Parser {

    private ElementType elemType;

    VarDeclParser(Scanner s, SymListManager sym, Code c) {
        super(s, sym, c);
    }

    @Override
    public boolean parse() {

        if (!type()) {
            return false;
        }

        int name = ident();
        if (name == NOIDENT) {
            return false;
        }
        // cc
        SymListEntry obj = sym.findObject(name);
        if (obj.getKind() != OperandKind.ILLEGAL) {
            ErrorHandler.getInstance().raise(new NameAlreadyDefined(scanner.getNameManager().getStringName(name), scanner.getCurrentLine()));
            return false;
        }
        // endcc
        // sem
        sym.newVar(name, elemType);
        // endsem

        if (scanner.getCurrentToken().getSy() == Symbol.ASSIGNSY) {
            // sem
            obj = sym.findObject(name);
            Operand destOp = obj.createOperand();
            Operand destAddrOp = destOp.emitLoadAddr(code);
            // endsem
            scanner.nextToken();
            ExprParser exprP = new ExprParser(scanner, sym, code);
            if (!exprP.parse()) {
                return false;
            }
            // sem
            Operand srcOp = exprP.getOperand();
            // endsem
            // cc
            if (srcOp.getType() != destOp.getType() || srcOp.getSize() != destOp.getSize()) {
                ErrorHandler.getInstance().raise(new IncompatibleTypes(srcOp.getType().toString(), destOp.getType().toString(), scanner.getCurrentLine()));
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
        if (scanner.getCurrentToken().getSy() == Symbol.LBRACKETSY) {
            scanner.nextToken();
            int val = number();
            if (val == NONUMBER) {
                ErrorHandler.getInstance().raise(new SymbolExpected(Symbol.NUMBERSY.toString(), scanner.getCurrentLine()));
                return false;
            }
            if (!tokenIsA(Symbol.RBRACKETSY)) {
                return false;
            }
        }
        return true;
    }

    private boolean typeSpec() {
        return simpleType();
    }

    private boolean simpleType() {
        switch (scanner.getCurrentToken().getSy()) {
            case INTSY:
                elemType = ElementType.INT;
                break;

            case CHARSY:
                elemType = ElementType.CHAR;
                break;

            case BOOLSY:
                elemType = ElementType.BOOL;
                break;

            default:
                ErrorHandler.getInstance().raise(new SynErr(49, "Panic: Code here is not reachable!", scanner.getCurrentLine()));
                return false;
        }
        scanner.nextToken();
        return true;
    }
}
