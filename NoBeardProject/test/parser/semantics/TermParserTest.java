/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.semantics;

import machine.InstructionSet.Instruction;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.ParserFactory;
import parser.TermParser;
import parser.general.TermParserTestSetup;

/**
 *
 * @author peter
 */
public class TermParserTest {

    public TermParserTest() {
    }

    /**
     * Tests parsing a multiplication.
     */
    @Test
    public void testParseMul() {
        System.out.println("testParseMul");
        byte[] expected = {
            Instruction.LV.getId(), 0, 0, 32,
            Instruction.LV.getId(), 0, 0, 36,
            Instruction.MUL.getId(),
            Instruction.LV.getId(), 0, 0, 40,
            Instruction.MUL.getId()
        };

        TermParser p = TermParserTestSetup.getMulTermSetup();
        assertEquals("Parse", true, p.parse());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, TermParserTestSetup.getByteCode());
    }

    /**
     * Tests parsing a division.
     */
    @Test
    public void testParseDiv() {
        System.out.println("testParseDiv");

        byte[] expected = {
            Instruction.LIT.getId(), 0, 1,
            Instruction.LIT.getId(), 0, 2,
            Instruction.DIV.getId(),
            Instruction.LV.getId(), 0, 0, 32,
            Instruction.DIV.getId()
        };

        TermParser p = TermParserTestSetup.getDivTermSetup();
        assertEquals("Parse", true, p.parse());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, TermParserTestSetup.getByteCode());
    }
    
    /**
     * Tests parsing the mod operation.
     */
    @Test
    public void testParseMod() {
        System.out.println("testParseMod");

        byte[] expected = {
            Instruction.LIT.getId(), 0, 10,
            Instruction.LV.getId(), 0, 0, 32,
            Instruction.MOD.getId(),
            Instruction.LV.getId(), 0, 0, 36,
            Instruction.MOD.getId()
        };

        TermParser p = TermParserTestSetup.getModTermSetup();
        assertEquals("Parse", true, p.parse());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, TermParserTestSetup.getByteCode());
    }
    
    /**
     * Test boolean "and"-term
     */
    
    @Test
    public void testParseAnd() {
        System.out.println("testParseAnd");
        
        byte[] expected = {
            Instruction.LV.getId(), 0, 0, 32,
            Instruction.FJMP.getId(), 0, 21,
            Instruction.LV.getId(), 0, 0, 36,
            Instruction.FJMP.getId(), 0, 21,
            Instruction.LV.getId(), 0, 0, 40,
            Instruction.JMP.getId(), 0, 24,
            Instruction.LIT.getId(), 0, 0
        };
        
        TermParser p = TermParserTestSetup.getAndTermSetup();
        assertTrue("Parse", p.parse());
        AssemblerCodeChecker.assertCodeEquals("Code", expected, TermParserTestSetup.getByteCode());
    }
    
    @Test
    public void testOperatorOperandTypeMismatch() {
        TermParser p = TermParserTestSetup.getTypeMismatchSetup();
        
        assertFalse(p.parse());
        assertEquals("Operator and requires a bool operand", ParserFactory.getErrorHandler().getAllErrors().get(0).getMessage());
    }
}
