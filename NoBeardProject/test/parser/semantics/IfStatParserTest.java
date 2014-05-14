/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.semantics;

import nbm.Nbm.Opcode;
import parser.general.IfStatParserTestSetup;
import parser.IfStatParser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class IfStatParserTest {
    
    public IfStatParserTest() {
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
     * Test of parse method, of class IfStatParser.
     */
    @Test
    public void testSimpleIf() {
        System.out.println("parseSimpleIf");
        byte[] expected = {
            Opcode.LV.byteCode(), 0, 0, 32,
            Opcode.LIT.byteCode(), 0, 0,
            Opcode.REL.byteCode(), 2,
            Opcode.FJMP.byteCode(), 0, 23,
            Opcode.INC.byteCode(), 0, 0,
            Opcode.LIT.byteCode(), 0, 48,   // ascii value of '0'
            Opcode.LIT.byteCode(), 0, 1,    // width of column
            Opcode.PUT.byteCode(), 1        // put simple char
        };
        
        IfStatParser instance = IfStatParserTestSetup.getSimpleIfTestSetup();
        assertTrue(instance.parse());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, IfStatParserTestSetup.byteCode());
    }

    /**
     * Test of parse method, of class IfStatParser.
     */
    @Test
    public void testIfElse() {
        System.out.println("testIfElse");
        byte[] expected = {
            Opcode.LV.byteCode(), 0, 0, 32,
            Opcode.LIT.byteCode(), 0, 0,
            Opcode.REL.byteCode(), 2,
            Opcode.FJMP.byteCode(), 0, 26,
            Opcode.INC.byteCode(), 0, 0,
            Opcode.LIT.byteCode(), 0, 48,   // ascii value of '0'
            Opcode.LIT.byteCode(), 0, 1,    // width of column
            Opcode.PUT.byteCode(), 1,       // put simple char
            Opcode.JMP.byteCode(), 0, 37,   // if is finished
            Opcode.INC.byteCode(), 0, 0,
            Opcode.LIT.byteCode(), 0, 49,   // ascii value of '1'
            Opcode.LIT.byteCode(), 0, 1,    // width of column
            Opcode.PUT.byteCode(), 1,       // put simple char
        };
        IfStatParser instance = IfStatParserTestSetup.getIfElseTestSetup();
        assertTrue(instance.parse());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, IfStatParserTestSetup.byteCode());
    }
}
