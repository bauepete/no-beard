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

import machine.InstructionSet.Instruction;
import scanner.Scanner.Symbol;
import symboltable.SymbolTableEntry;

/**
 *
 * @author peter
 */
public class BlockParser extends Parser {

    private final SymbolTableEntry obj;

    private int incAddress = 0;

    public BlockParser() {
        this.obj = null;
    }

    @Override
    protected void parseSpecificPart() {
        parseSymbol(Symbol.DO);
        sem(() -> {
            sym.newBlock();
            code.emit(Instruction.INC);
            code.emit(0);
            incAddress = code.getPc() - 2;
        });
        while (parsingWasSuccessful() && getScanner().getCurrentToken().getSymbol() != Symbol.DONE) {
            parseStatement();
        }
        sem(() -> {
            code.fixup(incAddress, sym.getCurrBlock().getSize());
            sym.endBlock();
        });
        parseSymbol(Symbol.DONE);
    }

    private void parseStatement() {
        StatementParser statementParser = ParserFactory.create(StatementParser.class);
        parseSymbol(statementParser);
    }
}
