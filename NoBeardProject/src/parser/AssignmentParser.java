/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import error.ErrorHandler;
import nbm.CodeGenerator;
import scanner.Scanner;
import scanner.Scanner.Symbol;
import symboltable.IllegalOperand;
import symboltable.SymbolTable;

/**
 *
 * @author peter
 */
public class AssignmentParser extends ParserForAssignment {
    /**
     * 
     * @param s
     * @param sym
     * @param c
     * @param e 
     * @deprecated 
     */
    public AssignmentParser(Scanner s, SymbolTable sym, CodeGenerator c, ErrorHandler e) {
        super();
    }

    public AssignmentParser() {
        destOp = new IllegalOperand();
        destAddrOp = new IllegalOperand();
        srcOp = new IllegalOperand();
    }

    @Override
    public void parseSpecificPart() {
        ReferenceParser referenceParser = ParserFactory.create(ReferenceParser.class);
        parseSymbol(referenceParser);
        prepareLeftHandSide(referenceParser);
        parseSymbol(Symbol.ASSIGN);
        parseRightHandSide();
        parseSymbol(Symbol.SEMICOLON);
    }

    private void prepareLeftHandSide(ReferenceParser referenceParser) {
        sem(() -> {
            destOp = referenceParser.getOperand();
            destAddrOp = destOp.emitLoadAddr(code);
        });
    }

    /**
     * 
     * @return 
     * @deprecated 
     */
    @Override
    public boolean parseOldStyle() {
        return true;
    }
}
