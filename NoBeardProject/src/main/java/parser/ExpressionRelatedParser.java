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
import symboltable.Operand;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public abstract class ExpressionRelatedParser extends OperandExportingParser {

    protected Instruction opCode;
    protected int positionOfLastBooleanOperatorJump;

    protected Operand op2;

    @Override
    protected void parseSpecificPart() {
        parseLeadingSign();
        OperandExportingParser subExpressionParser = createSubExpressionParser();
        parseSymbol(subExpressionParser);
        prepareExportedOperand(subExpressionParser);

        while (currentTokenIsAValidOperator()) {
            parseOperator();
            if (operatorIsBoolean()) {
                handleBooleanSubExpression(subExpressionParser);
            } else {
                handleIntegerSubExpression(subExpressionParser, getLastParsedToken().getSymbol().toString());
            }
        }
        fixBooleanOperatorChainIfNecessary();
    }

    protected abstract void parseLeadingSign();

    protected abstract OperandExportingParser createSubExpressionParser();

    protected abstract void prepareExportedOperand(OperandExportingParser subExpressionParser);

    protected abstract boolean currentTokenIsAValidOperator();

    protected void parseOperator() {
        Scanner.Symbol currentMulOp = getScanner().getCurrentToken().getSymbol();
        parseSymbol(currentMulOp);
        opCode = OperatorToOpCodeMap.getOpCode(currentMulOp);
    }

    protected abstract boolean operatorIsBoolean();

    protected abstract void handleBooleanSubExpression(OperandExportingParser subExpressionParser);

    protected abstract void handleIntegerSubExpression(OperandExportingParser subExpressionParser, String usedOperator);

    private void fixBooleanOperatorChainIfNecessary() {
        sem(() -> {
            if (positionOfLastBooleanOperatorJump != 0) {
                fixBooleanOperatorChain();
            }
        });
    }

    protected abstract void fixBooleanOperatorChain();

    protected void checkOperandForBeing(final Operand op, final Operand.Type requestedType, String usedOperator) {
        where(op != null && op.getType() == requestedType,
                () -> getErrorHandler().throwOperatorOperandTypeMismatch(usedOperator, requestedType.toString()));
    }

    protected void fetchOperand(OperandExportingParser factorParser) {
        sem(() -> op2 = factorParser.getOperand());
    }

    protected final void emitCodeForLoadingValue() {
        sem(() -> exportedOperand = op2.emitLoadVal(code));
    }
}
