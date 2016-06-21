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
import symlist.Operand;
import symlist.Operand.OperandType;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class TermParser extends OperandExportingParser {

    private nbm.Nbm.Opcode opCode;

    private int positionOfLastAndJump;

    public TermParser(Scanner s, SymListManager sym, CodeGenerator c, ErrorHandler e) {
        super();
    }

    public TermParser() {
        this.exportedOperand = new Operand(Operand.OperandKind.ILLEGAL, OperandType.VOID, 0, 0, 0);

    }

    @Override
    public void parseSpecificPart() {
        FactorParser factorParser = ParserFactory.create(FactorParser.class);
        parseSymbol(factorParser);
        sem(() -> exportedOperand = factorParser.getOperand());

        while (currentTokenIsAMulOp()) {
            parseMulOp();
            if (getLastParsedToken().getSy() == Symbol.AND) {
                handleBooleanFactor(factorParser);
            } else {
                handleIntegerFactor(factorParser, getLastParsedToken().getSy().toString());
            }
        }
        sem(() -> {
            if (positionOfLastAndJump != 0) {
                fixAndChain();
            }
        });
    }

    private boolean currentTokenIsAMulOp() {
        Symbol sy = scanner.getCurrentToken().getSy();
        return (sy == Symbol.TIMES || sy == Symbol.DIV || sy == Symbol.MOD || sy == Symbol.AND);
    }

    private void parseMulOp() {
        Scanner.Symbol currentMulOp = scanner.getCurrentToken().getSy();
        parseSymbol(currentMulOp);
        opCode = OperatorToOpCodeMap.getOpCode(currentMulOp);
    }

    private void handleBooleanFactor(FactorParser factorParser) {
        checkOperandForBeing(exportedOperand, OperandType.SIMPLEBOOL, "and");
        sem(() -> exportedOperand.emitLoadVal(code));
        maintainAndChain();
        parseSymbol(factorParser);
        fetchOperand(factorParser);
        checkOperandForBeing(op2, OperandType.SIMPLEBOOL, "and");
        emitCodeForLoadingValue();
    }

    private void maintainAndChain() {
        sem(() -> {
            code.emitOp(Opcode.FJMP);
            code.emitHalfWord(positionOfLastAndJump);
            positionOfLastAndJump = code.getPc() - 2;
        });
    }

    private void handleIntegerFactor(FactorParser factorParser, String usedOperator) {
        checkOperandForBeing(exportedOperand, OperandType.SIMPLEINT, usedOperator);
        sem(() -> exportedOperand.emitLoadVal(code));
        parseSymbol(factorParser);
        fetchOperand(factorParser);
        checkOperandForBeing(op2, OperandType.SIMPLEINT, usedOperator);
        emitCodeForLoadingValue();
        sem(() -> code.emitOp(opCode));
    }

    private void fixAndChain() {
        code.emitOp(Opcode.JMP);
        code.emitHalfWord(code.getPc() + 5);
        while (positionOfLastAndJump != 0) {
            int next = code.getCodeHalfWord(positionOfLastAndJump);
            code.fixup(positionOfLastAndJump, code.getPc());
            positionOfLastAndJump = next;
        }
        code.emitOp(Opcode.LIT);
        code.emitHalfWord(0);
    }

    @Override
    public boolean parseOldStyle() {
        return true;
    }
}
