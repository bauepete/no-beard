/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import nbm.Code;
import scanner.Scanner;
import scanner.Scanner.Symbol;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class IfStatParser extends Parser {

    public IfStatParser(Scanner scanner, SymListManager sym, Code code) {
        super(scanner, sym, code);
    }
    
    @Override
    public boolean parse() {
        if (!tokenIsA(Symbol.IFSY)) {
            return false;
        }
        ExprParser exprP = new ExprParser(scanner, sym, code);
        if (!exprP.parse()) {
            return false;
        }
        // sem
        sym.newBlock();
        
        // endsem
        BlockParser blockP = new BlockParser(scanner, sym, code, sym.getCurrBlock());
        if (!blockP.parse()) {
            return false;
        }
        if (scanner.getCurrentToken().getSy() == Symbol.ELSESY) {
            if (!tokenIsA(Symbol.ELSESY)) {
                return false;
            }
            if (!blockP.parse()) {
                return false;
            }
        }
        return true;
    }
    
}
