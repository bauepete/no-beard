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

import machine.InstructionSet.Instruction;
import scanner.Scanner.Symbol;
import symboltable.Operand;

/**
 *
 * @author peter
 */
public class IfParser extends Parser {

    private int addressOfSkipIfJump = 0;
    private int addressOfSkipElseJump = 0;
    
    @Override
    protected void parseSpecificPart() {
        BlockParser blockParser = ParserFactory.create(BlockParser.class);
        parseIfPart(blockParser);
        
        if (ParserFactory.getScanner().getCurrentToken().getSymbol() == Symbol.ELSE) {
            parseElsePart(blockParser);
        } else {
            setJumpDestintationToHere(addressOfSkipIfJump);
        }
    }

    private void parseIfPart(BlockParser blockParser) {
        parseSymbol(Symbol.IF);
        ExpressionParser expressionParser = ParserFactory.create(ExpressionParser.class);
        parseSymbol(expressionParser);
        checkExpressionForBeingBool(expressionParser);
        prepareJumpToSkipIfBlock();
        parseSymbol(blockParser);
        
    }

    private void checkExpressionForBeingBool(ExpressionParser expressionParser) {
        where(expressionParser.getOperand().getType() == Operand.Type.SIMPLEBOOL, () -> getErrorHandler().throwOperatorOperandTypeMismatch("if", "bool"));
    }

    private void prepareJumpToSkipIfBlock() {
        sem(() -> {
            code.emit(Instruction.FJMP);
            code.emit(0);
            addressOfSkipIfJump = code.getPc() - 2;
        });
    }
    
    private void parseElsePart(BlockParser blockParser) {
        prepareJumpToSkipElseBlock();
        setJumpDestintationToHere(addressOfSkipIfJump);
        parseSymbol(Symbol.ELSE);
        parseSymbol(blockParser);
        setJumpDestintationToHere(addressOfSkipElseJump);
    }

    private void prepareJumpToSkipElseBlock() {
        sem(() -> {
            code.emit(Instruction.JMP);
            code.emit(0);
            addressOfSkipElseJump = code.getPc() - 2;
        });
    }

    private void setJumpDestintationToHere(final int addressOfJumpSource) {
        sem(() -> code.fixup(addressOfJumpSource, code.getPc()));
    }
}
