/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import error.ErrorHandler;
import error.synerr.SymbolExpected;
import nbm.Code;
import scanner.Scanner;
import scanner.Scanner.Symbol;
import symlist.ConstantOperand;
import symlist.Operand;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class FactParser extends Parser {

    private Operand op;
    
    public FactParser(Scanner s, SymListManager sym, Code c) {
        super(s, sym, c);
    }

    @Override
    public boolean parse() {

        switch (scanner.getCurrentToken().getSy()) {
            case IDENTSY:
                ReferenceParser refP = new ReferenceParser(scanner, sym, code);
                if (!refP.parse()) {
                    return false;
                }
                op = refP.getOperand();
                
                break;

            case NUMBERSY:
                int val = number();
                // sem
                op = new ConstantOperand(Operand.OperandType.SIMPLEINT, 4, val, 0);
                // endsem
                break;
                
            case STRINGSY:
                // sem
                Operand.OperandType opType;
                if (scanner.getStringLength() == 1) {
                    opType = Operand.OperandType.SIMPLECHAR;
                }
                else {
                    opType = Operand.OperandType.ARRAYCHAR;
                }
                op = new ConstantOperand(opType, scanner.getStringLength(), scanner.getStringAddress(), 0);
                // endsem
                scanner.nextToken();
                break;

            case LPARSY:
                scanner.nextToken();
                
                ExprParser exprP = new ExprParser(scanner, sym, code);
                if (!exprP.parse()) {
                    return false;
                }
                op = exprP.getOperand();

                if (!tokenIsA(Symbol.RPARSY)) {
                    return false;
                }
                break;


            default:
                ErrorHandler.getInstance().raise(new SymbolExpected(Symbol.IDENTSY.toString(), Symbol.NUMBERSY.toString(), Symbol.LPARSY.toString(), scanner.getCurrentLine()));
                return false;
        }

        return true;
    }
    
    public Operand getOperand() {
        return op;
    }
}
