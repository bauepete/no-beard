/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import error.ErrorHandler;
import nbm.Nbm;
import nbm.CodeGenerator;
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

    private SymListEntry unitObj;
    
    public NoBeardParser(Scanner s, SymListManager sym, CodeGenerator c, ErrorHandler e) {
        super(s, sym, c, e);
    }

    @Override
    public boolean parseOldStyle() {
        if (!tokenIsA(Symbol.UNIT)) {
            return false;
        }

        int name = ident();
        if (name == NOIDENT) {
            return false;
        }

        if (!tokenIsA(Symbol.SEMICOLON)) {
            return false;
        }

        sem(() -> {
            sym.newUnit(name);
            unitObj = sym.findObject(name);
        });

        if (!block(unitObj)) {
            return false;
        }

        sem(() -> code.emitOp(Nbm.Opcode.HALT));

        int name1 = ident();
        if (name1 == NOIDENT) {
            return false;
        }
        if (!tokenIsA(Symbol.SEMICOLON)) {
            return false;
        }

        // cc
        if (name != name1) {
            NameManager n = scanner.getNameManager();
            String[] pList = {n.getStringName(name), n.getStringName(name1)};
            getErrorHandler().raise(new error.Error(error.Error.ErrorType.BLOCK_NAME_MISSMATCH, pList));
            return false;
        }
        // end cc
        return true;
    }

    private boolean block(SymListEntry obj) {
        BlockParser blockP = new BlockParser(scanner, sym, code, obj, getErrorHandler());
        return blockP.parseOldStyle();
    }

    @Override
    public void parseSpecificPart() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
