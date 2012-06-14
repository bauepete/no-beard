/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import nbm.Code;
import scanner.Scanner;
import scanner.Scanner.Symbol;
import scanner.Token;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class ExprParser extends Parser {

    public ExprParser(Scanner scanner, SymListManager sym, Code code) {
        super(scanner, sym, code);
    }
    
    @Override
    public boolean parse() {
        SimExprParser simExprP = new SimExprParser(scanner, sym, code);
        if (!simExprP.parse()) {
            return false;
        }
        while (tokenIsARelop(scanner.getCurrentToken())) {
            scanner.nextToken();
            if (!simExprP.parse()) {
                return false;
            }
        }
        return true;
    }
    
    private boolean tokenIsARelop(Token currentToken) {
        return (currentToken.getSy() == Symbol.LTHSY ||
                currentToken.getSy() == Symbol.GTHSY ||
                currentToken.getSy() == Symbol.LEQSY ||
                currentToken.getSy() == Symbol.GEQSY ||
                currentToken.getSy() == Symbol.EQLSY ||
                currentToken.getSy() == Symbol.NEQSY);
    }
    
}
