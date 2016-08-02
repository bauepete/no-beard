/*
 * Copyright ©2011 - 2016. Created by P. Bauer (p.bauer@htl-leonding.ac.at),
 * Department of Informatics and Media Technique, HTBLA Leonding,
 * Limesstr. 12 - 14, 4060 Leonding, AUSTRIA. All Rights Reserved. Permission
 * to use, copy, modify, and distribute this software and its documentation
 * for educational, research, and not-for-profit purposes, without fee and
 * without a signed licensing agreement, is hereby granted, provided that the
 * above copyright notice, this paragraph and the following two paragraphs
 * appear in all copies, modifications, and distributions. Contact the Head of
 * Informatics and Media Technique, HTBLA Leonding, Limesstr. 12 - 14,
 * 4060 Leonding, Austria, for commercial licensing opportunities.
 * 
 * IN NO EVENT SHALL HTBLA LEONDING BE LIABLE TO ANY PARTY FOR DIRECT,
 * INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST
 * PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION,
 * EVEN IF HTBLA LEONDING HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * HTBLA LEONDING SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE. THE SOFTWARE AND ACCOMPANYING DOCUMENTATION, IF ANY,
 * PROVIDED HEREUNDER IS PROVIDED "AS IS". HTBLA LEONDING HAS NO OBLIGATION
 * TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 */
package parser;

import error.ErrorHandler;
import nbm.Nbm;
import nbm.CodeGenerator;
import scanner.NameManager;
import scanner.Scanner;
import scanner.Scanner.Symbol;
import symboltable.SymbolTableEntry;
import symboltable.SymbolTable;

/**
 *
 * @author peter
 */
public class NoBeardParser extends Parser {

    private SymbolTableEntry unitObj;
    
    public NoBeardParser(Scanner s, SymbolTable sym, CodeGenerator c, ErrorHandler e) {
        super(s, sym, c, e);
    }
    
    public NoBeardParser() {
        
    }

    @Override
    protected void parseSpecificPart() {
        parseSymbol(Symbol.UNIT);
        int name = parseIdentifier();
        parseSymbol(Symbol.SEMICOLON);
        BlockParser blockParser = ParserFactory.create(BlockParser.class);
        parseSymbol(blockParser);
        int name1 = parseIdentifier();
        parseSymbol(Symbol.SEMICOLON);
    }

    @Override
    public boolean parseOldStyle() {
        if (!tokenIsA(Symbol.UNIT)) {
            return false;
        }

        int name = parseIdentifier();
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

        int name1 = parseIdentifier();
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

    private boolean block(SymbolTableEntry obj) {
        BlockParser blockP = new BlockParser(scanner, sym, code, obj, getErrorHandler());
        return blockP.parseOldStyle();
    }
}
