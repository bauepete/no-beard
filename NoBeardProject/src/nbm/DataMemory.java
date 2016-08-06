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
package nbm;

import error.ErrorHandler;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class DataMemory {

    private final byte[] memory;
    private final ErrorHandler errorHandler;

    DataMemory(int memorySize, ErrorHandler errorHandler) {
        memory = new byte[memorySize];
        this.errorHandler = errorHandler;
    }

    void storeWord(int atAddress, int value) {
        if (atAddress + 4 <= memory.length) {
            for (int i = 0; i < 4; i++) {
                memory[atAddress + i] = (byte) (value % 256);
                value /= 256;
            }
        } else {
            errorHandler.throwDataAddressError(Integer.toHexString(atAddress));
        }
    }

    int loadWord(int atAddress) {
        if (atAddress < 1021) {
            int rv = 0;
            for (int i = 3; i >= 0; i--) {
                rv *= 256;
                rv += (memory[atAddress + i] & 0xff);
            }
            return rv;
        } else {
            return -1;
        }
    }

    void store(int atAddress, byte[] memoryBlock) {
        if (atAddress + memoryBlock.length <= memory.length) {
            System.arraycopy(memoryBlock, 0, memory, atAddress, memoryBlock.length);
        } else {
            errorHandler.throwDataAddressError(Integer.toHexString(atAddress));
        }
    }

    byte[] load(int atAddress, int length) {
        if (atAddress + length <= memory.length) {
            byte[] rv = new byte[length];
            System.arraycopy(memory, atAddress, rv, 0, length);
            return rv;
        } else {
            errorHandler.throwDataAddressError(Integer.toHexString(atAddress));
            return null;
        }
    }

    /**
     * Stores string constants in memory starting at address 0.
     *
     * @param stringConstants The string constants to be stored.
     * @return The address of the next free word in DataMemory if storing is
     * successful, otherwise -1.
     */
    int storeStringConstants(byte[] stringConstants) {
        store(0, stringConstants);
        if (errorHandler.getCount() == 0) {
            final int startAddressOfNextWord = (((stringConstants.length - 1) / 4 + 1) * 4);
            return startAddressOfNextWord;
        } else {
            return -1;
        }
    }
}
