/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import error.ErrorHandler;
import error.SynErr;
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
public class SimExprParser extends Parser {

    private Operand op;
    private int orChain;

    private enum AddopType {

        NOADD, PLUS, MINUS
    }
    AddopType addOperator = AddopType.NOADD;

    public SimExprParser(Scanner s, SymListManager sym, Code c) {
        super(s, sym, c);
    }

    @Override
    public boolean parse() {

        // sem
        orChain = 0;
        // endsem
        
        if (tokenIsAddOp()) {
            if (!addOp()) {
                return false;
            }
        }

        TermParser termP = new TermParser(scanner, sym, code);
        if (!termP.parse()) {
            return false;
        }
        Operand op1 = termP.getOperand();

        // cc
        if (addOperator != AddopType.NOADD && !operandIsA(op1, OperandType.SIMPLEINT)) {
            return false;
        }
        // endcc

        // sem
        if (addOperator == AddopType.MINUS) {
            op = op1.emitLoadVal(code);
            code.emitOp(Opcode.NEG);
        } else {
            op = op1;
        }
        // endsem

        while (tokenIsAddOp()) {
            if (scanner.getCurrentToken().getSy() == Symbol.ORSY) {
                scanner.nextToken();
                // cc
                if (!operandIsA(op, OperandType.SIMPLEBOOL)) {
                    return false;
                }
                // ccend
                
                // sem
                op.emitLoadVal(code);
                code.emitOp(Opcode.TJMP);
                code.emitHalfWord(orChain);
                orChain = code.getPc() - 2;
                // endsem
                
                if (!termP.parse()) {
                    return false;
                }
                
                // cc
                Operand op2 = termP.getOperand();
                if (!operandIsA(op2, OperandType.SIMPLEBOOL)) {
                    return false;
                }
                // endcc
                
                // sem
                op = op2.emitLoadVal(code);
                // endsem
                
            } else {
                if (!addOp()) {
                    return false;
                }
                // cc
                if (!operandIsA(op, OperandType.SIMPLEINT)) {
                    return false;
                }
                // endcc
                // sem
                op.emitLoadVal(code);
                // endsem


                if (!termP.parse()) {
                    return false;
                }
                Operand op2 = termP.getOperand();
                // cc
                if (!operandIsA(op2, OperandType.SIMPLEINT)) {
                    return false;
                }
                // endcc
                // sem
                op = op2.emitLoadVal(code);
                if (addOperator == AddopType.PLUS) {
                    code.emitOp(Opcode.ADD);
                } else {
                    code.emitOp(Opcode.SUB);
                }
                // endsem
            }

        }
        // sem
        if (orChain != 0) {
            code.emitOp(Opcode.JMP);
            code.emitHalfWord(code.getPc() + 5);
            while (orChain != 0) {
                int next = code.getCodeHalfWord(orChain);
                code.fixup(orChain, code.getPc());
                orChain = next;
            }
            code.emitOp(Opcode.LIT);
            code.emitHalfWord(1);
        }
        // endsem
        return true;
    }

    public Operand getOperand() {
        return op;
    }

    private boolean tokenIsAddOp() {
        Symbol sy = scanner.getCurrentToken().getSy();
        return (sy == Symbol.PLUSSY || sy == Symbol.MINUSSY || sy == Symbol.ORSY);
    }

    private boolean addOp() {
        switch (scanner.getCurrentToken().getSy()) {
            case PLUSSY:
                //sem
                addOperator = AddopType.PLUS;
                // endsem
                scanner.nextToken();
                break;

            case MINUSSY:
                // sem
                addOperator = AddopType.MINUS;
                // endsem
                scanner.nextToken();
                break;

            default:
                String[] sList = {Symbol.PLUSSY.toString(), Symbol.MINUSSY.toString()};
                ErrorHandler.getInstance().raise(new SynErr().new SymbolExpected(sList));
                return false;
        }
        return true;
    }
}
