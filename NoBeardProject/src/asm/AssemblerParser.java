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
package asm;

import error.ErrorHandler;
import io.SourceReader;
import nbm.CodeGenerator;
import nbm.InstructionSet.Instruction;
import scanner.Scanner;
import scanner.Scanner.Symbol;
import scanner.Token;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class AssemblerParser {

    private final ErrorHandler errorHandler;
    private final Scanner scanner;
    private final CodeGenerator codeGenerator;

    AssemblerParser(SourceReader sr, CodeGenerator codeGenerator, ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        scanner = new Scanner(sr, errorHandler);
        this.codeGenerator = codeGenerator;
    }

    boolean parse() {
        scanner.nextToken();
        if (parseOpcode()) {
            return true;
        }
        return false;
    }

    private boolean parseOpcode() {
        Token t = scanner.getCurrentToken();
        if (t.getSymbol() != Symbol.IDENTIFIER) {
            errorHandler.throwSymbolExpectedError("Opcode", t.getClearName());
            return false;
        }
        Instruction parsedInstruction = OpcodeToInstructionMap.getInstruction(t.getClearName());
        if (parsedInstruction == null) {
            errorHandler.throwSymbolExpectedError("Opcode", t.getClearName());
            return false;
        }
        codeGenerator.emit(parsedInstruction);
        return true;
    }

    byte[] getByteCode() {
        return codeGenerator.getByteCode();
    }

}
