/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.semantics;

import nbm.Nbm.Opcode;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
     * Test of parse method, of class PutStatParser.
     */
    @Test
    public void testParsePutInt() {
        System.out.println("testParsePutInt");

        byte[] expected = {
            Opcode.LIT.byteCode(), 0, 5,        // int value to be putted
            Opcode.LIT.byteCode(), 0, 0,        // column width
            Opcode.PUT.byteCode(), 0            // put int
        };

        PutStatParser instance = PutStatParserTestSetup.getPutIntSetup();
        assertTrue("Parse: ", instance.parse());
        AssmCodeChecker.assertCodeEquals("Code ", expected, PutStatParserTestSetup.getCode().getByteCode());

    }

    /**
     * Test of parse method, of class PutStatParser.
     */
    @Test
    public void testParsePutString() {
        System.out.println("testParsePutString");

        byte[] expected = {
            Opcode.LIT.byteCode(), 0, 0,    // address of string
            Opcode.LIT.byteCode(), 0, 6,    // length of string
            Opcode.LIT.byteCode(), 0, 6,    // width of column
            Opcode.PUT.byteCode(), 2        // put string
        };

        PutStatParser instance = PutStatParserTestSetup.getPutStringSetup();
        assertTrue("Parse: ", instance.parse());
        AssmCodeChecker.assertCodeEquals("Code ", expected, PutStatParserTestSetup.getCode().getByteCode());

    }

    /**
     * Test of parse method, of class PutStatParser.
     */
    @Test
    public void testParsePutln() {
        System.out.println("testParsePutln");

        byte[] expected = {
            Opcode.PUT.byteCode(), 3
        };

        PutStatParser instance = PutStatParserTestSetup.getPutlnSetup();
        assertTrue("Parse: ", instance.parse());
        AssmCodeChecker.assertCodeEquals("Code ", expected, PutStatParserTestSetup.getCode().getByteCode());

    }
}
