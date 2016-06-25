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
    public boolean parseOldStyle() {
        AddExpressionParser exprP = new AddExpressionParser(scanner, sym, code, getErrorHandler());
        switch (scanner.getCurrentToken().getSy()) {
            case INT:
            case BOOL:
            case CHAR:
                VariableDeclarationParser varDeclP = new VariableDeclarationParser(scanner, sym, code, getErrorHandler());
                if (!varDeclP.parseOldStyle()) {
                    return false;
                }
                if (!tokenIsA(Symbol.SEMICOLON)) {
                    getErrorHandler().raise(new Error(Error.ErrorType.SYMBOL_EXPECTED, Symbol.SEMICOLON.toString()));
                    return false;
                }
                break;
                
            case IDENTIFIER:
                AssignmentParser assignP = new AssignmentParser(scanner, sym, code, getErrorHandler());
                if (!assignP.parseOldStyle()) {
                    return false;
                }

                if (!tokenIsA(Symbol.SEMICOLON)) {
                    getErrorHandler().raise(new Error(Error.ErrorType.SYMBOL_EXPECTED, Symbol.SEMICOLON.toString()));
                    return false;
                }

                break;

            case PUT:
            case PUTLN:
                PutStatParser putStatP = new PutStatParser(scanner, sym, code, getErrorHandler());
                if (!putStatP.parseOldStyle()) {
                    return false;
                }
                if (!tokenIsA(Symbol.SEMICOLON)) {
                    getErrorHandler().raise(new Error(Error.ErrorType.SYMBOL_EXPECTED, Symbol.SEMICOLON.toString()));
                    return false;
                }
                break;
                
            case IF:
                IfParser ifStatP = new IfParser(scanner, sym, code, getErrorHandler());
                if (!ifStatP.parseOldStyle()) {
                    return false;
                }

            case DONE: // if stat is empty
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

    @Override
    protected void parseSpecificPart() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
