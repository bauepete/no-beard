/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import error.Error;
import error.Error.ErrorType;
import error.ErrorHandler;
import java.util.LinkedList;
import java.util.List;
import nbm.CodeGenerator;
import nbm.Nbm.Opcode;
import scanner.Scanner;
import scanner.Scanner.Symbol;
import symboltable.ConstantOperand;
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
            parseSymbol(Symbol.COMMA);
            parseExpression();
            fetchOperandForColumnWidth();
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

    private void fetchOperandForColumnWidth() {
        sem(() -> operandForColumnWidth = parserForOutputExpression.getOperand());
        where(operandForColumnWidth.getType() == Type.SIMPLEINT, () -> getErrorHandler().throwOperandOfKindExpected("Integer"));
    }

    private void emitCodeForPut() {
        sem(() -> {
            switch (operandForOutputValue.getType()) {
                case SIMPLEINT:
                    operandForOutputValue.emitLoadVal(code);
                    code.emitOp(Opcode.LIT);
                    code.emitHalfWord(0);
                    code.emitOp(Opcode.PUT);
                    code.emitByte((byte) 0);
                    break;

                case SIMPLECHAR:
                    operandForOutputValue.emitLoadVal(code);
                    code.emitOp(Opcode.LIT);
                    code.emitHalfWord(0);
                    code.emitOp(Opcode.PUT);
                    code.emitByte((byte) 1);

                default:        // needs to be a string
                    operandForOutputValue.emitLoadAddr(code);
                    code.emitOp(Opcode.LIT);
                    code.emitHalfWord(operandForOutputValue.getSize());
                    code.emitOp(Opcode.LIT);
                    code.emitHalfWord(operandForOutputValue.getSize());
                    code.emitOp(Opcode.PUT);
                    code.emitByte((byte) 2);
            }
        });
    }

    private void parsePutln() {
        parseSymbol(Symbol.PUTLN);
        sem(() -> {
            code.emitOp(Opcode.PUT);
            code.emitByte((byte) 3);
        });
    }

    @Override
    public boolean parseOldStyle() {
        boolean isParsedCorrectly = false;

        switch (scanner.getCurrentToken().getSymbol()) {
            case PUT:
                isParsedCorrectly = put();
                break;

            case PUTLN:
                isParsedCorrectly = putln();
                break;

            default:
                String[] sList = {Symbol.PUTLN.toString(), Symbol.PUT.toString()};
                getErrorHandler().raise(new Error(ErrorType.SYMBOL_EXPECTED, sList));
                break;
        }
        return isParsedCorrectly;
    }

    private boolean put() {
        if (!tokenIsA(Symbol.PUT)) {
            return false;
        }

        if (!tokenIsA(Symbol.LPAR)) {
            return false;
        }

        ExpressionParser exprP = new ExpressionParser(scanner, sym, code, getErrorHandler());
        if (!exprP.parseOldStyle()) {
            return false;
        }

        // cc
        Operand op = exprP.getOperand();
        if (!isOperandToPut(op)) {
            getErrorHandler().raise(new Error(ErrorType.OPERAND_KIND_EXPECTED, outputableOperands()));
            return false;
        }
        // endcc

        // sem
        if (op.getType() == Type.ARRAYCHAR) {
            op.emitLoadAddr(code);
            if (op.getSize() == Operand.UNDEFSIZE) {
                code.emitOp(Opcode.LIT);
                code.emitHalfWord(65535);
            } else {
                code.emitOp(Opcode.LIT);
                code.emitHalfWord(op.getSize());
            }
        } else {
            op.emitLoadVal(code);
        }
        // endsem

        switch (scanner.getCurrentToken().getSymbol()) {
            case COMMA:
                scanner.nextToken();

                if (!exprP.parseOldStyle()) {
                    return false;
                }
                // sem
                Operand wOp = exprP.getOperand();
                // endsem
                // cc
                if (wOp.getType() != Type.SIMPLEINT) {
                    getErrorHandler().raise(new Error(ErrorType.TYPES_EXPECTED, Type.SIMPLEINT.toString()));
                    return false;
                }
                // endcc
                // sem
                wOp.emitLoadVal(code);
                // endsem
                if (!tokenIsA(Symbol.RPAR)) {
                    getErrorHandler().raise(new Error(ErrorType.SYMBOL_EXPECTED, Symbol.RPAR.toString()));
                    // TODO: Add raiseError() and getNameManager as private methods to Parser
                    // raiseError(new SymbolExpected(getNameManager().getString(Symbol.RPAR)));
                    return false;
                }
                // sem
                emitPut(op.getType());
                //endsem

                break;

            case RPAR:
                // cc
                if (op.getSize() == Operand.UNDEFSIZE) {
                    getErrorHandler().raise(new Error(ErrorType.OPERAND_KIND_EXPECTED, outputableOperands()));
                    return false;
                }
                // end cc
                // sem
                switch (op.getType()) {
                    case SIMPLEINT:
                        code.emitOp(Opcode.LIT);
                        code.emitHalfWord(0);
                        break;

                    case SIMPLECHAR:
                    case ARRAYCHAR:
                        code.emitOp(Opcode.LIT);
                        code.emitHalfWord(op.getSize());
                        break;
                }
                emitPut(op.getType());
                // endsem
                scanner.nextToken();
                break;

            default:
                String[] sList = {Symbol.COMMA.toString(), Symbol.RPAR.toString()};
                getErrorHandler().raise(new Error(ErrorType.SYMBOL_EXPECTED, sList));
                return false;
        }
        return true;
    }

    private boolean putln() {
        if (!tokenIsA(Symbol.PUTLN)) {
            return false;
        }

        // sem
        code.emitOp(Opcode.PUT);
        code.emitByte((byte) 3);
        // endsem
        return true;
    }

    private String[] outputableOperands() {
        List<String> opList = new LinkedList();

        for (Type operand : OUTPUTABLE_OPERANDS) {
            opList.add(operand.toString());
        }
        return (String[]) opList.toArray();
    }

    private void emitPut(Type type) {
        switch (type) {
            case SIMPLEINT:
                code.emitOp(Opcode.PUT);
                code.emitByte((byte) 0);
                break;

            case SIMPLECHAR:
                code.emitOp(Opcode.PUT);
                code.emitByte((byte) 1);
                break;

            case ARRAYCHAR:
                code.emitOp(Opcode.PUT);
                code.emitByte((byte) 2);
                break;
        }
    }
}
