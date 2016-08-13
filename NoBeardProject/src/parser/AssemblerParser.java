/*
 * Copyright ©2016. Created by P. Bauer (p.bauer@htl-leonding.ac.at),
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
import nbm.InstructionSet;
import nbm.InstructionSet.Instruction;
import nbm.InstructionSet.OperandType;
import scanner.Scanner.Symbol;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class AssemblerParser extends Parser {

    private static final int MAX_HALF_WORD = 65535;
    private static final int MAX_BYTE = 255;

    private final ErrorHandler errorHandler;
    private final CodeGenerator codeGenerator;

    private Instruction parsedInstruction;
    private OperandType requestedOperandType;

    AssemblerParser() {
        this.errorHandler = ParserFactory.getErrorHandler();
        this.codeGenerator = ParserFactory.getCodeGenerator();
    }

    @Override
    protected void parseSpecificPart() {
        while (ParserFactory.getScanner().getCurrentToken().getSymbol() != Symbol.EOFSY) {
            parseOpcode();
            if (parsedInstruction != null && parsedInstruction.hasOperands()) {
                parseOperands();
            }
        }
    }

    private void parseOpcode() {
        parseSymbol(Symbol.IDENTIFIER);
        sem(() -> parsedInstruction = OpcodeToInstructionMap.getInstruction(getLastParsedToken().getClearName()));
        where(parsedInstruction != null, () -> errorHandler.throwSymbolExpectedError("Opcode", getLastParsedToken().getClearName()));
        sem(() -> codeGenerator.emit(parsedInstruction));
    }

    private void parseOperands() {
        parsedInstruction.getOperandTypes().stream().map((operandType) -> {
            parseSymbol(Symbol.NUMBER);
            return operandType;
        }).map((operandType) -> {
            sem(() -> requestedOperandType = operandType);
            return operandType;
        }).map((operandType) -> {
            where(operandFits(), errorHandler::throwOperandRangeError);
            return operandType;
        }).forEach((operandType) -> {
            sem(() -> {
                if (operandType == InstructionSet.OperandType.BYTE) {
                    codeGenerator.emit((byte) getLastParsedToken().getValue());
                } else {
                    codeGenerator.emit(getLastParsedToken().getValue());
                }
            });
        });
    }

    private boolean operandFits() {
        if (requestedOperandType == OperandType.BYTE) {
            return getLastParsedToken().getValue() < MAX_BYTE + 1;
        } else {
            return getLastParsedToken().getValue() < MAX_HALF_WORD + 1;
        }
    }

    public byte[] getByteCode() {
        return codeGenerator.getByteCode();
    }
}
