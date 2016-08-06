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
package nbm;

import compiler.NoBeardCompiler;
import nbm.InstructionSet.Opcode;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author peter
 */
public class NoBeardMachineTest {

    private NoBeardMachine m;

    public NoBeardMachineTest() {
    }
    
    @Before
    public void setUp() {
        m = new NoBeardMachine();
    }

    @Test
    public void testInitialization() {
        System.out.println("testInitialization");
        assertTrue("State", m.getState() == NoBeardMachine.State.RUN);
    }

    @Test
    public void testLoadStringConstants() {
        m.loadStringConstants(new byte[4]);
        assertEquals(NoBeardMachine.State.RUN, m.getState());
        assertEquals(4, m.getAddressOfFirstFrame());
    }
    
    @Test
    public void testLoadMaxNumberOfStrings() {
        m.loadStringConstants(new byte[NoBeardMachine.getMAXDAT()]);
        assertEquals(NoBeardMachine.State.RUN, m.getState());
    }
    
    @Test
    public void testStoreStringsOverflow() {
        m.loadStringConstants(new byte[NoBeardMachine.getMAXDAT() + 1]);
        assertEquals(NoBeardMachine.State.ERROR, m.getState());
        assertEquals(error.Error.ErrorType.DATA_ADDRESS_ERROR.getNumber(), m.getError().getNumber());
    }

    @Test
    public void testExecCycle() {
        System.out.println("testExecCycle");

        byte[] p = {
            Opcode.LIT.byteCode(), 0, 2,
            Opcode.PUT.byteCode(), 0,
            Opcode.HALT.byteCode()
        };
        m.loadProg(0, p);

        m.execCycle();
        assertEquals(2, m.getStackTopValue());
        assertTrue("Expected machine state RUN", m.getState() == NoBeardMachine.State.RUN);

        m.execCycle();
        assertEquals(NoBeardMachine.STACKEMPTY, m.getStackTopValue());
        assertTrue("Expected machine state RUN", m.getState() == NoBeardMachine.State.RUN);

        m.execCycle();
        assertEquals(NoBeardMachine.STACKEMPTY, m.getStackTopValue());
        assertTrue("Expected machine state STOP", m.getState() == NoBeardMachine.State.STOP);
    }

    @Test
    public void testAddInt() {
        NoBeardCompiler.setSourceFile("SamplePrograms/AddInt.nb");
        boolean parseOk = NoBeardCompiler.compile();

        if (parseOk) {
            m.loadProg(0, NoBeardCompiler.getByteCode());
            m.loadStringConstants(NoBeardCompiler.getStringStore());
            m.runProg(0);
            assertEquals("Stack top ", NoBeardMachine.STACKEMPTY, m.getStackTopValue());
        } else {
            System.err.println("Compilation not successfull. Can't run program.");
            fail("Program must compile successfully.");
        }
    }

    @Test
    public void testComplexExpr() {
        NoBeardCompiler.setSourceFile("SamplePrograms/ComplexExpr.nb");
        boolean parseOk = NoBeardCompiler.compile();
        if (parseOk) {
            m.loadProg(0, NoBeardCompiler.getByteCode());
            m.loadStringConstants(NoBeardCompiler.getStringStore());

            m.runProg(0);
            assertEquals("Stack top ", NoBeardMachine.STACKEMPTY, m.getStackTopValue());
        } else {
            System.err.println("Compilation not successfull. Can't run program.");
            fail("Program must compile successfully.");
        }
    }
}
