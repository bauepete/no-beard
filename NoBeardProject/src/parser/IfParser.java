/*
 * Copyright ©2012 - 2016. Created by P. Bauer (p.bauer@htl-leonding.ac.at),
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
        super();
    }
    
    public IfParser() {
        
    }

    private int addressOfIfFalseJump = 0;
    
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
        where(expressionParser.getOperand().getType() == Operand.Type.SIMPLEBOOL, () -> getErrorHandler().throwOperatorOperandTypeMismatch("if", "bool"));
        sem(() -> {
            code.emitOp(Opcode.FJMP);
            code.emitHalfWord(0);
            addressOfIfFalseJump = code.getPc() - 2;
        });
        parseSymbol(blockParser);
        sem(() -> code.fixup(addressOfIfFalseJump, code.getPc()));
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
