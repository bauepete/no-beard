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

import java.util.HashMap;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class InstructionSet {

    @FunctionalInterface
    interface InstructionImplementation {
        
        void execute(ControlUnit controlUnit);
    }

    public enum Instruction {

        NOP((byte) 0x00, (byte) 1, (cu) -> {
        }),
        LIT((byte) 0x01, (byte) 3, (cu) -> {
            int n = cu.getLiteral();
            cu.getCallStack().push(n);
        }),
        LA((byte) 0x02, (byte) 4, (cu) -> {
            byte d = cu.getDisplacement();
            int a = cu.getAddress();
            int base = cu.getBaseFromDisplacement(d);
            cu.getCallStack().push(base + a);
        }),
        LV((byte) 0x03, (byte) 4, (cu) -> {
            byte d = cu.getDisplacement();
            int a = cu.getAddress();
            int base = cu.getBaseFromDisplacement(d);
            cu.getCallStack().push(cu.getDataMemory().loadWord(base + a));
        }),
        LC((byte) 0x04, (byte) 4, (cu) -> {
            byte d = cu.getDisplacement();
            int a = cu.getAddress();
            int base = cu.getBaseFromDisplacement(d);
            int c = cu.getDataMemory().loadByte(base + a);  // assigning byte to int does left padding to full word
            cu.getCallStack().push(c);
        }),
        STO((byte) 0x07, (byte) 1, (cu) -> {
            int value = cu.getCallStack().pop();
            int a = cu.getCallStack().pop();
            cu.getDataMemory().storeWord(a, value);
        }),
        STC((byte) 0x08, (byte) 1, (cu) -> {
            byte value = (byte) cu.getCallStack().pop(); // remove upper significant bytes by casting
            int address = cu.getCallStack().pop();
            cu.getDataMemory().storeByte(address, value);
        }),
        ASSN((byte) 0x0A, (byte) 1, (cu) -> {
            int n = cu.getCallStack().pop();
            int srcAddress = cu.getCallStack().pop();
            int dstAddress = cu.getCallStack().pop();
            cu.getDataMemory().copyBlock(srcAddress, dstAddress, n);
        }),
        NEG((byte) 0x0B, (byte) 1, (cu) -> {
            int x = cu.getCallStack().pop();
            cu.getCallStack().push(-x);
        });

        private final byte id;
        private final byte size;
        private final InstructionImplementation implementation;

        Instruction(byte id, byte size, InstructionImplementation implementation) {
            this.id = id;
            this.size = size;
            this.implementation = implementation;
        }

        byte getId() {
            return id;
        }

        byte getSize() {
            return size;
        }

        void execute(ControlUnit controlUnit) {
            implementation.execute(controlUnit);
        }
    }

    private static final HashMap<Byte, Instruction> idToInstructionMap;

    static {
        idToInstructionMap = new HashMap<>();
        for (Instruction i : Instruction.values()) {
            idToInstructionMap.put(i.getId(), i);
        }
    }

    public Instruction getInstructionById(int id) {
        return idToInstructionMap.get((byte) id);
    }
}
