/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.semantics;

import machine.InstructionSet.Instruction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.ExpressionParser;
import parser.general.ExpressionParserTestSetup;

/**
 *
 * @author peter
 */
public class ExpressionParserTest {
    
    public ExpressionParserTest() {
    }

    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of parseOldStyle method, of class ExpressionParser.
     */
    @Test
    public void testParseSimpleRel() {
        System.out.println("parseSimpleRel");
        byte[] expected = {
            Instruction.LV.getId(), 0, 0, 32,
            Instruction.LV.getId(), 0, 0, 36,
            Instruction.REL.getId(), 0
        };
        ExpressionParser p = ExpressionParserTestSetup.getSimpleRel();
        assertTrue(p.parse());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, ExpressionParserTestSetup.getByteCode());
    }

    /**
     * Test of parseOldStyle method, of class ExpressionParser.
     */
    @Test
    public void testParseAndRel() {
        System.out.println("parseAndRel");
        byte[] expected = {
            Instruction.LV.getId(), 0, 0, 32, // 0: (a <= b)
            Instruction.LV.getId(), 0, 0, 36, // 4:
            Instruction.REL.getId(), 1,       // 8:
            Instruction.FJMP.getId(), 0, 25,  // 10: &&
            Instruction.LV.getId(), 0, 0, 36, // 13: (b == 1)
            Instruction.LIT.getId(), 0, 1,    // 17:
            Instruction.REL.getId(), 2,       // 20:
            Instruction.JMP.getId(), 0, 28,   // 22: finalize and expression
            Instruction.LIT.getId(), 0, 0     // 25:
        };
        ExpressionParser p = ExpressionParserTestSetup.getAndRel();
        assertTrue(p.parse());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, ExpressionParserTestSetup.getByteCode());
    }

    /**
     * Test of parseOldStyle method, of class ExpressionParser.
     */
    @Test
    public void testParseOrRel() {
        System.out.println("parseOrRel");
        byte[] expected = {
            Instruction.LV.getId(), 0, 0, 32, // 0: (a != b)
            Instruction.LV.getId(), 0, 0, 36,
            Instruction.REL.getId(), 3,
            Instruction.TJMP.getId(), 0, 25,  // 10: &&
            Instruction.LV.getId(), 0, 0, 36, // 13: (b >= 1)
            Instruction.LIT.getId(), 0, 1,
            Instruction.REL.getId(), 4,
            Instruction.JMP.getId(), 0, 28,
            Instruction.LIT.getId(), 0, 1
        };
        ExpressionParser p = ExpressionParserTestSetup.getOrRel();
        assertTrue(p.parse());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, ExpressionParserTestSetup.getByteCode());
    }

    /**
     * Test of parseOldStyle method, of class ExpressionParser.
     */
    @Test
    public void testParseAndOrRel() {
        System.out.println("parseAndOrRel");
        // ((a < b) && (b > 1)) || (a < 0)
        byte[] expected = {
            Instruction.LV.getId(), 0, 0, 32, // 0: (a < b)
            Instruction.LV.getId(), 0, 0, 36,
            Instruction.REL.getId(), 0,
            Instruction.FJMP.getId(), 0, 25,  // 10: &&
            Instruction.LV.getId(), 0, 0, 36, // 13: (b > 1)
            Instruction.LIT.getId(), 0, 1,    // 17:
            Instruction.REL.getId(), 5,       // 20: 
            Instruction.JMP.getId(), 0, 28,   // 22:
            Instruction.LIT.getId(), 0, 0,    // 25:
            Instruction.TJMP.getId(), 0, 43,  // 28: ||
            Instruction.LV.getId(), 0, 0, 40, // 31: (c < 0)
            Instruction.LIT.getId(), 0, 0,    // 35:
            Instruction.REL.getId(), 0,       // 38:
            Instruction.JMP.getId(), 0, 46,   // 40:
            Instruction.LIT.getId(), 0, 1     // 43:
            
        };
        ExpressionParser p = ExpressionParserTestSetup.getAndOrRel();
        assertTrue(p.parse());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, ExpressionParserTestSetup.getByteCode());
    }
}
