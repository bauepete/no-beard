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
public class TermParser extends Parser {

    private nbm.Nbm.Opcode opCode;
    
    private Operand op;
    private Operand op2;

    private int positionOfLastAndJump;

    public TermParser(Scanner s, SymListManager sym, CodeGenerator c, ErrorHandler e) {
        super(s, sym, c, e);
    }

    public TermParser() {

    }

    @Override
    public void parseSpecificPart() {
        sem(() -> positionOfLastAndJump = 0);
        FactorParser factorParser = ParserFactory.create(FactorParser.class);
        parseSymbol(factorParser);
        sem(() -> op = factorParser.getOperand());

        while (currentTokenIsAMulOp()) {
            parseMulOp();
            if (getLastParsedToken().getSy() == Symbol.AND) {
                handleBooleanFactors(factorParser);
            } else {
                handleIntegerFactors(factorParser);
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

    private void handleBooleanFactors(FactorParser factorParser) {
        checkOperandForBeing(OperandType.SIMPLEBOOL);
        maintainAndChain();
        parseSymbol(factorParser);
        fetchOperand(factorParser);
        checkOperandForBeing(OperandType.SIMPLEBOOL);
        EmitCodeForLoadingValueAndTakeOverOperand();
    }

    private void checkOperandForBeing(final OperandType requestedType) {
        where(op.getType() == requestedType,
                () -> getErrorHandler().throwOperatorOperandTypeMismatch("and", "bool"));
    }

    private void maintainAndChain() {
        sem(() -> {
            op.emitLoadVal(code);
            code.emitOp(Opcode.FJMP);
            code.emitHalfWord(positionOfLastAndJump);
            positionOfLastAndJump = code.getPc() - 2;
        });
    }

    private void fetchOperand(FactorParser factorParser) {
        sem(() -> op2 = factorParser.getOperand());
    }

    private void EmitCodeForLoadingValueAndTakeOverOperand() {
        sem(() -> op = op2.emitLoadVal(code));
    }

    private void handleIntegerFactors(FactorParser factorParser) {
        checkOperandForBeing(OperandType.SIMPLEINT);
        sem(() -> op.emitLoadVal(code));
        parseSymbol(factorParser);
        fetchOperand(factorParser);
        checkOperandForBeing(OperandType.SIMPLEINT);
        EmitCodeForLoadingValueAndTakeOverOperand();
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

    public Operand getOperand() {
        return op;
    }
}
