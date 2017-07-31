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
    private final ProgramMemory programMemory;
    private InstructionSet.Instruction instructionRegister;

    public Disassembler(BinaryFile objectFile) {
        this.objectFile = objectFile;
        this.programMemory = new ProgramMemory(objectFile.getProgram(), null);
    }

    public List<String> getProgramData() {
        List<String> result = new ArrayList<>();
        int pc = 0;
        int currentAddressInProgramMemory = pc;
        while (pc < objectFile.getProgram().length) {
            instructionRegister = InstructionSet.getInstructionById(programMemory.loadByte(pc));
            StringBuilder line = new StringBuilder("0x" + String.format("%0" + 3 + "d  ", pc) + instructionRegister.toString());
            for (InstructionSet.OperandType operandType : instructionRegister.getOperandTypes()) {
                line.append(" ");
                currentAddressInProgramMemory++;
                if (operandType == InstructionSet.OperandType.BYTE)
                    line.append(String.valueOf(programMemory.loadByte(currentAddressInProgramMemory)));
                else
                    line.append(String.valueOf(programMemory.loadHalfWord(currentAddressInProgramMemory)));
            }
            result.add(line.toString());
            pc += instructionRegister.getSize();
            currentAddressInProgramMemory = pc;
        }
        return result;
    }
}
