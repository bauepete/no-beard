/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import error.Error;
import error.ErrorHandler;
import nbm.Code;
import scanner.Scanner;
import scanner.Scanner.Symbol;
import symlist.Operand;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class AssignmentParser extends Parser {

    public AssignmentParser(Scanner s, SymListManager sym, Code c, ErrorHandler e) {
        super(s, sym, c, e);
    }

    @Override
    public boolean parse() {
        ReferenceParser refP = new ReferenceParser(scanner, sym, code, getErrorHandler());
        if (!refP.parse()) {
            return false;
        }
        // sem
        Operand destOp = refP.getOperand();
        Operand destAddrOp = destOp.emitLoadAddr(code); 
        // endsem
        
        if (!tokenIsA(Symbol.ASSIGNSY)) {
            getErrorHandler().raise(new Error(Error.ErrorType.SYMBOL_EXPECTED, Symbol.ASSIGNSY.toString()));
            return false;
        }
        SimExprParser exprP = new SimExprParser(scanner, sym, code, getErrorHandler());
        if (!exprP.parse()) {
            return false;
        }
        // sem
        Operand srcOp = exprP.getOperand();
        // endsem
        // cc
        if (srcOp.getType() != destOp.getType() || srcOp.getSize() != destOp.getSize()) {
            String[] opList = {srcOp.getType().toString(), destOp.getType().toString()};
            getErrorHandler().raise(new Error(Error.ErrorType.INCOMPATIBLE_TYPES, opList));
            return false;
        }
        // endcc
        // sem
        srcOp.emitAssign(code, destAddrOp);
        // endsem
        return true;
    }
}
