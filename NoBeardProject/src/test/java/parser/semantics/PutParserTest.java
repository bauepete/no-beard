/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.semantics;

import machine.InstructionSet.Instruction;
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
            Instruction.LIT.getId(), 0, 5,        // int value to be putted
            Instruction.LIT.getId(), 0, 1,        // column width
            Instruction.OUT.getId(), 0            // put int
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
            Instruction.LIT.getId(), 0, 97,       // decimal ascii value of 'a' to be putted
            Instruction.LIT.getId(), 0, 1,        // column width
            Instruction.OUT.getId(), 1            // put simple char
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
            Instruction.LIT.getId(), 0, 0,    // address of string
            Instruction.LIT.getId(), 0, 6,    // length of string
            Instruction.LIT.getId(), 0, 6,    // width of column
            Instruction.OUT.getId(), 2        // put string
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
            Instruction.LIT.getId(), 0, 42,   // value
            Instruction.LIT.getId(), 0, 17,   // column width
            Instruction.OUT.getId(), 0        // put int
        };
        
        PutParser instance = PutParserTestSetup.getPutIntWithColumnWidthSetup();
        assertTrue(instance.parse());
        AssemblerCodeChecker.assertCodeEquals(expectedCode, PutParserTestSetup.getByteCode());
    }
    
    @Test
    public void testPutStringWithColumnWidth() {
        byte[] expectedCode = {
            Instruction.LIT.getId(), 0, 0,    // address of string
            Instruction.LIT.getId(), 0, 6,    // length of string
            Instruction.LIT.getId(), 0, 17,   // widths of column
            Instruction.OUT.getId(), 2        // put string
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
            Instruction.OUT.getId(), 3
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
