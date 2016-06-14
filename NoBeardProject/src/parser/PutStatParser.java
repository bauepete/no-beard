/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import error.Error;
import error.Error.ErrorType;
import error.ErrorHandler;
import java.util.LinkedList;
import java.util.List;
import nbm.CodeGenerator;
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

    public PutStatParser(Scanner s, SymListManager sym, CodeGenerator c, ErrorHandler e) {
        super(s, sym, c, e);
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
                String[] sList = {Symbol.PUTLNSY.toString(), Symbol.PUTSY.toString()};
                getErrorHandler().raise(new Error(ErrorType.SYMBOL_EXPECTED, sList));
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

        ExprParser exprP = new ExprParser(scanner, sym, code, getErrorHandler());
        if (!exprP.parse()) {
            return false;
        }

        // cc
        Operand op = exprP.getOperand();
        if (!isOperandToPut(op)) {
            getErrorHandler().raise(new Error(ErrorType.OPERAND_KIND_EXPECTED, outputableOperands()));
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
            case COMMASY:
                scanner.nextToken();

                if (!exprP.parse()) {
                    return false;
                }
                // sem
                Operand wOp = exprP.getOperand();
                // endsem
                // cc
                if (wOp.getType() != OperandType.SIMPLEINT) {
                    getErrorHandler().raise(new Error(ErrorType.TYPES_EXPECTED, OperandType.SIMPLEINT.toString()));
                    return false;
                }
                // endcc
                // sem
                wOp.emitLoadVal(code);
                // endsem
                if (!tokenIsA(Symbol.RPARSY)) {
                    getErrorHandler().raise(new Error(ErrorType.SYMBOL_EXPECTED, Symbol.RPARSY.toString()));
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
                    getErrorHandler().raise(new Error(ErrorType.OPERAND_KIND_EXPECTED, outputableOperands()));
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
                String[] sList = {Symbol.COMMASY.toString(), Symbol.RPARSY.toString()};
                getErrorHandler().raise(new Error(ErrorType.SYMBOL_EXPECTED, sList));
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

    private final OperandType[] OUTPUTABLE_OPERANDS = {
        OperandType.SIMPLECHAR, OperandType.SIMPLEINT, OperandType.ARRAYCHAR
    };
    
    private boolean isOperandToPut(Operand op) {
        for (OperandType operand : OUTPUTABLE_OPERANDS) {
            if (operand == op.getType())
                return true;
        }
        return false;
    }
    
    private String[] outputableOperands() {
        List<String> opList = new LinkedList();
        
        for (OperandType operand: OUTPUTABLE_OPERANDS)
            opList.add(operand.toString());
        return (String[]) opList.toArray();
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
