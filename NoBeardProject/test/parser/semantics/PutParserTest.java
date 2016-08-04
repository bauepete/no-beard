/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.semantics;

import nbm.NoBeardMachine.Opcode;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.PutParser;
import parser.general.PutParserTestSetup;

/**
 *
 * @author peter
 */
public class PutParserTest {

    public PutParserTest() {
    }

    /**
     * Test of output an integer constant.
     */
    @Test
    public void testParsePutInt() {
        byte[] expected = {
            Opcode.LIT.byteCode(), 0, 5,        // int value to be putted
            Opcode.LIT.byteCode(), 0, 1,        // column width
            Opcode.PUT.byteCode(), 0            // put int
        };

        PutParser instance = PutParserTestSetup.getPutIntSetup();
        assertTrue(instance.parse());
        AssemblerCodeChecker.assertCodeEquals(expected, PutParserTestSetup.getCode().getByteCode());

    }

    /**
     * Test output of a char constant.
     */
    @Test
    public void testParsePutChar() {
        byte[] expected = {
            Opcode.LIT.byteCode(), 0, 97,       // decimal ascii value of 'a' to be putted
            Opcode.LIT.byteCode(), 0, 1,        // column width
            Opcode.PUT.byteCode(), 1            // put simple char
        };

        PutParser instance = PutParserTestSetup.getPutCharSetup();
        assertTrue(instance.parse());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, PutParserTestSetup.getCode().getByteCode());

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

        PutParser instance = PutParserTestSetup.getPutStringSetup();
        assertTrue(instance.parse());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, PutParserTestSetup.getCode().getByteCode());

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
        
        PutParser instance = PutParserTestSetup.getPutIntWithColumnWidthSetup();
        assertTrue(instance.parse());
        AssemblerCodeChecker.assertCodeEquals(expectedCode, PutParserTestSetup.getByteCode());
    }
    
    @Test
    public void testPutStringWithColumnWidth() {
        byte[] expectedCode = {
            Opcode.LIT.byteCode(), 0, 0,    // address of string
            Opcode.LIT.byteCode(), 0, 6,    // length of string
            Opcode.LIT.byteCode(), 0, 17,   // widths of column
            Opcode.PUT.byteCode(), 2        // put string
        };
        
        PutParser instance = PutParserTestSetup.getPutStringWithColumnWidthSetup();
        assertTrue(instance.parse());
        AssemblerCodeChecker.assertCodeEquals(expectedCode, PutParserTestSetup.getByteCode());
    }

    /**
     * Test putln.
     */
    @Test
    public void testParsePutln() {
        byte[] expected = {
            Opcode.PUT.byteCode(), 3
        };

        PutParser instance = PutParserTestSetup.getPutlnSetup();
        assertTrue(instance.parse());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, PutParserTestSetup.getCode().getByteCode());

    }
    
    @Test
    public void testParsePutWithUndefinedIdentifier() {
        PutParser instance = PutParserTestSetup.getPutWithUndefinedIdentifier();
        assertFalse(instance.parse());
    }
    
    @Test
    public void testPutWithNonPuttableIdentifier() {
        PutParser instance = PutParserTestSetup.getPutWithNonPuttableIdentifier();
        assertFalse(instance.parse());
    }
}
