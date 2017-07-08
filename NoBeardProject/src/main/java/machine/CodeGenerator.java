/*
 * Copyright ©2011 - 2016. Created by P. Bauer (p.bauer@htl-leonding.ac.at),
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
package machine;

import error.ErrorHandler;

/**
 *
 * @author peter
 */
public class CodeGenerator {

    private final byte[] prog;
    private int pc;
    private ErrorHandler errorHandler;

    public CodeGenerator(int codeStorageSize) {
        prog = new byte[codeStorageSize];
    }

    CodeGenerator(int codeStorageSize, ErrorHandler errorHandler) {
        prog = new byte[codeStorageSize];
        this.errorHandler = errorHandler;
    }

    /**
     * 
     * @return The next free byte in program memory.
     */
    public int getPc() {
        return pc;
    }

    byte getCodeByte(int atAddr) {
        return prog[atAddr];
    }

    /**
     * Returns two bytes from program memory as an integer value starting
     * from atAddr.
     * @param atAddr The address where to start.
     * @return Two bytes as integer value.
     */
    public int getCodeHalfWord(int atAddr) {
        return prog[atAddr] * 256 + prog[atAddr + 1];
    }

    /**
     * Emits an opcode to the program memory
     * @param op The opcode.
     */
    public void emit(InstructionSet.Instruction op) {
        appendToProgramMemory(getByteCode(op));
    }

    private byte[] getByteCode(InstructionSet.Instruction op) {
        return new byte[]{op.getId()};
    }

    private void appendToProgramMemory(byte[] instruction) {
        if (getPc() + instruction.length <= prog.length) {
            System.arraycopy(instruction, 0, prog, pc, instruction.length);
            pc += instruction.length;
        } else {
            errorHandler.throwProgramMemoryOverflow();
        }
    }

    /**
     * Emits one byte to the program memory.
     * @param b The byte.
     */
    public void emit(byte b) {
            appendToProgramMemory(getByteCode(b));
    }

    private byte[] getByteCode(byte b) {
        return new byte[]{b};
    }

    /**
     * Emits two bytes to the program memory. The two bytes are encoded
     * as an integer value.
     * @param halfWord 
     */
    public void emit(int halfWord) {
        appendToProgramMemory(getByteCode(halfWord));
    }
    
    private byte[] getByteCode(int halfWord) {
        return new byte[] {(byte)(halfWord/256), (byte)(halfWord%256)};
    }

    /**
     * Writes a half word at a specific address into the program memory.
     *
     * @param atAddr Address in program memory where to write
     * @param halfWord The half word to be written.
     */
    public void fixup(int atAddr, int halfWord) {
        prog[atAddr] = (byte) (halfWord / 256);
        prog[atAddr + 1] = (byte) (halfWord % 256);
    }

    /**
     * Returns the byte code generated via the emit(...) commands.
     * @return The complete byte code.
     */
    public byte[] getByteCode() {
        byte[] returnedCode = new byte[pc];
        System.arraycopy(prog, 0, returnedCode, 0, pc);
        return returnedCode;
    }

}
