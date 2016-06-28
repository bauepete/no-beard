/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.semantics;

import nbm.Nbm.Opcode;
import parser.general.IfStatParserTestSetup;
import parser.IfParser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author peter
 */
@Ignore
public class IfParserTest {
    
    public IfParserTest() {
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
     * Test of parseOldStyle method, of class IfParser.
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
        
        IfParser instance = IfStatParserTestSetup.getSimpleIfTestSetup();
        assertTrue(instance.parseOldStyle());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, IfStatParserTestSetup.getByteCode());
    }

    /**
     * Test of parseOldStyle method, of class IfParser.
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
        IfParser instance = IfStatParserTestSetup.getIfElseTestSetup();
        assertTrue(instance.parseOldStyle());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, IfStatParserTestSetup.getByteCode());
    }
}
