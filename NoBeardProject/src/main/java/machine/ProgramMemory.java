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
package machine;

import error.ErrorHandler;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class ProgramMemory {

    private final byte[] programMemory;
    private final ErrorHandler errorHandler;

    ProgramMemory(int memorySize, ErrorHandler errorHandler) {
        programMemory = new byte[memorySize];
        this.errorHandler = errorHandler;
    }

    public ProgramMemory(byte[] programMemory, ErrorHandler errorHandler) {
        this.programMemory = programMemory;
        this.errorHandler = errorHandler;
    }

    void store(int startAddress, byte[] program) {
        if (startAddress + program.length <= this.programMemory.length) {
            System.arraycopy(program, 0, this.programMemory, startAddress, program.length);
        } else {
            errorHandler.throwProgramMemoryOverflow();
        }
    }

    public byte loadByte(int atAddress) {
        if (atAddress < programMemory.length) {
            return programMemory[atAddress];
        } else {
            errorHandler.throwProgramAddressError("0x" + Integer.toHexString(atAddress));
            return -1;
        }
    }

    public int loadHalfWord(int atAddress) {
        if (atAddress < programMemory.length - 1)
            return ((programMemory[atAddress] & 0xff) * 256 + (programMemory[atAddress + 1] & 0xff));
        else {
            errorHandler.throwProgramAddressError("0x" + Integer.toHexString(atAddress));
            return -1;
        }
    }

    public void replaceInstruction(int atAddress, byte instructionId) {
        programMemory[atAddress] = instructionId;
    }

}