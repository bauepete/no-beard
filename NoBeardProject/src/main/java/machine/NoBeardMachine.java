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

import config.Configuration;
import error.ErrorHandler;
import error.SourceCodeInfo;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author peter
 */
public class NoBeardMachine implements SourceCodeInfo {

    @Override
    public int getCurrentCol() {
        return 0;
    }

    @Override
    public int getCurrentLine() {
        return controlUnit.getPc();
    }

    error.Error getError() {
        return errorHandler.getLastError();
    }

    public static final int MAX_PROG = 1024;   // Size of program memory
    public static final int MAX_DATA = 1024;    // Size of data memory
    public static final int SIZE_OF_AUXILIARY_CELLS = 28;    // Space to store house keeping data for stack frames

    private final error.ErrorHandler errorHandler;
    private final DataMemory dataMemory;
    private final CallStack callStack;
    private final ProgramMemory programMemory;
    private final ControlUnit controlUnit;
    private final TreeSet<Integer> breakpoints;
    private final Debugger debugger;

    public NoBeardMachine(InputDevice in, OutputDevice out) {
        errorHandler = new ErrorHandler(this);
        dataMemory = new DataMemory(MAX_DATA, errorHandler);
        callStack = new CallStack(dataMemory, 0, SIZE_OF_AUXILIARY_CELLS);
        programMemory = new ProgramMemory(MAX_PROG, errorHandler);
        controlUnit = new ControlUnit(programMemory, dataMemory, callStack, errorHandler, in, out);
        breakpoints = new TreeSet<>();
        debugger = new Debugger(programMemory);
        controlUnit.addObserver(debugger);
    }

    public static String getVersion() {
        return Configuration.getVersion();
    }

    public ControlUnit.MachineState getState() {
        return controlUnit.getMachineState();
    }

    public void loadStringConstants(byte[] data) {
        int nextWord = dataMemory.storeStringConstants(data);
        if (errorHandler.getCount() == 0) {
            callStack.setCurrentFramePointer(nextWord);
        } else {
            controlUnit.stopDueToError();
        }
    }

    public void loadProgram(int atAddr, byte[] data) {
        programMemory.store(atAddr, data);
    }

    public void runProgram(int startPc) {
        System.out.println("Starting programm at pc " + startPc);
        controlUnit.startMachine(startPc);
        runUntilNextBreakpoint();
    }

    public void runUntilNextBreakpoint() {
        if (getState() == ControlUnit.MachineState.BLOCKED)
            controlUnit.startMachine(getCurrentLine());
        while (getState() == ControlUnit.MachineState.RUNNING) {
            step();
        }
    }

    public void addBreakpoint(int atAddress) {
        debugger.setBreakpoint(atAddress, InstructionSet.getInstructionById(programMemory.loadByte(atAddress)).getId());
    }

    public void removeBreakpoint(int atAddress) {
        debugger.removeBreakpoint(atAddress);
    }

    public void removeAllBreakpoints() {
        debugger.clearBreakpoints();
    }

    public Set<Integer> getBreakpoints() {
          return debugger.getAllBreakpoints();
    }

    public void stopProgram() {
        controlUnit.stopMachine();
        if(this.getBreakpoints().contains(getCurrentLine())) {
            this.debugger.replaceInstructionAtAddress(getCurrentLine(), InstructionSet.Instruction.BREAK);
        }
        resetStackPointer();
    }

    private void resetStackPointer() {
        callStack.setCurrentFramePointer(callStack.getFramePointer());
    }

    public void setBreakInstructionIfNeeded() {
        int address = getCurrentLine()-controlUnit.getInstructionRegister().getSize();
        if (getBreakpoints().contains(address))
            debugger.replaceInstructionAtAddress(address, InstructionSet.Instruction.BREAK);
    }

    public void replaceBreakInstruction() {
        debugger.replaceInstructionAtAddress(getCurrentLine(), null);
    }

    public void step() {
        controlUnit.executeCycle();
    }

    Object peek() {
        return callStack.peek();
    }

    public DataMemory getDataMemory() {
        return dataMemory;
    }

    public CallStack getCallStack() {
        return callStack;
    }
}
