/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.semantics;

import compiler.NbCompiler;
import error.ErrorHandler;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nbm.Code;
import nbm.Nbm.Opcode;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.NoBeardParser;
import scanner.Scanner;
import scanner.SrcFileReader;
import scanner.SrcReader;
import scanner.SrcStringReader;
import symlist.SymListManager;

/**
 *
 * @author peter
 */
public class NoBeardParserTest {

    private NbCompiler comp;
    private Code code;
    private SymListManager sym;
    private Scanner scanner;
    private NoBeardParser p;

    public NoBeardParserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of parse method, of class NoBeardParser.
     */
    @Test
    public void testBlockIdentMismatch() {
        System.out.println("testBlockIdentMismatch");

        setupTest(new SrcStringReader("unit foo; do put (x); done fox;"));

        ErrorHandler eh = comp.getErrorHandler();
        assertFalse("False expected", p.parse());
        assertEquals("Sem err count", 1, eh.getCount(error.Error.ErrorClass.SEMANTICAL));
        assertEquals("Last error", error.Error.ErrorType.OPERAND_KIND_EXPECTED.getNumber(), eh.getLastError().getNumber());
    }

    @Test
    public void testParseEmpty() {
        System.out.println("parse");

        byte[] expected = {
            Opcode.INC.byteCode(), 0, 0,
            Opcode.HALT.byteCode()
        };

        setupTest(new SrcStringReader("unit foo; do done foo;"));


        assertTrue("True expected", p.parse());
        assertCodeEquals("Code ", expected, code.getByteCode());
    }

    @Test
    public void testParse() {
        System.out.println("parse");

        byte[] expected1 = {
            Opcode.INC.byteCode(), 0, 4,
            Opcode.LA.byteCode(), 0, 0, 32,
            Opcode.LIT.byteCode(), 0, 3,
            Opcode.STO.byteCode(),
            Opcode.HALT.byteCode()
        };
        setupTest(new SrcStringReader("unit foo; do int x = 3; done foo;"));

        assertTrue("True expected", p.parse());
        assertCodeEquals("Code ", expected1, code.getByteCode());

        byte[] expected2 = {
            Opcode.INC.byteCode(), 0, 4,
            Opcode.LA.byteCode(), 0, 0, 32,
            Opcode.LIT.byteCode(), 0, 3,
            Opcode.STO.byteCode(),
            Opcode.LV.byteCode(), 0, 0, 32,
            Opcode.LIT.byteCode(), 0, 0,
            Opcode.PUT.byteCode(), 0,
            Opcode.HALT.byteCode()
        };

        setupTest(new SrcStringReader("unit bah; do int x = 3; put (x); done bah;"));

        assertTrue("True expected", p.parse());
        assertCodeEquals("Code ", expected2, code.getByteCode());

        byte[] expected3 = {
            Opcode.INC.byteCode(), 0, 8,
            Opcode.LA.byteCode(), 0, 0, 32,
            Opcode.LIT.byteCode(), 0, 3,
            Opcode.STO.byteCode(),
            Opcode.LA.byteCode(), 0, 0, 36,
            Opcode.LIT.byteCode(), 0, 1,
            Opcode.STO.byteCode(),
            Opcode.LV.byteCode(), 0, 0, 32,
            Opcode.LV.byteCode(), 0, 0, 36,
            Opcode.ADD.byteCode(),
            Opcode.LIT.byteCode(), 0, 0,
            Opcode.PUT.byteCode(), 0,
            Opcode.HALT.byteCode()
        };

        setupTest(new SrcStringReader("unit rsch; do int x = 3; int y = 1; put (x + y); done rsch;"));


        assertTrue("True expected", p.parse());
        assertCodeEquals("Code ", expected3, code.getByteCode());
    }

    @Test
    public void testParseChar() {
        System.out.println("testParseChar");

        byte[] expected = {
            Opcode.INC.byteCode(), 0, 1,
            Opcode.LA.byteCode(), 0, 0, 32,
            Opcode.LIT.byteCode(), 0, 120, // ascii code of 'x'
            Opcode.STC.byteCode(),
            Opcode.LC.byteCode(), 0, 0, 32,
            Opcode.LIT.byteCode(), 0, 1,
            Opcode.PUT.byteCode(), 1,
            Opcode.HALT.byteCode()
        };
        setupTest(new SrcStringReader("unit foo; do char x = \"x\"; put(x); done foo;"));
        assertEquals("Parse: ", true, p.parse());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, code.getByteCode());
    }

    @Test
    public void testHelloWorld() {
        System.out.println("testHelloWorld");
        
        byte[] expected = {
            Opcode.INC.byteCode(), 0, 0,
            Opcode.LIT.byteCode(), 0, 0,    // address of string
            Opcode.LIT.byteCode(), 0, 11,   // length of string
            Opcode.LIT.byteCode(), 0, 11,   // width parameter of print instr.
            Opcode.PUT.byteCode(), 2,
            Opcode.HALT.byteCode()
        };
        
        try {
            setupTest(new SrcFileReader("SamplePrograms/HelloWorld.nb"));
            
            assertEquals("Parse: ", true, p.parse());
            AssemblerCodeChecker.assertCodeEquals("Code ", expected, code.getByteCode());
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NoBeardParserTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testVariableWorld() {
        System.out.println("testVariableWorld");
        
        byte[] expected = {
            Opcode.INC.byteCode(), 0, 29,   // stack pointer plus size of vars
            Opcode.LA.byteCode(), 0, 0, 32, // address of string x
            Opcode.LIT.byteCode(), 0, 0,    // address of source string
            Opcode.LIT.byteCode(), 0, 12,   // length of string
            Opcode.ASSN.byteCode(),         // Array assignment
            Opcode.LA.byteCode(), 0, 0, 32, // Load address of var to print
            Opcode.LIT.byteCode(), 0, 12,   // Length of string
            Opcode.LIT.byteCode(), 0, 12,   // Width parameter of print instr.
            Opcode.PUT.byteCode(), 2,       // Output of a string
            Opcode.LA.byteCode(), 0, 0, 44, // address of string y
            Opcode.LIT.byteCode(), 0, 12,   // address of source string
            Opcode.LIT.byteCode(), 0, 17,   // length of second string
            Opcode.ASSN.byteCode(),         // Array assignment
            Opcode.HALT.byteCode()
        };
        
        try {
            setupTest(new SrcFileReader("SamplePrograms/VariableWorld.nb"));
            
            assertEquals("Parse: ", true, p.parse());
            AssemblerCodeChecker.assertCodeEquals("Code ", expected, code.getByteCode());
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NoBeardParserTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void assertCodeEquals(String msg, byte[] exp, byte[] act) {
        AssemblerCodeChecker.assertCodeEquals(msg, exp, act);
    }

    private void setupTest(SrcReader src) {
        comp = new NbCompiler(src);
        scanner = comp.getScanner();
        sym = comp.getSymListManager();
        code = comp.getCode();
        p = comp.getParser();
    }
}
