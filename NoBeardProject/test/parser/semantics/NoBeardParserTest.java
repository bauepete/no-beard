/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.semantics;

import compiler.NbCompiler;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nbm.CodeGenerator;
import nbm.Nbm.Opcode;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import parser.NoBeardParser;
import parser.Parser;
import parser.ParserFactory;
import parser.general.NoBeardParserTestSetup;
import scanner.SrcFileReader;
import scanner.SrcReader;
import scanner.SrcStringReader;

/**
 *
 * @author peter
 */
public class NoBeardParserTest {

    private NbCompiler comp;
    private CodeGenerator code;
    private NoBeardParser p;

    public NoBeardParserTest() {
    }

    @Test
    public void testParseEmpty() {
        byte[] expected = {
            Opcode.INC.byteCode(), 0, 0,
            Opcode.HALT.byteCode()
        };

        Parser instance = NoBeardParserTestSetup.getEmptyProgramSetup();
        assertTrue("True expected", instance.parse());
        assertCodeEquals("Code ", expected, ParserFactory.getCodeGenerator().getByteCode());
    }

    /**
     * Test that block identifiers do not match at begin and end.
     */
    @Test
    public void testBlockIdentMismatch() {
        Parser instance = NoBeardParserTestSetup.getBlockIdentifierNameMismatch();

        assertFalse("False expected", instance.parse());
        assertEquals(error.Error.ErrorType.BLOCK_NAME_MISSMATCH.getNumber(), ParserFactory.getErrorHandler().getLastError().getNumber());
    }

    @Test
    @Ignore
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

        assertTrue("True expected", p.parseOldStyle());
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

        assertTrue("True expected", p.parseOldStyle());
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


        assertTrue("True expected", p.parseOldStyle());
        assertCodeEquals("Code ", expected3, code.getByteCode());
    }

    @Test
    @Ignore
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
        assertEquals("Parse: ", true, p.parseOldStyle());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, code.getByteCode());
    }

    @Test
    @Ignore
    public void testHelloWorld() {
        System.out.println("testHelloWorld");
        
        byte[] expected = {
            Opcode.INC.byteCode(), 0, 0,
            Opcode.LIT.byteCode(), 0, 0,    // address of string
            Opcode.LIT.byteCode(), 0, 11,   // length of string
            Opcode.LIT.byteCode(), 0, 11,   // width parameter of print instr.
            Opcode.PUT.byteCode(), 2,
            Opcode.PUT.byteCode(), 3,
            Opcode.HALT.byteCode()
        };
        
        try {
            setupTest(new SrcFileReader("SamplePrograms/HelloWorld.nb"));
            
            assertEquals("Parse: ", true, p.parseOldStyle());
            AssemblerCodeChecker.assertCodeEquals("Code ", expected, code.getByteCode());
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NoBeardParserTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    @Ignore
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
            
            assertEquals("Parse: ", true, p.parseOldStyle());
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
        code = comp.getCode();
        p = comp.getParser();
    }
}
