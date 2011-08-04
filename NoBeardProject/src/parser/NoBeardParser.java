/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import error.ErrorHandler;
import error.semerr.BlockNameMismatch;
import nbm.Nbm;
import nbm.Code;
import scanner.NameManager;
import scanner.Scanner;
import scanner.Scanner.Symbol;
import symlist.SymListEntry;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class NoBeardParser extends Parser {

    public NoBeardParser(Scanner s, SymListManager sym, Code c) {
        super(s, sym, c);
    }

    @Override
    public boolean parse() {
        if (!tokenIsA(Symbol.UNITSY)) {
            return false;
        }

        int name = ident();
        if (name == NOIDENT) {
            return false;
        }

        if (!tokenIsA(Symbol.SEMICOLONSY)) {
            return false;
        }

        // sem
        sym.newUnit(name);
        SymListEntry unitObj = sym.findObject(name);
        // endsem

        if (!block(unitObj)) {
            return false;
        }

        // sem
        code.emitOp(Nbm.Opcode.HALT);
        // endsem

        int name1 = ident();
        if (name1 == NOIDENT) {
            return false;
        }
        if (!tokenIsA(Symbol.SEMICOLONSY)) {
            return false;
        }

        // cc
        if (name != name1) {
            NameManager n = scanner.getNameManager();
            ErrorHandler.getInstance().raise(new BlockNameMismatch(n.getStringName(name), n.getStringName(name1)));
            return false;
        }
        // end cc
        return true;
    }

    private boolean block(SymListEntry obj) {
        if (!tokenIsA(Symbol.DOSY)) {
            return false;
        }

        // sem
        sym.defineProcStart(obj, code.getPc());
        int incAddr = code.getPc() + 1;
        code.emitOp(Nbm.Opcode.INC);
        code.emitHalfWord(0);  // tmp address will be fixed later
        // endsem

        if (!statSeq()) {
            return false;
        }

        // sem
        sym.fixINC(incAddr, obj);
        // endsem

        if (!tokenIsA(Symbol.DONESY)) {
            return false;
        }
        return true;
    }

    private boolean statSeq() {
        StatParser statP = new StatParser(scanner, sym, code);
        if (!statP.parse()) {
            return false;
        }
        
        while (scanner.getCurrentToken().getSy() != Symbol.DONESY) {
            if (!statP.parse()) {
                return false;
            }
        }
        return true;
    }
}
