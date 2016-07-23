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
public class TermParser extends ExpressionRelatedParser {

    public TermParser(Scanner s, SymbolTable sym, CodeGenerator c, ErrorHandler e) {
        super();
    }

    public TermParser() {
        this.exportedOperand = new Operand(Operand.Kind.ILLEGAL, Type.VOID, 0, 0, 0);

    }

    @Override
    protected OperandExportingParser createSubExpressionParser() {
        return ParserFactory.create(FactorParser.class);
    }

    @Override
    protected boolean operatorIsBoolean() {
        return getLastParsedToken().getSy() == Symbol.AND;
    }
    
    @Override
    protected void parseLeadingSign() {
        
    }

    @Override
    protected void prepareExportedOperand(OperandExportingParser factorParser) {
        sem(() -> exportedOperand = factorParser.getOperand());
    }

    @Override
    protected boolean currentTokenIsAValidOperator() {
        Symbol sy = scanner.getCurrentToken().getSy();
        return (sy == Symbol.TIMES || sy == Symbol.DIV || sy == Symbol.MOD || sy == Symbol.AND);
    }

    @Override
    protected void handleBooleanSubExpression(OperandExportingParser factorParser) {
        checkOperandForBeing(exportedOperand, Type.SIMPLEBOOL, "and");
        sem(() -> exportedOperand.emitLoadVal(code));
        maintainBooleanOperatorChain();
        parseSymbol(factorParser);
        fetchOperand(factorParser);
        checkOperandForBeing(op2, Type.SIMPLEBOOL, "and");
        emitCodeForLoadingValue();
    }

    private void maintainBooleanOperatorChain() {
        sem(() -> {
            code.emitOp(Opcode.FJMP);
            code.emitHalfWord(positionOfLastBooleanOperatorJump);
            positionOfLastBooleanOperatorJump = code.getPc() - 2;
        });
    }

    @Override
    protected void handleIntegerSubExpression(OperandExportingParser factorParser, String usedOperator) {
        checkOperandForBeing(exportedOperand, Type.SIMPLEINT, usedOperator);
        sem(() -> exportedOperand.emitLoadVal(code));
        parseSymbol(factorParser);
        fetchOperand(factorParser);
        checkOperandForBeing(op2, Type.SIMPLEINT, usedOperator);
        emitCodeForLoadingValue();
        sem(() -> code.emitOp(opCode));
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
        code.emitHalfWord(0);
    }

    @Override
    public boolean parseOldStyle() {
        return true;
    }
}
