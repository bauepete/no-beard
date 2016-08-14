/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import machine.InstructionSet.Instruction;
import scanner.Scanner.Symbol;
import symboltable.ConstantOperand;
import symboltable.IllegalOperand;
import symboltable.Operand;
import symboltable.Operand.Type;

/**
 *
 * @author peter
 */
public class PutParser extends Parser {

    ExpressionParser parserForOutputExpression;

    private Operand operandForOutputValue = new IllegalOperand();
    private Operand operandForColumnWidth = new IllegalOperand();

    @Override
    protected void parseSpecificPart() {
        if (getScanner().getCurrentToken().getSymbol() == Symbol.PUT) {
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
        if (getScanner().getCurrentToken().getSymbol() == Symbol.COMMA) {
            parseColumnWidth();
        } else {
            fixOperandForColumnWidth();
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

    private void fixOperandForColumnWidth() {
        if (operandForOutputValue.getType() == Type.ARRAYCHAR) {
            operandForColumnWidth = new ConstantOperand(Type.SIMPLEINT, 4, operandForOutputValue.getSize(), 0);
        } else {
            operandForColumnWidth = new ConstantOperand(Type.SIMPLEINT, 4, 1, 0);
        }
    }

    private static final int FOR_INT = 0;
    private static final int FOR_CHAR = 1;
    private static final int FOR_STRING = 2;
    private static final int FOR_NEW_LINE = 3;

    private void emitCodeForPut() {
        sem(() -> {
            switch (operandForOutputValue.getType()) {
                case SIMPLEINT:
                    emitCodeForPutIntOrChar(FOR_INT);
                    break;

                case SIMPLECHAR:
                    emitCodeForPutIntOrChar(FOR_CHAR);
                    break;

                default:            // operand needs to be a string
                    emitCodeForPutString();
                    break;
            }
        });
    }

    private void emitCodeForPutIntOrChar(final int putType) {
        operandForOutputValue.emitLoadVal(code);
        emitColumnWidth(operandForColumnWidth.getValaddr());
        emitPutStatement(putType);
    }

    private void emitCodeForPutString() {
        operandForOutputValue.emitLoadAddr(code);
        emitStringLength();
        emitColumnWidth(operandForColumnWidth.getValaddr());
        emitPutStatement(FOR_STRING);
    }

    private void emitColumnWidth(final int width) {
        code.emit(Instruction.LIT);
        code.emit(width);
    }

    private void emitPutStatement(final int putStyle) {
        code.emit(Instruction.OUT);
        code.emit((byte) putStyle);
    }

    private void emitStringLength() {
        code.emit(Instruction.LIT);
        code.emit(operandForOutputValue.getSize());
    }

    private void parsePutln() {
        parseSymbol(Symbol.PUTLN);
        sem(() -> emitPutStatement(FOR_NEW_LINE));
        parseSymbol(Symbol.SEMICOLON);
    }
}
