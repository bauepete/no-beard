/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nbm;

import parser.NoBeardParser;
import compiler.NoBeardCompiler;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import scanner.SrcFileReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

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

    @After
    public void tearDown() {
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
    @Ignore
    public void testAddInt() {
        try {
            NoBeardCompiler comp = new NoBeardCompiler(new SrcFileReader("SamplePrograms/AddInt.nb"));
            NoBeardParser p = comp.getParser();
            boolean parseOk = p.parseOldStyle();
            
            if (parseOk) {
                CodeGenerator c = comp.getCode();
                m.loadProg(0, c.getByteCode());
                m.loadDat(0, comp.getStringStorage());
                m.runProg(0);
                assertEquals("Stack top ", Nbm.STACKEMPTY, m.getStackTopValue());
            }
            else {
                comp.getErrorHandler().printSummary();
                System.err.println("Compilation not successfull. Can't run program.");
                fail("Program must compile successfully.");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NbmTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    @Ignore
    public void testComplexExpr() {
        try {
            NoBeardCompiler comp = new NoBeardCompiler(new SrcFileReader("SamplePrograms/ComplexExpr.nb"));
            NoBeardParser p = comp.getParser();
            boolean parseOk = p.parseOldStyle();
            
            if (parseOk) {
                CodeGenerator c = comp.getCode();
                m.loadProg(0, c.getByteCode());
                m.loadDat(0, comp.getStringStorage());
                m.runProg(0);
                assertEquals("Stack top ", Nbm.STACKEMPTY, m.getStackTopValue());
            }
            else {
                comp.getErrorHandler().printSummary();
                System.err.println("Compilation not successfull. Can't run program.");
                fail("Program must compile successfully.");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NbmTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    @Ignore
    public void testPutChar() {
        try {
            NoBeardCompiler comp = new NoBeardCompiler(new SrcFileReader("SamplePrograms/PutChar.nb"));
            NoBeardParser p = comp.getParser();
            boolean parseOk = p.parseOldStyle();
            
            if (parseOk) {
                CodeGenerator c = comp.getCode();
                m.loadProg(0, c.getByteCode());
                m.runProg(0);
                assertEquals("Stack top ", Nbm.STACKEMPTY, m.getStackTopValue());
            }
            else {
                comp.getErrorHandler().printSummary();
                System.err.println("Compilation not successfull. Can't run program.");
                fail("Program must compile successfully.");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NbmTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    @Ignore
    public void testHelloWorld() {
        try {
            NoBeardCompiler comp = new NoBeardCompiler(new SrcFileReader("SamplePrograms/HelloWorld.nb"));
            NoBeardParser p = comp.getParser();
            boolean parseOk = p.parseOldStyle();
            
            if (parseOk) {
                CodeGenerator c = comp.getCode();
                m.loadProg(0, c.getByteCode());
                m.loadDat(0, comp.getStringStorage());
                m.runProg(0);
                assertEquals("Stack top ", Nbm.STACKEMPTY, m.getStackTopValue());
            }
            else {
                comp.getErrorHandler().printSummary();
                System.err.println("Compilation not successfull. Can't run program.");
                fail("Program must compile successfully.");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NbmTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    @Ignore
    public void testVariableWorld() {
        System.out.println("testVariableWorld");
        try {
            NoBeardCompiler comp = new NoBeardCompiler(new SrcFileReader("SamplePrograms/VariableWorld.nb"));
            NoBeardParser p = comp.getParser();
            boolean parseOk = p.parseOldStyle();
            
            if (parseOk) {
                CodeGenerator c = comp.getCode();
                m.loadProg(0, c.getByteCode());
                m.loadDat(0, comp.getStringStorage());
                m.runProg(0);
                assertEquals("Stack top", Nbm.STACKEMPTY, m.getStackTopValue());
            }
            else {
                comp.getErrorHandler().printSummary();
                System.err.print("Compilation not successfull. Can't run program.");
                fail("Program must compile successfully.");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NbmTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
