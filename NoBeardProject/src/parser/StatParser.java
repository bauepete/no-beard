/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import error.ErrorHandler;
import error.Error;
import nbm.CodeGenerator;
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

    public StatParser(Scanner s, SymListManager sym, CodeGenerator c, ErrorHandler e) {
        super(s, sym, c, e);
    }

    @Override
    public boolean parse() {
        SimExprParser exprP = new SimExprParser(scanner, sym, code, getErrorHandler());
        switch (scanner.getCurrentToken().getSy()) {
            case INTSY:
            case BOOLSY:
            case CHARSY:
                VarDeclParser varDeclP = new VarDeclParser(scanner, sym, code, getErrorHandler());
                if (!varDeclP.parse()) {
                    return false;
                }
                if (!tokenIsA(Symbol.SEMICOLONSY)) {
                    getErrorHandler().raise(new Error(Error.ErrorType.SYMBOL_EXPECTED, Symbol.SEMICOLONSY.toString()));
                    return false;
                }
                break;
                
            case IDENTSY:
                AssignmentParser assignP = new AssignmentParser(scanner, sym, code, getErrorHandler());
                if (!assignP.parse()) {
                    return false;
                }

                if (!tokenIsA(Symbol.SEMICOLONSY)) {
                    getErrorHandler().raise(new Error(Error.ErrorType.SYMBOL_EXPECTED, Symbol.SEMICOLONSY.toString()));
                    return false;
                }

                break;

            case PUTSY:
            case PUTLNSY:
                PutStatParser putStatP = new PutStatParser(scanner, sym, code, getErrorHandler());
                if (!putStatP.parse()) {
                    return false;
                }
                if (!tokenIsA(Symbol.SEMICOLONSY)) {
                    getErrorHandler().raise(new Error(Error.ErrorType.SYMBOL_EXPECTED, Symbol.SEMICOLONSY.toString()));
                    return false;
                }
                break;
                
            case IFSY:
                IfStatParser ifStatP = new IfStatParser(scanner, sym, code, getErrorHandler());
                if (!ifStatP.parse()) {
                    return false;
                }

            case DONESY: // if stat is empty
                return true;

            default:
                getErrorHandler().raise(new Error(Error.ErrorType.GENERAL_SYN_ERROR, "Statement expected"));
                return false;

        }
        return true;
    }

    private boolean isOperandToPut(Operand op) {
        OperandType opType = op.getType();

        return (opType == OperandType.SIMPLECHAR || opType == OperandType.SIMPLEINT);
    }
}
