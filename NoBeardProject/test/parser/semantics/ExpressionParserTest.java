/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.semantics;

import nbm.ControlUnit.Opcode;
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
            Opcode.LV.byteCode(), 0, 0, 32,
            Opcode.LV.byteCode(), 0, 0, 36,
            Opcode.REL.byteCode(), 0
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
            Opcode.LV.byteCode(), 0, 0, 32, // 0: (a <= b)
            Opcode.LV.byteCode(), 0, 0, 36, // 4:
            Opcode.REL.byteCode(), 1,       // 8:
            Opcode.FJMP.byteCode(), 0, 25,  // 10: &&
            Opcode.LV.byteCode(), 0, 0, 36, // 13: (b == 1)
            Opcode.LIT.byteCode(), 0, 1,    // 17:
            Opcode.REL.byteCode(), 2,       // 20:
            Opcode.JMP.byteCode(), 0, 28,   // 22: finalize and expression
            Opcode.LIT.byteCode(), 0, 0     // 25:
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
            Opcode.LV.byteCode(), 0, 0, 32, // 0: (a != b)
            Opcode.LV.byteCode(), 0, 0, 36,
            Opcode.REL.byteCode(), 3,
            Opcode.TJMP.byteCode(), 0, 25,  // 10: &&
            Opcode.LV.byteCode(), 0, 0, 36, // 13: (b >= 1)
            Opcode.LIT.byteCode(), 0, 1,
            Opcode.REL.byteCode(), 4,
            Opcode.JMP.byteCode(), 0, 28,
            Opcode.LIT.byteCode(), 0, 1
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
            Opcode.LV.byteCode(), 0, 0, 32, // 0: (a < b)
            Opcode.LV.byteCode(), 0, 0, 36,
            Opcode.REL.byteCode(), 0,
            Opcode.FJMP.byteCode(), 0, 25,  // 10: &&
            Opcode.LV.byteCode(), 0, 0, 36, // 13: (b > 1)
            Opcode.LIT.byteCode(), 0, 1,    // 17:
            Opcode.REL.byteCode(), 5,       // 20: 
            Opcode.JMP.byteCode(), 0, 28,   // 22:
            Opcode.LIT.byteCode(), 0, 0,    // 25:
            Opcode.TJMP.byteCode(), 0, 43,  // 28: ||
            Opcode.LV.byteCode(), 0, 0, 40, // 31: (c < 0)
            Opcode.LIT.byteCode(), 0, 0,    // 35:
            Opcode.REL.byteCode(), 0,       // 38:
            Opcode.JMP.byteCode(), 0, 46,   // 40:
            Opcode.LIT.byteCode(), 0, 1     // 43:
            
        };
        ExpressionParser p = ExpressionParserTestSetup.getAndOrRel();
        assertTrue(p.parse());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, ExpressionParserTestSetup.getByteCode());
    }
}
