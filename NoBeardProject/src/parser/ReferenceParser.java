/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import scanner.Scanner.Symbol;
import symlist.Operand;
import symlist.Operand.OperandKind;
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
    public void parseSpecificPart() {
        parseSymbol(Symbol.IDENTIFIER);
        sem(() -> foundSymbolListEntry = sym.findObject(getLastParsedToken().getValue()));
        where(foundSymbolListEntry != null && foundSymbolListEntry.getKind() == OperandKind.VARIABLE, () -> getErrorHandler().throwOperandOfKindExpected("Variable or parameter"));
        
        sem(() -> op = foundSymbolListEntry.createOperand());
    }
}
