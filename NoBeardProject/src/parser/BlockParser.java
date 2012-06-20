/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import nbm.Code;
import nbm.Nbm.Opcode;
import scanner.Scanner;
import scanner.Scanner.Symbol;
import symlist.Operand.OperandKind;
import symlist.SymListEntry;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class BlockParser extends Parser {

    private SymListEntry obj;

    public BlockParser(Scanner scanner, SymListManager sym, Code code, SymListEntry obj) {
        super(scanner, sym, code);
        this.obj = obj;
    }

    @Override
    public boolean parse() {
        if (!tokenIsA(Symbol.DOSY)) {
            return false;
        }

        // sem
        int incAddr = 0;
        if (isNamedBlock()) {
            sym.defineFuncStart(obj, code.getPc());
            incAddr = code.getPc() + 1;
            code.emitOp(Opcode.INC);
            code.emitHalfWord(0);  // tmp address will be fixed later
        }
        // endsem

        if (!statSeq()) {
            return false;
        }

        // sem
        if (isNamedBlock()) {
            sym.fixINC(incAddr, obj);
        }
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
    
    private boolean isNamedBlock() {
        return (obj.isNamedBlockEntry());
    }
}
