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

import java.util.Arrays;
import java.util.List;

import io.BinaryFile;
import io.BinaryFileHandler;
import machine.InstructionSet.Instruction;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author peter
 */
public class NoBeardMachineTest {

    private NoBeardMachine machine;
    private FakeInputDevice in;
    private FakeOutputDevice out;

    public NoBeardMachineTest() {
    }

    @Before
    public void setUp() {
        in = new FakeInputDevice();
        out = new FakeOutputDevice();
        machine = new NoBeardMachine(in, out);
    }

    @Test
    public void testInitialization() {
        assertTrue("State", machine.getState() == ControlUnit.MachineState.STOPPED);
    }

    @Test
    public void testLoadStringConstants() {
        machine.loadStringConstants(new byte[4]);
        assertEquals(ControlUnit.MachineState.STOPPED, machine.getState());
    }

    @Test
    public void testLoadMaxNumberOfStrings() {
        machine.loadStringConstants(new byte[NoBeardMachine.MAX_DATA]);
        assertEquals(ControlUnit.MachineState.STOPPED, machine.getState());
    }

    @Test
    public void testStoreStringsOverflow() {
        machine.loadStringConstants(new byte[NoBeardMachine.MAX_DATA + 1]);
        assertEquals(ControlUnit.MachineState.ERROR, machine.getState());
        assertEquals(error.Error.ErrorType.DATA_ADDRESS_ERROR.getNumber(), machine.getError().getNumber());
    }

    @Test
    public void testStep() {
        byte[] p = {
            Instruction.LIT.getId(), 0, 2, // value
            Instruction.LIT.getId(), 0, 1, // column width
            Instruction.OUT.getId(), 0,
            Instruction.HALT.getId()
        };
        machine.loadProgram(0, p);

        machine.step();
        assertEquals(2, machine.peek());
        machine.step();
        assertEquals(1, machine.peek());
        machine.step();
        assertEquals(ControlUnit.MachineState.STOPPED, machine.getState());
    }

    @Test
    public void runProgram() {
        String output = "Calculating+=";
        byte[] stringMemory = output.getBytes();
        byte[] program = {
                Instruction.INC.getId(), 0, 12, // reserve space for 3 variables
                Instruction.LA.getId(), 0, 0, 32, // load first variable
                Instruction.LIT.getId(), 0, 17, // load 17
                Instruction.STO.getId(), // store 17
                Instruction.LA.getId(), 0, 0, 36, // load second variable
                Instruction.LIT.getId(), 0, 42, // load 42
                Instruction.STO.getId(), // store 42
                Instruction.LA.getId(), 0, 0, 40, // load third variable
                Instruction.LV.getId(), 0, 0, 32, // load value of first var
                Instruction.LV.getId(), 0, 0, 36, // load value of second var
                Instruction.ADD.getId(), // add 17 and 42
                Instruction.STO.getId(), // store to address 40
                Instruction.LIT.getId(), 0, 0, // address of "Calculating"
                Instruction.LIT.getId(), 0, 11, // length of "Calculating"
                Instruction.LIT.getId(), 0, 12, // width of column
                Instruction.OUT.getId(), 2, // output string "Calculating"
                Instruction.LV.getId(), 0, 0, 32,
                Instruction.LIT.getId(), 0, 1,
                Instruction.OUT.getId(), 0, // output of first variable
                Instruction.LIT.getId(), 0, 11, // address of "+"
                Instruction.LIT.getId(), 0, 1, // length of "+"
                Instruction.LIT.getId(), 0, 1, // width of column
                Instruction.OUT.getId(), 2, // output string "+"
                Instruction.LV.getId(), 0, 0, 36,
                Instruction.LIT.getId(), 0, 1,
                Instruction.OUT.getId(), 0, // output of second variable
                Instruction.LIT.getId(), 0, 12, // address of "="
                Instruction.LIT.getId(), 0, 1, // length of "="
                Instruction.LIT.getId(), 0, 1, // width of column
                Instruction.OUT.getId(), 2, // output string "="
                Instruction.LV.getId(), 0, 0, 40,
                Instruction.LIT.getId(), 0, 1,
                Instruction.OUT.getId(), 0, // output of second variable
                Instruction.OUT.getId(), 3,
                Instruction.HALT.getId()
        };

        machine.loadStringConstants(stringMemory);
        machine.loadProgram(0, program);
        machine.runProgram(0);

        List<String> expectedOutput = Arrays.asList("Calculating", "17", "+", "42", "=", "59", "\n");
        assertEquals(expectedOutput, out.output);
        assertEquals(17 + 42, machine.getDataMemory().loadWord(56));
    }

    @Ignore
    @Test
    public void testProgramDebugger() {
        String output = "HelloWorld";
        byte[] stringMemory = output.getBytes();
        byte[] program = {
                Instruction.LIT.getId(), 0, 0,
                Instruction.LIT.getId(), 0, 5,
                Instruction.LIT.getId(), 0, 6,
                Instruction.OUT.getId(), 2,
                Instruction.LIT.getId(), 0, 5,
                Instruction.LIT.getId(), 0, 5,
                Instruction.LIT.getId(), 0, 5,
                Instruction.OUT.getId(), 2,
                Instruction.OUT.getId(), 3,
                Instruction.HALT.getId()
        };

        machine.addBreakpoint(11);
        machine.addBreakpoint(22);
        machine.loadStringConstants(stringMemory);
        machine.loadProgram(0, program);
        machine.runProgram(0);

        List<String> expectedOutput = Arrays.asList("Hello");
        assertEquals(expectedOutput, out.output);

        expectedOutput = Arrays.asList("Hello", "World");
        machine.runUntilNextBreakpoint();
        assertEquals(expectedOutput, out.output);

        expectedOutput = Arrays.asList("Hello", "World", "\n");
        machine.runUntilNextBreakpoint();
        assertEquals(expectedOutput, out.output);
    }
}
