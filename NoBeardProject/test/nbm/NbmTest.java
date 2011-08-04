/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nbm;

import parser.NoBeardParser;
import compiler.NbCompiler;
import error.ErrorHandler;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import scanner.SrcFileReader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class NbmTest {

    private Nbm m;

    public NbmTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        m = new Nbm();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testInitialization() {
        System.out.println("testInitialization");
        assertTrue(m.getState() == Nbm.MachineState.RUN);
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
        try {
            NbCompiler comp = new NbCompiler(new SrcFileReader("SamplePrograms/AddInt.nb"));
            NoBeardParser p = comp.getParser();
            boolean parseOk = p.parse();
            
            if (parseOk) {
                Code c = comp.getCode();
                m.loadProg(0, c.getByteCode());
                m.runProg(0);
                assertEquals("Stack top ", Nbm.STACKEMPTY, m.getStackTopValue());
            }
            else {
                ErrorHandler.getInstance().printSummary();
                System.err.println("Compilation not successfull. Can't run program.");
                fail("Program must compile successfully.");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NbmTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testComplexExpr() {
        try {
            NbCompiler comp = new NbCompiler(new SrcFileReader("SamplePrograms/ComplexExpr.nb"));
            NoBeardParser p = comp.getParser();
            boolean parseOk = p.parse();
            
            if (parseOk) {
                Code c = comp.getCode();
                m.loadProg(0, c.getByteCode());
                m.runProg(0);
                assertEquals("Stack top ", Nbm.STACKEMPTY, m.getStackTopValue());
            }
            else {
                ErrorHandler.getInstance().printSummary();
                System.err.println("Compilation not successfull. Can't run program.");
                fail("Program must compile successfully.");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NbmTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
