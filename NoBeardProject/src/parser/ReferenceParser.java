/*
 * Copyright ©2015, 2016. Created by P. Bauer (p.bauer@htl-leonding.ac.at),
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

import scanner.Scanner.Symbol;
import symlist.Operand;
import symlist.Operand.Kind;
import symlist.SymListEntry;

/**
 *
 * @author peter
 */
public class ReferenceParser extends Parser {

    private SymListEntry foundSymbolListEntry;
    private Operand op;

    @Override
    public boolean parseOldStyle() {
        return true;
    }

    public Operand getOperand() {
        return op;
    }

    @Override
    protected void parseSpecificPart() {
        parseSymbol(Symbol.IDENTIFIER);
        sem(() -> foundSymbolListEntry = sym.findObject(getLastParsedToken().getValue()));
        where(foundSymbolListEntry != null && foundSymbolListEntry.getKind() == Kind.VARIABLE, () -> getErrorHandler().throwOperandOfKindExpected("Variable or parameter"));
        
        sem(() -> op = foundSymbolListEntry.createOperand());
        
        if (scanner.getCurrentToken().getSy() == Symbol.LBRACKET) {
            parseSymbol(Symbol.LBRACKET);
            ExpressionParser p = ParserFactory.create(ExpressionParser.class);
            parseSymbol(p);
            parseSymbol(Symbol.RBRACKET);
        }
    }
}
