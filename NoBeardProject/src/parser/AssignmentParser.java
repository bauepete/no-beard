/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import error.ErrorHandler;
import error.SemErr;
import error.SynErr;
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
        Operand destAddrOp = destOp.emitLoadAddr(code); 
        // endsem
        
        if (!tokenIsA(Symbol.ASSIGNSY)) {
            ErrorHandler.getInstance().raise(new SynErr().new SymbolExpected(Symbol.ASSIGNSY.toString()));
            return false;
        }
        SimExprParser exprP = new SimExprParser(scanner, sym, code);
        if (!exprP.parse()) {
            return false;
        }
        // sem
        Operand srcOp = exprP.getOperand();
        // endsem
        // cc
        if (srcOp.getType() != destOp.getType() || srcOp.getSize() != destOp.getSize()) {
            ErrorHandler.getInstance().raise(new SemErr().new IncompatibleTypes(srcOp.getType().toString(), destOp.getType().toString()));
            return false;
        }
        // endcc
        // sem
        srcOp.emitAssign(code, destAddrOp);
        // endsem
        return true;
    }
}
