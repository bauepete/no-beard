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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class InstructionSet {

    @FunctionalInterface
    interface InstructionImplementation {

        void execute(ControlUnit controlUnit);
    }

    public enum OperandType {

        BYTE, HALFWORD
    };

    public enum Instruction {

        NOP((byte) 0x00, (byte) 1, (cu) -> {
        }),
        LIT((byte) 0x01, (byte) 3, OperandType.HALFWORD, (cu) -> {
            int n = cu.getLiteral();
            cu.getCallStack().push(n);
        }),
        LA((byte) 0x02, (byte) 4, OperandType.BYTE, OperandType.HALFWORD, (cu) -> {
            byte d = cu.getDisplacement();
            int a = cu.getAddress();
            int base = cu.getBaseFromDisplacement(d);
            cu.getCallStack().push(base + a);
        }),
        LV((byte) 0x03, (byte) 4, OperandType.BYTE, OperandType.HALFWORD, (cu) -> {
            byte d = cu.getDisplacement();
            int a = cu.getAddress();
            int base = cu.getBaseFromDisplacement(d);
            cu.getCallStack().push(cu.getDataMemory().loadWord(base + a));
        }),
        LC((byte) 0x04, (byte) 4, OperandType.BYTE, OperandType.HALFWORD, (cu) -> {
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
        }),
        ADD((byte) 0x0C, (byte) 1, (cu) -> {
            cu.getCallStack().push(cu.getCallStack().pop() + cu.getCallStack().pop());
        }),
        SUB((byte) 0x0D, (byte) 1, (cu) -> {
            int y = cu.getCallStack().pop();
            int x = cu.getCallStack().pop();
            cu.getCallStack().push(x - y);
        }),
        MUL((byte) 0x0E, (byte) 1, (cu) -> {
            cu.getCallStack().push(cu.getCallStack().pop() * cu.getCallStack().pop());
        }),
        DIV((byte) 0x0F, (byte) 1, (cu) -> {
            int y = cu.getCallStack().pop();
            int x = cu.getCallStack().pop();
            if (y != 0) {
                cu.getCallStack().push(x / y);
            } else {
                cu.stopDueToDivisionByZero();
            }
        }),
        MOD((byte) 0x10, (byte) 1, (cu) -> {
            int y = cu.getCallStack().pop();
            int x = cu.getCallStack().pop();
            if (y != 0) {
                cu.getCallStack().push(x % y);
            } else {
                cu.stopDueToDivisionByZero();
            }
        }),
        NOT((byte) 0x11, (byte) 1, (cu) -> {
            int x = cu.getCallStack().pop();
            if (x == 0) {
                cu.getCallStack().push(1);
            } else {
                cu.getCallStack().push(0);
            }
        }),
        REL((byte) 0x12, (byte) 2, OperandType.BYTE, (cu) -> {
            int y = cu.getCallStack().pop();
            int x = cu.getCallStack().pop();
            byte relOp = cu.getType();
            int valueToPush;

            switch (relOp) {
                case 0:
                    if (x < y) {
                        valueToPush = 1;
                    } else {
                        valueToPush = 0;
                    }
                    break;
                case 1:
                    if (x <= y) {
                        valueToPush = 1;
                    } else {
                        valueToPush = 0;
                    }
                    break;
                case 2:
                    if (x == y) {
                        valueToPush = 1;
                    } else {
                        valueToPush = 0;
                    }
                    break;
                case 3:
                    if (x != y) {
                        valueToPush = 1;
                    } else {
                        valueToPush = 0;
                    }
                    break;
                case 4:
                    if (x >= y) {
                        valueToPush = 1;
                    } else {
                        valueToPush = 0;
                    }
                    break;
                case 5:
                    if (x > y) {
                        valueToPush = 1;
                    } else {
                        valueToPush = 0;
                    }
                    break;
                default:
                    cu.stopDueToOperandRangeError();
                    valueToPush = 0;
            }
            cu.getCallStack().push(valueToPush);
        }),
        FJMP((byte) 0x16, (byte) 3, OperandType.HALFWORD, (cu) -> {
            int newPc = cu.getAddress();
            int x = cu.getCallStack().pop();
            if (x == 0) {
                cu.setPc(newPc);
            }
        }),
        TJMP((byte) 0x17, (byte) 3, OperandType.HALFWORD, (cu) -> {
            int newPc = cu.getAddress();
            int x = cu.getCallStack().pop();
            if (x == 1) {
                cu.setPc(newPc);
            }
        }),
        JMP((byte) 0x18, (byte) 3, OperandType.HALFWORD, (cu) -> {
            int newPc = cu.getAddress();
            cu.setPc(newPc);
        }),
        OUT((byte) 0x1A, (byte) 2, OperandType.BYTE, (cu) -> {
            byte type = cu.getType();
            switch (type) {
                case 0:
                    cu.outputInt();
                    break;
                case 1:
                    cu.outputChar();
                    break;
                case 2:
                    cu.outputString();
                    break;
                case 3:
                    System.out.println();
                    break;
                default:
                    cu.stopDueToOperandRangeError();
            }
        }),
        INC((byte) 0x1D, (byte) 3, OperandType.HALFWORD, (cu) -> {
            int size = cu.getLiteral();
            int sp = cu.getCallStack().getStackPointer();
            cu.getCallStack().setStackPointer(sp + size);
        }),
        HALT((byte) 0x1F, (byte) 1, (cu) -> {
            cu.stopMachine();
        });

        private final byte id;
        private final byte size;
        private final List<OperandType> operandTypes;
        private final InstructionImplementation implementation;

        Instruction(byte id, byte size, InstructionImplementation implementation) {
            this.id = id;
            this.size = size;
            this.implementation = implementation;
            this.operandTypes = new ArrayList<>();
        }
        
        Instruction(byte id, byte size, OperandType operandType, InstructionImplementation implementation) {
            this.id = id;
            this.size = size;
            this.implementation = implementation;
            this.operandTypes = Arrays.asList(operandType);
        }

        Instruction(byte id, byte size, OperandType operandType, OperandType operandType2, InstructionImplementation implementation) {
            this.id = id;
            this.size = size;
            this.implementation = implementation;
            this.operandTypes = Arrays.asList(operandType, operandType2);
        }

        public byte getId() {
            return id;
        }

        byte getSize() {
            return size;
        }
        
        public boolean hasOperands() {
            return getOperandTypes().size() > 0;
        }
        
        public List<OperandType> getOperandTypes() {
            return operandTypes;
        }

        void execute(ControlUnit controlUnit) {
            implementation.execute(controlUnit);
        }
        
        @Override
        public String toString() {
            String s = super.toString();
            return s.toLowerCase();
        }
    }

    private static final HashMap<Byte, Instruction> idToInstructionMap;

    static {
        idToInstructionMap = new HashMap<>();
        for (Instruction i : Instruction.values()) {
            idToInstructionMap.put(i.getId(), i);
        }
    }

    public static Instruction getInstructionById(int id) {
        return idToInstructionMap.get((byte) id);
    }
}
