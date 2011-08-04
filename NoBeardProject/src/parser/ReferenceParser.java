/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import error.ErrorHandler;
import error.semerr.IllegalOperand;
import nbm.Code;
import scanner.Scanner;
import symlist.Operand;
import symlist.Operand.OperandKind;
import symlist.SymListEntry;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class ReferenceParser extends Parser {

    private Operand op;

    public ReferenceParser(Scanner s, SymListManager sym, Code c) {
        super(s, sym, c);
    }

    @Override
    public boolean parse() {
        int name = ident();
        if (name == NOIDENT) {
            return false;
        }
        // sem
        SymListEntry obj = sym.findObject(name);
        // endsem

        // cc
        if (obj.getKind() == OperandKind.ILLEGAL) {
            ErrorHandler.getInstance().raise(new IllegalOperand(scanner.getCurrentLine()));
            return false;
        }
        
        // sem
        op = obj.createOperand();
        // endsem
        
        if (op.getKind() != OperandKind.VARIABLE) {
            ErrorHandler.getInstance().raise(new IllegalOperand(scanner.getCurrentLine()));
            return false;
        }
        // endcc
        return true;
    }

    public Operand getOperand() {
        return op;
    }
}
