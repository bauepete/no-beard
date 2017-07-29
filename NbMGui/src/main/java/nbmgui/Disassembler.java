package nbmgui;

import io.BinaryFile;
import machine.InstructionSet;
import machine.ProgramMemory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Egon on 28.07.2017.
 */
public class Disassembler {
    private final BinaryFile objectFile;
    private InstructionSet.Instruction instructionRegister;
    private byte[] programMemory;

    public Disassembler(BinaryFile objectFile) {
        this.objectFile = objectFile;
        this.programMemory = new byte[]{};
    }

    public List<String> getProgramData() {
        programMemory = objectFile.getProgram().clone();
        List<String> result = new ArrayList<>();
        int pc = 0;
        int currentAddressInProgramMemory = pc;
        while (pc < programMemory.length) {
            instructionRegister = InstructionSet.getInstructionById(programMemory[pc]);
            StringBuilder line = new StringBuilder("0x" + String.format("%0" + 3 + "d  ", pc) + instructionRegister.toString());
            currentAddressInProgramMemory++;
            if (instructionRegister.hasOperands()) {
                line.append(" ");
                if (instructionRegister.getOperandTypes().size() == 1) {
                    if (instructionRegister.getOperandTypes().get(0) == InstructionSet.OperandType.BYTE)
                        line.append(String.valueOf(programMemory[currentAddressInProgramMemory]));
                    else
                        line.append(String.valueOf(((programMemory[currentAddressInProgramMemory] & 0xff) * 256 + (programMemory[currentAddressInProgramMemory + 1] & 0xff))));
                } else {
                    line.append(String.valueOf(programMemory[currentAddressInProgramMemory]));
                    currentAddressInProgramMemory++;
                    line.append(" " + String.valueOf(((programMemory[currentAddressInProgramMemory] & 0xff) * 256 + (programMemory[currentAddressInProgramMemory + 1] & 0xff))));
                }
            }
            result.add(line.toString());
            pc += instructionRegister.getSize();
            currentAddressInProgramMemory = pc;
        }
        return result;
    }
}
