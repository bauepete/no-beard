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
import symboltable.Operand;
import symboltable.Operand.Type;
import symboltable.SymbolTable;

/**
 *
 * @author peter
 */
public class IfParser extends Parser {

    public IfParser(Scanner scanner, SymbolTable sym, CodeGenerator code, ErrorHandler e) {
        super(scanner, sym, code, e);
    }
    
    public IfParser() {
        
    }

    @Override
    protected void parseSpecificPart() {
        BlockParser blockParser = ParserFactory.create(BlockParser.class);
        parseIfPart(blockParser);
        
        if (ParserFactory.getScanner().getCurrentToken().getSymbol() == Symbol.ELSE) {
            parseElsePart(blockParser);
        }
    }

    private void parseIfPart(BlockParser blockParser) {
        parseSymbol(Symbol.IF);
        ExpressionParser expressionParser = ParserFactory.create(ExpressionParser.class);
        parseSymbol(expressionParser);
        parseSymbol(blockParser);
    }
    
    private void parseElsePart(BlockParser blockParser) {
        parseSymbol(Symbol.ELSE);
        parseSymbol(blockParser);
    }

    @Override
    public boolean parseOldStyle() {
        if (!tokenIsA(Symbol.IF)) {
            return false;
        }
        ExpressionParser exprP = new ExpressionParser(scanner, sym, code, getErrorHandler());
        if (!exprP.parseOldStyle()) {
            return false;
        }
        Operand op = exprP.getOperand();
        // cc
        if (!operandIsA(op, Type.SIMPLEBOOL)) {
            return false;
        }
        // sem
        op.emitLoadVal(code);
        code.emitOp(Opcode.FJMP);
        code.emitHalfWord(0);
        int jumpAddr = code.getPc() - 2;
        
        sym.newBlock();
        // endsem
        
        BlockParser blockP = new BlockParser(scanner, sym, code, sym.getCurrBlock(), getErrorHandler());
        if (!blockP.parseOldStyle()) {
            return false;
        }
        // sem
        sym.endBlock();
        // endsem
        
        if (scanner.getCurrentToken().getSymbol() == Symbol.ELSE) {
            if (!tokenIsA(Symbol.ELSE)) {
                return false;
            }
            
            // sem
            code.emitOp(Opcode.JMP);
            code.emitHalfWord(0);
            code.fixup(jumpAddr, code.getPc());
            jumpAddr = code.getPc() - 2;
            
            sym.newBlock();
            // endsem
            
            if (!blockP.parseOldStyle()) {
                return false;
            }
            
            // sem
            sym.endBlock();
        }
        code.fixup(jumpAddr, code.getPc());
        return true;
    }
}
