/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import error.ErrorHandler;
import error.SemErr;
import java.util.HashMap;
import nbm.Code;
import nbm.Nbm.Opcode;
import scanner.Scanner;
import scanner.Scanner.Symbol;
import scanner.Token;
import symlist.Operand;
import symlist.Operand.OperandType;
import symlist.SymListManager;
import symlist.ValueOnStackOperand;

/**
 *
 * @author peter
 */
public class ExprParser extends Parser {

    private Operand op;
    private byte ror;

    public ExprParser(Scanner scanner, SymListManager sym, Code code) {
        super(scanner, sym, code);
    }

    @Override
    public boolean parse() {

        SimExprParser simExprP = new SimExprParser(scanner, sym, code);
        if (!simExprP.parse()) {
            return false;
        }
        op = simExprP.getOperand();

        if (tokenIsARelop(scanner.getCurrentToken())) {
            
            if (!relOp()) {
                return false;
            }
            
            // sem
            switch (op.getType()) {
                case SIMPLEBOOL:
                case SIMPLECHAR:
                case SIMPLEINT:
                    op.emitLoadVal(code);
                    break;
                    
                case ARRAYBOOL:
                case ARRAYCHAR:
                case ARRAYINT:
                    op.emitLoadAddr(code);
                    break;
            }
            // endsem
            
            if (!simExprP.parse()) {
                return false;
            }
            Operand op2 = simExprP.getOperand();
            // cc
            if (op.getSize() == Operand.UNDEFSIZE || op2.getSize() == Operand.UNDEFSIZE ||
                    op.getType() != op2.getType() || op.getSize() != op2.getSize()) {
                ErrorHandler.getInstance().raise(new SemErr().new IncompatibleTypes(op.getType().toString(), op2.getType().toString()));
                return false;
            }
            // endcc
            
            // sem
            switch (op2.getType()) {
                case SIMPLEBOOL:
                case SIMPLECHAR:
                case SIMPLEINT:
                    op2.emitLoadVal(code);
                    code.emitOp(Opcode.REL);
                    code.emitByte(ror);
                    break;
                    
                default:
                    int line = scanner.getCurrentLine();
                    String[] tList = {OperandType.SIMPLEBOOL.toString(), OperandType.SIMPLECHAR.toString(), OperandType.SIMPLEINT.toString()};
                    ErrorHandler.getInstance().raise(new SemErr().new TypeExpected(tList));
                    break;
            }
            op = new ValueOnStackOperand(Operand.OperandType.SIMPLEBOOL, 4, op.getValaddr(), op.getCurrLevel());
            // ensem
        }
        return true;
    }

    public Operand getOperand() {
        return op;
    }

    private boolean tokenIsARelop(Token currentToken) {
        return (currentToken.getSy() == Symbol.LTHSY
                || currentToken.getSy() == Symbol.GTHSY
                || currentToken.getSy() == Symbol.LEQSY
                || currentToken.getSy() == Symbol.GEQSY
                || currentToken.getSy() == Symbol.EQLSY
                || currentToken.getSy() == Symbol.NEQSY);
    }
    
    private boolean relOp() {
        HashMap<Scanner.Symbol, Integer> rop = 
                new HashMap<Scanner.Symbol, Integer>(){{
                    put(Symbol.LTHSY, 0);
                    put(Symbol.LEQSY, 1);
                    put(Symbol.EQLSY, 2);
                    put(Symbol.NEQSY, 3);
                    put(Symbol.GEQSY, 4);
                    put(Symbol.GTHSY, 5);
                }};
        
        if (!rop.containsKey(scanner.getCurrentToken().getSy())) {
            return false;
        }
        
        int r = rop.get(scanner.getCurrentToken().getSy());
        ror = (byte) r;
        scanner.nextToken();
        return true;
    }
}
