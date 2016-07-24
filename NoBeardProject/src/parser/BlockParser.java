/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import error.ErrorHandler;
import nbm.CodeGenerator;
import nbm.Nbm.Opcode;
import scanner.Scanner;
import scanner.Scanner.Symbol;
import symboltable.SymbolTableEntry;
import symboltable.SymbolTable;

/**
 *
 * @author peter
 */
public class BlockParser extends Parser {

    private final SymbolTableEntry obj;

    public BlockParser(Scanner scanner, SymbolTable sym, CodeGenerator code, SymbolTableEntry obj, ErrorHandler e) {
        super(scanner, sym, code, e);
        this.obj = obj;
    }

    public BlockParser() {
        this.obj = null;
    }

    @Override
    protected void parseSpecificPart() {
        parseSymbol(Symbol.DO);
        parseSymbol(Symbol.DONE);
    }

    @Override
    public boolean parseOldStyle() {
        if (!tokenIsA(Symbol.DO)) {
            return false;
        }

        // sem
        sym.defineFuncStart(obj, code.getPc());
        int incAddr = code.getPc() + 1;
        code.emitOp(Opcode.INC);
        code.emitHalfWord(0);  // tmp address will be fixed later
        // endsem

        if (!statSeq()) {
            return false;
        }

        // sem
        sym.fixINC(incAddr, obj);
        // endsem

        if (!tokenIsA(Symbol.DONE)) {
            return false;
        }
        return true;
    }

    private boolean statSeq() {
        StatParser statP = new StatParser(scanner, sym, code, getErrorHandler());
        if (!statP.parseOldStyle()) {
            return false;
        }

        while (scanner.getCurrentToken().getSymbol() != Symbol.DONE) {
            if (!statP.parseOldStyle()) {
                return false;
            }
        }
        return true;
    }

    private boolean isNamedBlock() {
        return (obj.isNamedBlockEntry());
    }
}
