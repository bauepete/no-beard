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
import java.util.Scanner;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class ControlUnit {

    public enum MachineState {

        STOPPED, RUNNING, ERROR
    }

    private final ProgramMemory programMemory;
    private final DataMemory dataMemory;
    private final CallStack callStack;
    private final ErrorHandler errorHandler;
    private int pc;
    private int currentAddressInProgramMemory;
    private InstructionSet.Instruction instructionRegister;
    private MachineState machineState;

    ControlUnit(ProgramMemory programMemory, DataMemory dataMemory, CallStack callStack, ErrorHandler errorHandler) {
        this.programMemory = programMemory;
        this.dataMemory = dataMemory;
        this.callStack = callStack;
        this.errorHandler = errorHandler;
        this.machineState = MachineState.STOPPED;
    }

    public ProgramMemory getProgramMemory() {
        return programMemory;
    }

    public DataMemory getDataMemory() {
        return dataMemory;
    }

    public CallStack getCallStack() {
        return callStack;
    }

    int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    int getLiteral() {
        int n = programMemory.loadHalfWord(currentAddressInProgramMemory);
        currentAddressInProgramMemory += 2;
        return n;
    }

    byte getDisplacement() {
        byte d = programMemory.loadByte(currentAddressInProgramMemory);
        currentAddressInProgramMemory++;
        return d;
    }

    int getBaseFromDisplacement(byte displacement) {
        int base = getCallStack().getFramePointer();
        for (int i = 0; i < displacement; i++) {
            base = getDataMemory().loadWord(base);
        }
        return base;
    }

    int getAddress() {
        int a = programMemory.loadHalfWord(currentAddressInProgramMemory);
        currentAddressInProgramMemory += 2;
        return a;
    }

    byte getType() {
        byte r = programMemory.loadByte(currentAddressInProgramMemory);
        currentAddressInProgramMemory++;
        return r;
    }

    void executeCycle() {
        fetchInstruction();
        setPc(getPc() + instructionRegister.getSize());
        executeInstruction();
    }
    
    public void startMachine() {
        machineState = MachineState.RUNNING;
    }

    void fetchInstruction() {
        instructionRegister = InstructionSet.getInstructionById(programMemory.loadByte(getPc()));
        currentAddressInProgramMemory = getPc() + 1;
    }

    void executeInstruction() {
        instructionRegister.execute(this);
    }

    MachineState getMachineState() {
        return machineState;
    }

    void stopMachine() {
        machineState = MachineState.STOPPED;
    }

    void stopDueToDivisionByZero() {
        errorHandler.throwDivisionByZero();
        stopDueToError();
    }

    void stopDueToOperandRangeError() {
        errorHandler.throwOperandRangeError();
        stopDueToError();
    }

    /**
     * Sets the machine state to error because of any other reason.
     */
    void stopDueToError() {
        machineState = MachineState.ERROR;
    }
    
    void inputInt() {
        int successful = callStack.pop();
        int data = callStack.pop();
        Scanner c = new Scanner(System.in);
        if (c.hasNextInt()) {
            int readInt = c.nextInt();
            dataMemory.storeWord(successful, 1);
            dataMemory.storeWord(data, readInt);
        } else {
            dataMemory.storeWord(successful, 0);
        }
    }

    void outputInt() {
        int width = callStack.pop();
        int x = callStack.pop();
        String formatString = "%" + width + "d";
        System.out.printf(formatString, x);
    }

    void outputChar() {
        int width = callStack.pop();
        int c = callStack.pop();
        System.out.print((char) c);
        outputBlanks(width - 1);
    }

    private void outputBlanks(int number) {
        for (int i = 0; i < number; i++) {
            System.out.print(" ");
        }
    }

    void outputString() {
        int width = callStack.pop();
        int stringLength = callStack.pop();
        int address = callStack.pop();
        System.out.print(new String(dataMemory.load(address, stringLength)));
        outputBlanks(width - stringLength);
    }
}
