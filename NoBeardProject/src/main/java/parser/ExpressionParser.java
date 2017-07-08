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

import machine.InstructionSet.Instruction;
import scanner.Scanner;
import scanner.Scanner.Symbol;
import symboltable.Operand;
import symboltable.ValueOnStackOperand;

/**
 *
 * @author peter
 */
public class ExpressionParser extends OperandExportingParser {

    private byte ror;
    Operand op2;

    @Override
    protected void parseSpecificPart() {
        OperandExportingParser simExprP = parseLeftHandSide();
        if (currentTokenIsARelationalOperator()) {
            parseRightHandSide(simExprP);
        }
    }

    private OperandExportingParser parseLeftHandSide() {
        OperandExportingParser simExprP = ParserFactory.create(AddExpressionParser.class);
        parseSymbol(simExprP);
        sem(() -> exportedOperand = simExprP.getOperand());
        return simExprP;
    }
    private boolean currentTokenIsARelationalOperator() {
        Scanner.Symbol currentSymbol = getScanner().getCurrentToken().getSymbol();
        return (currentSymbol == Symbol.LTH
                || currentSymbol == Symbol.GTH
                || currentSymbol == Symbol.LEQ
                || currentSymbol == Symbol.GEQ
                || currentSymbol == Symbol.EQUALS
                || currentSymbol == Symbol.NEQ);
    }

    private void parseRightHandSide(OperandExportingParser simExprP) {
        parseOperator();
        emitCodeForLoadingOperand();

        parseSymbol(simExprP);
        sem(() -> op2 = simExprP.getOperand());
        checkOperandsForBeingCompatible();

        emitCodeForComparing();
        sem(() -> exportedOperand = new ValueOnStackOperand(Operand.Type.SIMPLEBOOL, 4, exportedOperand.getValaddr(), exportedOperand.getCurrLevel()));
    }
    
    protected void parseOperator() {
        Scanner.Symbol currentMulOp = getScanner().getCurrentToken().getSymbol();
        parseSymbol(currentMulOp);
        ror = OperatorToOpCodeMap.getOperand(currentMulOp);
    }

    private void emitCodeForLoadingOperand() {
        sem(() -> {
            switch (exportedOperand.getType()) {
                case SIMPLEBOOL:
                case SIMPLECHAR:
                case SIMPLEINT:
                    exportedOperand.emitLoadVal(code);
                    break;

                case ARRAYBOOL:
                case ARRAYCHAR:
                case ARRAYINT:
                    exportedOperand.emitLoadAddr(code);
                    break;
            }
        });
    }

    private void checkOperandsForBeingCompatible() {
        boolean operandsSizeIsUndefined = exportedOperand.getSize() == Operand.UNDEFSIZE || op2.getSize() == Operand.UNDEFSIZE;
        boolean typesAreCompatible = exportedOperand.getType() == op2.getType();
        boolean sizesAreCompatible = exportedOperand.getSize() == op2.getSize();

        where(!operandsSizeIsUndefined && typesAreCompatible && sizesAreCompatible,
                () -> getErrorHandler().throwOperandsAreIncompatible(exportedOperand.getSize(), exportedOperand.getType().toString(), op2.getSize(), op2.getType().toString()));
    }

    private void emitCodeForComparing() {
        sem(() -> {
            switch (op2.getType()) {
                case SIMPLEBOOL:
                case SIMPLECHAR:
                case SIMPLEINT:
                    op2.emitLoadVal(code);
                    code.emit(Instruction.REL);
                    code.emit(ror);
                    break;

                default:
                    getErrorHandler().throwOperatorOperandTypeMismatch("Relational operators", "bool, char or int");
                    break;
            }
        });
    }
}
