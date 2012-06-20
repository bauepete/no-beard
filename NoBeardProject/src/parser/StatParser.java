/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import error.ErrorHandler;
import error.synerr.StatementExpected;
import error.synerr.SymbolExpected;
import nbm.Code;
import scanner.Scanner;
import scanner.Scanner.Symbol;
import symlist.Operand;
import symlist.Operand.OperandType;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class StatParser extends Parser {

    public StatParser(Scanner s, SymListManager sym, Code c) {
        super(s, sym, c);
    }

    @Override
    public boolean parse() {
        SimExprParser exprP = new SimExprParser(scanner, sym, code);
        switch (scanner.getCurrentToken().getSy()) {
            case INTSY:
            case BOOLSY:
            case CHARSY:
                VarDeclParser varDeclP = new VarDeclParser(scanner, sym, code);
                if (!varDeclP.parse()) {
                    return false;
                }
                if (!tokenIsA(Symbol.SEMICOLONSY)) {
                    ErrorHandler.getInstance().raise(new SymbolExpected(Symbol.SEMICOLONSY.toString(), scanner.getCurrentLine()));
                    return false;
                }
                break;
                
            case IDENTSY:
                AssignmentParser assignP = new AssignmentParser(scanner, sym, code);
                if (!assignP.parse()) {
                    return false;
                }

                if (!tokenIsA(Symbol.SEMICOLONSY)) {
                    ErrorHandler.getInstance().raise(new SymbolExpected(Symbol.SEMICOLONSY.toString(), scanner.getCurrentLine()));
                    return false;
                }

                break;

            case PUTSY:
            case PUTLNSY:
                PutStatParser putStatP = new PutStatParser(scanner, sym, code);
                if (!putStatP.parse()) {
                    return false;
                }
                if (!tokenIsA(Symbol.SEMICOLONSY)) {
                    ErrorHandler.getInstance().raise(new SymbolExpected(Symbol.SEMICOLONSY.toString(), scanner.getCurrentLine()));
                    return false;
                }
                break;
                
            case IFSY:
                IfStatParser ifStatP = new IfStatParser(scanner, sym, code);
                if (!ifStatP.parse()) {
                    return false;
                }

            case DONESY: // if stat is empty
                return true;

            default:
                ErrorHandler.getInstance().raise(new StatementExpected(scanner.getCurrentLine()));
                return false;

        }
        return true;
    }

    private boolean isOperandToPut(Operand op) {
        OperandType opType = op.getType();

        return (opType == OperandType.SIMPLECHAR || opType == OperandType.SIMPLEINT);
    }
}
