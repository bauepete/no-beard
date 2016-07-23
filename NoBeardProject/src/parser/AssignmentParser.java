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
import symboltable.Operand;
import symboltable.SymbolTable;

/**
 *
 * @author peter
 */
public class AssignmentParser extends Parser {

    private Operand destOp;
    private Operand destAddrOp;
    private Operand srcOp;
    
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
        sem(() -> {
            destOp = referenceParser.getOperand();
            destAddrOp = destOp.emitLoadAddr(code);
        });
        parseSymbol(Symbol.ASSIGN);
        ExpressionParser expressionParser = ParserFactory.create(ExpressionParser.class);
        parseSymbol(expressionParser);
        
        sem(() -> srcOp = expressionParser.getOperand());
        where(srcOp.getType() == destOp.getType() && srcOp.getSize() == destOp.getSize(),
                () -> getErrorHandler().throwOperandsAreIncompatible(srcOp.getSize(), srcOp.getType(), destOp.getSize(), destOp.getType()));
        sem(() -> srcOp.emitAssign(code, destAddrOp));
        
        parseSymbol(Symbol.SEMICOLON);
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
