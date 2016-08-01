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
public class PutStatParser extends Parser {

    ExpressionParser parserForOutputExpression;

    private Operand operandForOutputValue;
    private Operand operandForColumnWidth;

    public PutStatParser(Scanner s, SymbolTable sym, CodeGenerator c, ErrorHandler e) {
        super(s, sym, c, e);
    }

    public PutStatParser() {

    }

    @Override
    protected void parseSpecificPart() {
        if (scanner.getCurrentToken().getSymbol() == Symbol.PUT) {
            parsePut();
        } else {
            parsePutln();
        }
    }

    private void parsePut() {
        parseSymbol(Symbol.PUT);
        parseSymbol(Symbol.LPAR);
        parseExpression();
        fetchOperandForOutput();
        if (scanner.getCurrentToken().getSymbol() == Symbol.COMMA) {
            parseColumnWidth();
        }
        parseSymbol(Symbol.RPAR);
        emitCodeForPut();
        parseSymbol(Symbol.SEMICOLON);
    }

    private void parseExpression() {
        parserForOutputExpression = ParserFactory.create(ExpressionParser.class);
        parseSymbol(parserForOutputExpression);
    }

    private void fetchOperandForOutput() {
        sem(() -> operandForOutputValue = parserForOutputExpression.getOperand());
        where(isOperandToPut(operandForOutputValue), () -> getErrorHandler().throwOperandOfKindExpected("Integer, char or string"));
    }

    private final Type[] OUTPUTABLE_OPERANDS = {
        Type.SIMPLECHAR, Type.SIMPLEINT, Type.ARRAYCHAR
    };

    private boolean isOperandToPut(Operand op) {
        for (Type operandType : OUTPUTABLE_OPERANDS) {
            if (operandType == op.getType()) {
                return true;
            }
        }
        return false;
    }

    private void parseColumnWidth() {
        parseSymbol(Symbol.COMMA);
        parseExpression();
        fetchOperandForColumnWidth();
    }

    private void fetchOperandForColumnWidth() {
        sem(() -> operandForColumnWidth = parserForOutputExpression.getOperand());
        where(operandForColumnWidth.getType() == Type.SIMPLEINT, () -> getErrorHandler().throwOperandOfKindExpected("Integer"));
    }

    private static final int FOR_INT = 0;
    private static final int FOR_CHAR = 1;
    private static final int FOR_STRING = 2;
     private static final int FOR_NEW_LINE = 3;
   private void emitCodeForPut() {
        sem(() -> {
            switch (operandForOutputValue.getType()) {
                case SIMPLEINT:
                    operandForOutputValue.emitLoadVal(code);
                    emitColumnWidth(0);
                    emitPutStatement(FOR_INT);
                    break;

                case SIMPLECHAR:
                    operandForOutputValue.emitLoadVal(code);
                    emitColumnWidth(0);
                    emitPutStatement(FOR_CHAR);
                    break;

                default:        // operand needs to be a string
                    operandForOutputValue.emitLoadAddr(code);
                    emitStringLength();
                    emitColumnWidth(operandForOutputValue.getSize());
                    emitPutStatement(FOR_STRING);
                    break;
            }
        });
    }

    private void emitColumnWidth(final int width) {
        code.emitOp(Opcode.LIT);
        code.emitHalfWord(width);
    }

    private void emitPutStatement(final int putStyle) {
        code.emitOp(Opcode.PUT);
        code.emitByte((byte) putStyle);
    }

    private void emitStringLength() {
        code.emitOp(Opcode.LIT);
        code.emitHalfWord(operandForOutputValue.getSize());
    }

    private void parsePutln() {
        parseSymbol(Symbol.PUTLN);
        sem(() -> emitPutStatement(FOR_NEW_LINE));
        parseSymbol(Symbol.SEMICOLON);
    }

    @Override
    public boolean parseOldStyle() {
        boolean isParsedCorrectly = false;

        return isParsedCorrectly;
    }
}
