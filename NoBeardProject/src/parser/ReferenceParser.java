/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import error.Error;
import error.Error.ErrorType;
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
        int name = ident();
        if (name == NOIDENT) {
            return false;
        }
        // sem
        SymListEntry obj = sym.findObject(name);
        // endsem

        // cc
        if (obj.getKind() != OperandKind.VARIABLE) {
            getErrorHandler().raise(new Error(ErrorType.OPERAND_KIND_EXPECTED, "Variable"));
            return false;
        }
        
        // sem
        op = obj.createOperand();
        // endsem
        
        if (op.getKind() != OperandKind.VARIABLE) {
            getErrorHandler().raise(new Error(ErrorType.OPERAND_KIND_EXPECTED, "Variable"));
            return false;
        }
        // endcc
        return true;
    }

    public Operand getOperand() {
        return op;
    }

    @Override
    public void parseSpecificPart() {
        parseSymbol(Symbol.IDENTIFIER);
        sem(() -> foundSymbolListEntry = sym.findObject(scanner.getCurrentToken().getValue()));
        where(foundSymbolListEntry.getKind() == OperandKind.VARIABLE, () -> errorHandler.throwOperandOfKindExpected("Variable or parameter"));
        
        sem(() -> op = foundSymbolListEntry.createOperand());
    }

    private void parseSymbol(Symbol symbol) {
        if (parsingWasSuccessfulUntilNow) {
            parsingWasSuccessfulUntilNow = scanner.getCurrentToken().getSy() == symbol;
            if (!parsingWasSuccessfulUntilNow)
                errorHandler.throwSymbolExpectedError(symbol.toString(), scanner.getCurrentToken().getSy().toString());
        }
    }
}
