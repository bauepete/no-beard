/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import scanner.Scanner;
import symboltable.Operand;
import symboltable.SymbolTableEntry;

/**
 *
 * @author peter
 */
public class GetParser extends Parser {

    private SymbolTableEntry foundSymbolListEntry;
    
    @Override
    protected void parseSpecificPart() {
        parseSymbol(Scanner.Symbol.GET);
        parseSymbol(Scanner.Symbol.LPAR);
        parseSymbol(Scanner.Symbol.IDENTIFIER);
//        sem(() -> {
//            sem(() -> foundSymbolListEntry = sym.findObject(getLastParsedToken().getValue()));
//        });
//        where(foundSymbolListEntry.getKind() == Operand.Kind.VARIABLE &&
//                foundSymbolListEntry.getType() == Operand.Type.SIMPLEINT,
//                () -> getErrorHandler().throwOperandOfKindExpected("Variable")
//        );
//        sem(() -> {
//            co
//        });
        parseSymbol(Scanner.Symbol.COMMA);
        parseSymbol(Scanner.Symbol.IDENTIFIER);
        parseSymbol(Scanner.Symbol.RPAR);
        parseSymbol(Scanner.Symbol.SEMICOLON);
    }
    
}
