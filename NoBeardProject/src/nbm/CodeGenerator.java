/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nbm;

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

    public int getPc() {
        return pc;
    }

    public byte getCodeByte(int atAddr) {
        return prog[atAddr];
    }

    public int getCodeHalfWord(int atAddr) {
        return prog[atAddr] * 256 + prog[atAddr + 1];
    }

    public void emitOp(NoBeardMachine.Opcode op) {
        if (getPc() < prog.length - 1) {
            prog[pc] = op.byteCode();
            pc++;
        } else {
            errorHandler.throwProgramMemoryOverflow();
        }
    }

    public void emitByte(byte b) {
        prog[pc] = b;
        pc++;

    }

    public void emitHalfWord(int halfWord) {
        prog[pc] = (byte) (halfWord / 256);
        prog[pc + 1] = (byte) (halfWord % 256);
        pc += 2;
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

    public byte[] getByteCode() {
        return prog;
    }

}
