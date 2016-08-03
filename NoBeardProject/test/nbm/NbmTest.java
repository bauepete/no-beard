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
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author peter
 */
public class NbmTest {

    private Nbm m;

    public NbmTest() {
    }
    
    @Before
    public void setUp() {
        m = new Nbm();
    }

    @Test
    public void testInitialization() {
        System.out.println("testInitialization");
        assertTrue("State", m.getState() == Nbm.MachineState.RUN);
    }

    @Test
    public void testLoadDat() {
        System.out.println("testLoadDat");
        assertTrue("Expected to store 4 bytes", m.loadDat(0, new byte[4]));
        assertTrue("Expected to store MAXDAT bytes", m.loadDat(0, new byte[Nbm.getMAXDAT()]));
        assertFalse("Expected to fail to store more than MAXDAT bytes", m.loadDat(0, new byte[Nbm.getMAXDAT() + 1]));
        assertTrue("Expected to store last byte", m.loadDat(Nbm.getMAXDAT() - 1, new byte[1]));
        assertFalse("Expected to fail over MAXDAT", m.loadDat(Nbm.getMAXDAT() - 1, new byte[2]));
    }

    @Test
    public void testExecCycle() {
        System.out.println("testExecCycle");

        byte[] p = {
            Nbm.Opcode.LIT.byteCode(), 0, 2,
            Nbm.Opcode.PUT.byteCode(), 0,
            Nbm.Opcode.HALT.byteCode()
        };
        m.loadProg(0, p);

        m.execCycle();
        assertEquals(2, m.getStackTopValue());
        assertTrue("Expected machine state RUN", m.getState() == Nbm.MachineState.RUN);

        m.execCycle();
        assertEquals(Nbm.STACKEMPTY, m.getStackTopValue());
        assertTrue("Expected machine state RUN", m.getState() == Nbm.MachineState.RUN);

        m.execCycle();
        assertEquals(Nbm.STACKEMPTY, m.getStackTopValue());
        assertTrue("Expected machine state STOP", m.getState() == Nbm.MachineState.STOP);
    }

    @Test
    public void testAddInt() {
        NoBeardCompiler.setSourceFile("SamplePrograms/AddInt.nb");
        boolean parseOk = NoBeardCompiler.compile();

        if (parseOk) {
            m.loadProg(0, NoBeardCompiler.getByteCode());
            m.loadDat(0, NoBeardCompiler.getStringStore());
            m.runProg(0);
            assertEquals("Stack top ", Nbm.STACKEMPTY, m.getStackTopValue());
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
            m.loadDat(0, NoBeardCompiler.getStringStore());

            m.runProg(0);
            assertEquals("Stack top ", Nbm.STACKEMPTY, m.getStackTopValue());
        } else {
            System.err.println("Compilation not successfull. Can't run program.");
            fail("Program must compile successfully.");
        }
    }
}
