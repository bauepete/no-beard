/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import error.ErrorHandler;
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
public class SimExprParser extends Parser {

    private Operand op;

    private enum AddopType {

        NOADD, PLUS, MINUS
    }
    AddopType addOperator = AddopType.NOADD;

    public SimExprParser(Scanner s, SymListManager sym, Code c) {
        super(s, sym, c);
    }

    @Override
    public boolean parse() {

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
        if (addOperator != AddopType.NOADD && termP.getOperand().getType() != OperandType.SIMPLEINT) {
            ErrorHandler.getInstance().raise(new TypeExpected(OperandType.SIMPLEINT.toString(), scanner.getCurrentLine()));
            return false;
        }
        // endcc
        
        // sem
        if (addOperator == AddopType.MINUS) {
            op = op1.emitLoadVal(code);
            code.emitOp(Opcode.NEG);
        }
        else {
            op = op1;
        }
        // endsem

        while (tokenIsAddOp()) {
            if (!addOp()) {
                return false;
            }
            // cc
            if (op.getType() != OperandType.SIMPLEINT) {
                ErrorHandler.getInstance().raise(new TypeExpected(OperandType.SIMPLEINT.toString(), scanner.getCurrentLine()));
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
            if (op2.getType() != OperandType.SIMPLEINT) {
                ErrorHandler.getInstance().raise(new TypeExpected(OperandType.SIMPLEINT.toString(), scanner.getCurrentLine()));
                return false;
            }
            // endcc
            // sem
            op = op2.emitLoadVal(code);
            if (addOperator == AddopType.PLUS) {
                code.emitOp(Opcode.ADD);
            }
            else {
                code.emitOp(Opcode.SUB);
            }
            // endsem

        }
        return true;
    }

    public Operand getOperand() {
        return op;
    }

    private boolean tokenIsAddOp() {
        Symbol sy = scanner.getCurrentToken().getSy();
        return (sy == Symbol.PLUSSY || sy == Symbol.MINUSSY);
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
                ErrorHandler.getInstance().raise(new SymbolExpected(Symbol.PLUSSY.toString(), Symbol.MINUSSY.toString(), scanner.getCurrentLine()));
                return false;
        }
        return true;
    }
}
