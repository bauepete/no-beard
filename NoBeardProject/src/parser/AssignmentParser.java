/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import scanner.Scanner.Symbol;
import symboltable.IllegalOperand;

/**
 *
 * @author peter
 */
public class AssignmentParser extends ParserForAssignment {

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
}
