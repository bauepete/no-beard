/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.semantics;

import nbm.Nbm.Opcode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.PutStatParser;
import parser.general.PutStatParserTestSetup;

/**
 *
 * @author peter
 */
public class PutStatParserTest {

    public PutStatParserTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of output an integer constant.
     */
    @Test
    public void testParsePutInt() {
        byte[] expected = {
            Opcode.LIT.byteCode(), 0, 5,        // int value to be putted
            Opcode.LIT.byteCode(), 0, 0,        // column width
            Opcode.PUT.byteCode(), 0            // put int
        };

        PutStatParser instance = PutStatParserTestSetup.getPutIntSetup();
        assertTrue(instance.parse());
        AssemblerCodeChecker.assertCodeEquals(expected, PutStatParserTestSetup.getCode().getByteCode());

    }

    /**
     * Test output of a char constant.
     */
    @Test
    public void testParsePutChar() {
        byte[] expected = {
            Opcode.LIT.byteCode(), 0, 97,       // decimal ascii value of 'a' to be putted
            Opcode.LIT.byteCode(), 0, 0,        // column width
            Opcode.PUT.byteCode(), 1            // put simple char
        };

        PutStatParser instance = PutStatParserTestSetup.getPutCharSetup();
        assertTrue(instance.parse());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, PutStatParserTestSetup.getCode().getByteCode());

    }

    /**
     * Test output of string.
     */
    @Test
    public void testParsePutString() {
        byte[] expected = {
            Opcode.LIT.byteCode(), 0, 0,    // address of string
            Opcode.LIT.byteCode(), 0, 6,    // length of string
            Opcode.LIT.byteCode(), 0, 6,    // width of column
            Opcode.PUT.byteCode(), 2        // put string
        };

        PutStatParser instance = PutStatParserTestSetup.getPutStringSetup();
        assertTrue(instance.parse());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, PutStatParserTestSetup.getCode().getByteCode());

    }
    
    /**
     * Test output of int specifying a column width (char is similar).
     */
    @Test
    public void testPutWithColumnWidth() {
        byte[] expectedCode = {
            Opcode.LIT.byteCode(), 0, 42,   // value
            Opcode.LIT.byteCode(), 0, 17,   // column width
            Opcode.PUT.byteCode(), 0        // put int
        };
        
        PutStatParser instance = PutStatParserTestSetup.getPutIntWithColumnWidthSetup();
        assertTrue(instance.parse());
        AssemblerCodeChecker.assertCodeEquals(expectedCode, PutStatParserTestSetup.getByteCode());
    }
    
    @Test
    public void testPutStringWithColumnWidth() {
        byte[] expectedCode = {
            Opcode.LIT.byteCode(), 0, 0,    // address of string
            Opcode.LIT.byteCode(), 0, 6,    // length of string
            Opcode.LIT.byteCode(), 0, 17,   // widths of column
            Opcode.PUT.byteCode(), 2        // put string
        };
        
        PutStatParser instance = PutStatParserTestSetup.getPutStringWithColumnWidthSetup();
        assertTrue(instance.parse());
        AssemblerCodeChecker.assertCodeEquals(expectedCode, PutStatParserTestSetup.getByteCode());
    }

    /**
     * Test putln.
     */
    @Test
    public void testParsePutln() {
        byte[] expected = {
            Opcode.PUT.byteCode(), 3
        };

        PutStatParser instance = PutStatParserTestSetup.getPutlnSetup();
        assertTrue(instance.parse());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, PutStatParserTestSetup.getCode().getByteCode());

    }
}
