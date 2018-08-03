/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.semantics;

import machine.InstructionSet.Instruction;
import parser.general.IfParserTestSetup;
import parser.IfParser;
import org.junit.Test;
import static org.junit.Assert.*;
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
            Instruction.LV.getId(), 0, 0, 32, // load x
            Instruction.LIT.getId(), 0, 0, // load 0
            Instruction.REL.getId(), 2, // check equality
            Instruction.FJMP.getId(), 0, 23, // skip block if false
            Instruction.INC.getId(), 0, 0, // No local variables in if block
            Instruction.LIT.getId(), 0, 48, // ascii value of '0'
            Instruction.LIT.getId(), 0, 1, // width of column
            Instruction.OUT.getId(), 1 // put simple char
        };

        IfParser instance = IfParserTestSetup.getSimpleIfTestSetup();
        assertTrue(instance.parse());
        AssemblerCodeChecker.assertCodeEquals(expected, IfParserTestSetup.getByteCode());
    }
    
    @Test
    public void testNonBoolExpressionInIf() {
        IfParser instance = IfParserTestSetup.getIfWithBadConditionTestSetup();
        assertFalse(instance.parse());
        assertEquals(error.Error.ErrorType.OPERATOR_OPERAND_TYPE_MISMATCH.getNumber(), ParserFactory.getErrorHandler().getLastError().getNumber());
    }

    /**
     * Test if / else statement.
     */
    @Test
    public void testIfElse() {
        byte[] expected = {
            Instruction.LV.getId(), 0, 0, 32, // load x
            Instruction.LIT.getId(), 0, 0, // load 0
            Instruction.REL.getId(), 2, // check equality
            Instruction.FJMP.getId(), 0, 26, // skip block if false
            Instruction.INC.getId(), 0, 0, // no local variables in if block
            Instruction.LIT.getId(), 0, 48, // ascii value of '0'
            Instruction.LIT.getId(), 0, 1, // width of column
            Instruction.OUT.getId(), 1, // put simple char
            Instruction.JMP.getId(), 0, 37, // if is finished -> skip else
            Instruction.INC.getId(), 0, 0, // again no local variables in else block
            Instruction.LIT.getId(), 0, 49, // ascii value of '1'
            Instruction.LIT.getId(), 0, 1, // width of column
            Instruction.OUT.getId(), 1, // put simple char
        };
        IfParser instance = IfParserTestSetup.getIfElseTestSetup();
        assertTrue(instance.parse());
        AssemblerCodeChecker.assertCodeEquals(expected, IfParserTestSetup.getByteCode());
    }
}
