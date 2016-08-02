/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.semantics;

import nbm.Nbm.Opcode;
import parser.general.IfStatParserTestSetup;
import parser.IfParser;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import parser.ParserFactory;

/**
 *
 * @author peter
 */
public class IfParserTest {

    public IfParserTest() {
    }

    /**
     * Test simple if statement.
     */
    @Test
    public void testSimpleIf() {
        byte[] expected = {
            Opcode.LV.byteCode(), 0, 0, 32, // load x
            Opcode.LIT.byteCode(), 0, 0, // load 0
            Opcode.REL.byteCode(), 2, // check equality
            Opcode.FJMP.byteCode(), 0, 23, // skip block if false
            Opcode.INC.byteCode(), 0, 0, // No local variables in if block
            Opcode.LIT.byteCode(), 0, 48, // ascii value of '0'
            Opcode.LIT.byteCode(), 0, 0, // width of column
            Opcode.PUT.byteCode(), 1 // put simple char
        };

        IfParser instance = IfStatParserTestSetup.getSimpleIfTestSetup();
        assertTrue(instance.parse());
        AssemblerCodeChecker.assertCodeEquals(expected, IfStatParserTestSetup.getByteCode());
    }
    
    @Test
    public void testNonBoolExpressionInIf() {
        IfParser instance = IfStatParserTestSetup.getIfWithBadConditionTestSetup();
        assertFalse(instance.parse());
        assertEquals(error.Error.ErrorType.OPERATOR_OPERAND_TYPE_MISMATCH.getNumber(), ParserFactory.getErrorHandler().getLastError().getNumber());
    }

    /**
     * Test if / else statement.
     */
    @Test
    public void testIfElse() {
        byte[] expected = {
            Opcode.LV.byteCode(), 0, 0, 32, // load x
            Opcode.LIT.byteCode(), 0, 0, // load 0
            Opcode.REL.byteCode(), 2, // check equality
            Opcode.FJMP.byteCode(), 0, 26, // skip block if false
            Opcode.INC.byteCode(), 0, 0, // no local variables in if block
            Opcode.LIT.byteCode(), 0, 48, // ascii value of '0'
            Opcode.LIT.byteCode(), 0, 0, // width of column
            Opcode.PUT.byteCode(), 1, // put simple char
            Opcode.JMP.byteCode(), 0, 37, // if is finished -> skip else
            Opcode.INC.byteCode(), 0, 0, // again no local variables in else block
            Opcode.LIT.byteCode(), 0, 49, // ascii value of '1'
            Opcode.LIT.byteCode(), 0, 0, // width of column
            Opcode.PUT.byteCode(), 1, // put simple char
        };
        IfParser instance = IfStatParserTestSetup.getIfElseTestSetup();
        assertTrue(instance.parse());
        AssemblerCodeChecker.assertCodeEquals(expected, IfStatParserTestSetup.getByteCode());
    }
}
