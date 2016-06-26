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

import error.ErrorHandler;
import nbm.CodeGenerator;
import nbm.Nbm;
import nbm.Nbm.Opcode;
import scanner.Scanner;
import scanner.Scanner.Symbol;
import symboltable.Operand.Type;
import symboltable.SymListManager;

/**
 *
 * @author peter
 */
public class AddExpressionParser extends ExpressionRelatedParser {

    public AddExpressionParser(Scanner s, SymListManager sym, CodeGenerator c, ErrorHandler eh) {
        super();
    }

    public AddExpressionParser() {

    }

    @Override
    protected OperandExportingParser createSubExpressionParser() {
        return ParserFactory.create(TermParser.class);
    }

    @Override
    protected boolean operatorIsBoolean() {
        return getLastParsedToken().getSy() == Scanner.Symbol.OR;
    }

    @Override
    protected void prepareExportedOperand(OperandExportingParser termParser) {
        sem(() -> op2 = termParser.getOperand());
        
        where(opCode == null || op2.getType() == Type.SIMPLEINT,
                () -> getErrorHandler().throwOperatorOperandTypeMismatch("+ or -", "int"));
        sem(() -> {
            if (opCode == Nbm.Opcode.SUB) {
                emitCodeForLoadingValue();
                code.emitOp(Opcode.NEG);
            } else {
                exportedOperand = op2;
            }
        });
    }

    @Override
    protected void parseLeadingSign() {
        if (currentTokenIsAValidOperator()) {
            parseOperator();
        }
    }

    @Override
    protected boolean currentTokenIsAValidOperator() {
        Symbol sy = scanner.getCurrentToken().getSy();
        return (sy == Symbol.PLUS || sy == Symbol.MINUS || sy == Symbol.OR);
    }

    @Override
    protected void handleBooleanSubExpression(OperandExportingParser termParser) {
        checkOperandForBeing(op2, Type.SIMPLEBOOL, "or");
        maintainBooleanOperatorChain();
        parseSymbol(termParser);
        sem(() -> op2 = termParser.getOperand());
        checkOperandForBeing(op2, Type.SIMPLEBOOL, "+ or -");
        emitCodeForLoadingValue();
    }

    private void maintainBooleanOperatorChain() {
        sem(() -> {
            exportedOperand.emitLoadVal(code);
            code.emitOp(Opcode.TJMP);
            code.emitHalfWord(positionOfLastBooleanOperatorJump);
            positionOfLastBooleanOperatorJump = code.getPc() - 2;
        });
    }

    @Override
    protected void fixBooleanOperatorChain() {
        code.emitOp(Opcode.JMP);
        code.emitHalfWord(code.getPc() + 5);
        while (positionOfLastBooleanOperatorJump != 0) {
            int next = code.getCodeHalfWord(positionOfLastBooleanOperatorJump);
            code.fixup(positionOfLastBooleanOperatorJump, code.getPc());
            positionOfLastBooleanOperatorJump = next;
        }
        code.emitOp(Opcode.LIT);
        code.emitHalfWord(1);
    }

    @Override
    protected void handleIntegerSubExpression(OperandExportingParser termParser, String usedOperator) {
        checkOperandForBeing(exportedOperand, Type.SIMPLEINT, usedOperator);
        sem(() -> exportedOperand.emitLoadVal(code));
        parseSymbol(termParser);
        fetchOperand(termParser);
        emitCodeForLoadingValue();
        sem(() -> code.emitOp(opCode));
    }

    @Override
    public boolean parseOldStyle() {
        return true;
    }
}
