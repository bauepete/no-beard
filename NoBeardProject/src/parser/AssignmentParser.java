/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import error.ErrorHandler;
import error.semerr.IncompatibleTypes;
import error.synerr.SymbolExpected;
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

    public AssignmentParser(Scanner s, SymListManager sym, Code c) {
        super(s, sym, c);
    }

    @Override
    public boolean parse() {
        ReferenceParser refP = new ReferenceParser(scanner, sym, code);
        if (!refP.parse()) {
            return false;
        }
        // sem
        Operand destOp = refP.getOperand();
        destOp.emitLoadAddr(code); 
        // endsem
        
        if (!tokenIsA(Symbol.ASSIGNSY)) {
            ErrorHandler.getInstance().raise(new SymbolExpected(Symbol.ASSIGNSY.toString()));
            return false;
        }
        ExprParser exprP = new ExprParser(scanner, sym, code);
        if (!exprP.parse()) {
            return false;
        }
        // sem
        Operand srcOp = exprP.getOperand();
        // endsem
        // cc
        if (srcOp.getType() != destOp.getType() || srcOp.getSize() != destOp.getSize()) {
            ErrorHandler.getInstance().raise(new IncompatibleTypes(srcOp.getType().toString(), destOp.getType().toString()));
        }
        // endcc
        // sem
        srcOp.emitAssign(code, destOp);
        // endsem
        return true;
    }
}
