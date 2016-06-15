/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import error.Error;
import error.ErrorHandler;
import nbm.CodeGenerator;
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
    
    public FactParser(Scanner s, SymListManager sym, CodeGenerator c, ErrorHandler e) {
        super(s, sym, c, e);
    }

    @Override
    public boolean parseOldStyle() {

        switch (scanner.getCurrentToken().getSy()) {
            case IDENTIFIER:
                ReferenceParser refP = new ReferenceParser(scanner, sym, code, getErrorHandler());
                if (!refP.parseOldStyle()) {
                    return false;
                }
                op = refP.getOperand();
                
                break;

            case NUMBER:
                int val = number();
                // sem
                op = new ConstantOperand(Operand.OperandType.SIMPLEINT, 4, val, 0);
                // endsem
                break;
                
            case STRING:
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

            case LPAR:
                scanner.nextToken();
                
                ExprParser exprP = new ExprParser(scanner, sym, code, getErrorHandler());
                if (!exprP.parseOldStyle()) {
                    return false;
                }
                op = exprP.getOperand();

                if (!tokenIsA(Symbol.RPAR)) {
                    return false;
                }
                break;


            default:
                String[] sList = {Symbol.IDENTIFIER.toString(), Symbol.NUMBER.toString(), Symbol.LPAR.toString()};
                getErrorHandler().raise(new Error(Error.ErrorType.SYMBOL_EXPECTED, sList));
                return false;
        }

        return true;
    }
    
    public Operand getOperand() {
        return op;
    }
}
