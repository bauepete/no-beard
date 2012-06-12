/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import error.ErrorHandler;
import error.semerr.CantPutOperand;
import error.semerr.TypeExpected;
import error.synerr.SymbolExpected;
import nbm.Code;
import nbm.Nbm.Opcode;
import scanner.Scanner;
import scanner.Scanner.Symbol;
import symlist.Operand;
import symlist.Operand.OperandType;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class PutStatParser extends Parser {

    public PutStatParser(Scanner s, SymListManager sym, Code c) {
        super(s, sym, c);
    }

    @Override
    public boolean parse() {
        boolean isParsedCorrectly = false;

        switch (scanner.getCurrentToken().getSy()) {
            case PUTSY:
                isParsedCorrectly = put();
                break;

            case PUTLNSY:
                isParsedCorrectly = putln();
                break;

            default:
                ErrorHandler.getInstance().raise(new SymbolExpected(Symbol.PUTLNSY.toString(), Symbol.PUTSY.toString(), scanner.getCurrentLine()));
                break;
        }
        return isParsedCorrectly;
    }

    private boolean put() {
        if (!tokenIsA(Symbol.PUTSY)) {
            return false;
        }

        if (!tokenIsA(Symbol.LPARSY)) {
            return false;
        }

        ExprParser exprP = new ExprParser(scanner, sym, code);
        if (!exprP.parse()) {
            return false;
        }

        // cc
        Operand op = exprP.getOperand();
        if (!isOperandToPut(op)) {
            ErrorHandler.getInstance().raise(new CantPutOperand(scanner.getCurrentLine()));
            return false;
        }
        // endcc

        // sem
        if (op.getType() == OperandType.ARRAYCHAR) {
            op.emitLoadAddr(code);
            if (op.getSize() == Operand.UNDEFSIZE) {
                code.emitOp(Opcode.LIT);
                code.emitHalfWord(65535);
            } else {
                code.emitOp(Opcode.LIT);
                code.emitHalfWord(op.getSize());
            }
        } else {
            op.emitLoadVal(code);
        }
        // endsem

        switch (scanner.getCurrentToken().getSy()) {
            case COLONSY:
                scanner.nextToken();

                if (!exprP.parse()) {
                    return false;
                }
                // sem
                Operand wOp = exprP.getOperand();
                // endsem
                // cc
                if (wOp.getType() != OperandType.SIMPLEINT) {
                    ErrorHandler.getInstance().raise(new TypeExpected(OperandType.SIMPLEINT.toString(), scanner.getCurrentLine()));
                    return false;
                }
                // endcc
                // sem
                wOp.emitLoadVal(code);
                // endsem
                if (!tokenIsA(Symbol.RPARSY)) {
                    ErrorHandler.getInstance().raise(new SymbolExpected(Symbol.RPARSY.toString(), scanner.getCurrentLine()));
                    // TODO: Add raiseError() and getNameManager as private methods to Parser
                    // raiseError(new SymbolExpected(getNameManager().getString(Symbol.RPARSY)));
                    return false;
                }
                // sem
                emitPut(op.getType());
                //endsem

                break;

            case RPARSY:
                // cc
                if (op.getSize() == Operand.UNDEFSIZE) {
                    ErrorHandler.getInstance().raise(new CantPutOperand(scanner.getCurrentLine()));
                    return false;
                }
                // end cc
                // sem
                switch (op.getType()) {
                    case SIMPLEINT:
                        code.emitOp(Opcode.LIT);
                        code.emitHalfWord(0);
                        break;

                    case SIMPLECHAR:
                    case ARRAYCHAR:
                        code.emitOp(Opcode.LIT);
                        code.emitHalfWord(op.getSize());
                        break;
                }
                emitPut(op.getType());
                // endsem
                scanner.nextToken();
                break;

            default:
                ErrorHandler.getInstance().raise(new SymbolExpected(Symbol.COLONSY.toString(), Symbol.RPARSY.toString(), scanner.getCurrentLine()));
                return false;
        }
        return true;
    }

    private boolean putln() {
        if (!tokenIsA(Symbol.PUTLNSY)) {
            return false;
        }
        
        // sem
        code.emitOp(Opcode.PUT);
        code.emitByte((byte)3);
        // endsem
        return true;
    }

    private boolean isOperandToPut(Operand op) {
        OperandType opType = op.getType();

        return (opType == OperandType.SIMPLECHAR || opType == OperandType.SIMPLEINT || opType == OperandType.ARRAYCHAR);
    }

    private void emitPut(OperandType type) {
        switch (type) {
            case SIMPLEINT:
                code.emitOp(Opcode.PUT);
                code.emitByte((byte) 0);
                break;

            case SIMPLECHAR:
                code.emitOp(Opcode.PUT);
                code.emitByte((byte) 1);
                break;

            case ARRAYCHAR:
                code.emitOp(Opcode.PUT);
                code.emitByte((byte) 2);
                break;
        }
    }
}
