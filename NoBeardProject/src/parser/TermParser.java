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
public class TermParser extends Parser {

    private enum MulopType {

        NOMUL, TIMES, DIV, MOD
    }
    private MulopType mulOperator = MulopType.NOMUL;
    
    private Operand op;

    public TermParser(Scanner s, SymListManager sym, Code c) {
        super(s, sym, c);
    }

    @Override
    public boolean parse() {
        FactParser factP = new FactParser(scanner, sym, code);
        if (!factP.parse()) {
            return false;
        }
        op = factP.getOperand();

        while (tokenIsMulOp()) {
            if (!mulOp()) {
                return false;
            }

            //cc
            if (op.getType() != OperandType.SIMPLEINT) {
                ErrorHandler.getInstance().raise(new TypeExpected(OperandType.SIMPLEINT.toString()));
                return false;
            }
            // endcc
            // sem
            op.emitLoadVal(code);
            // endsem
            if (!factP.parse()) {
                return false;
            }

            Operand op2 = factP.getOperand();

            // sem
            op = op2.emitLoadVal(code);

            switch (mulOperator) {
                case TIMES:
                    code.emitOp(Opcode.MUL);
                    break;

                case DIV:
                    code.emitOp(Opcode.DIV);
                    break;

                case MOD:
                    code.emitOp(Opcode.DIV);
                    break;
                    
                default:
                    return false;
            }
            // endsem
        }
        return true;
    }

    public Operand getOperand() {
        return op;
    }

    private boolean tokenIsMulOp() {
        Symbol sy = scanner.getCurrentToken().getSy();
        return (sy == Symbol.TIMESSY || sy == Symbol.DIVSY || sy == Symbol.MODSY);
    }

    private boolean mulOp() {
        switch (scanner.getCurrentToken().getSy()) {
            case TIMESSY:
                mulOperator = MulopType.TIMES;
                scanner.nextToken();
                break;

            case DIVSY:
                mulOperator = MulopType.DIV;
                scanner.nextToken();
                break;

            case MODSY:
                mulOperator = MulopType.MOD;
                scanner.nextToken();
                break;

            default:
                ErrorHandler.getInstance().raise(new SymbolExpected(Symbol.TIMESSY.toString()));
                return false;
        }
        return true;
    }
}
