/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import error.ErrorHandler;
import error.SemErr;
import error.SynErr;
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
            ErrorHandler.getInstance().raise(new SemErr().new NameAlreadyDefined(scanner.getNameManager().getStringName(name)));
            return false;
        }
        // endcc

        if (scanner.getCurrentToken().getSy() == Symbol.LBRACKETSY) {
            scanner.nextToken();
            // sem
            int val = number();
            if (val == NONUMBER) {
                ErrorHandler.getInstance().raise(new SynErr().new SymbolExpected(Symbol.NUMBERSY.toString()));
                return false;
            }
            // endsem
            // cc
            if (val <= 0) {
                ErrorHandler.getInstance().raise(new SemErr().new IndexExpected());
                return false;
            }
            // endcc

            if (!tokenIsA(Symbol.RBRACKETSY)) {
                return false;
            }
            sym.newVar(name, elemType, val);
        } else {
            // sem
            sym.newVar(name, elemType);
            // endsem
        }


        if (scanner.getCurrentToken().getSy() == Symbol.ASSIGNSY) {
            // sem
            obj = sym.findObject(name);
            Operand destOp = obj.createOperand();
            Operand destAddrOp = destOp.emitLoadAddr(code);
            // endsem
            scanner.nextToken();
            SimExprParser exprP = new SimExprParser(scanner, sym, code);
            if (!exprP.parse()) {
                return false;
            }
            // sem
            Operand srcOp = exprP.getOperand();
            // endsem
            // cc
            if (srcOp.getType() != destOp.getType() || srcOp.getSize() != destOp.getSize()) {
                ErrorHandler.getInstance().raise(new SemErr().new IncompatibleTypes(srcOp.getType().toString(), destOp.getType().toString()));
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
                String[] sList = {Symbol.INTSY.toString(), Symbol.CHARSY.toString(), Symbol.BOOLSY.toString()};
                ErrorHandler.getInstance().raise(new SynErr().new SymbolExpected(sList));
                return false;
        }
        scanner.nextToken();
        return true;
    }
}
