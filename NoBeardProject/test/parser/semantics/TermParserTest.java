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
import parser.TermParser;
import parser.general.TermParserTestSetup;

/**
 *
 * @author peter
 */
public class TermParserTest {

    public TermParserTest() {
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
     * Test of parse method, of class TermParser.
     */
    @Test
    public void testParseMul() {
        System.out.println("testParseMul");
        byte[] expected = {
            Opcode.LV.byteCode(), 0, 0, 32,
            Opcode.LV.byteCode(), 0, 0, 36,
            Opcode.MUL.byteCode(),
            Opcode.LV.byteCode(), 0, 0, 40,
            Opcode.MUL.byteCode()
        };

        TermParser p = TermParserTestSetup.getMulTermSetup();
        assertEquals("Parse", true, p.parse());
        AssmCodeChecker.assertCodeEquals("Code ", expected, TermParserTestSetup.getCode().getByteCode());
    }

    /**
     * Test of parse method, of class TermParser.
     */
    @Test
    public void testParseDiv() {
        System.out.println("testParseDiv");

        byte[] expected = {
            Opcode.LIT.byteCode(), 0, 1,
            Opcode.LIT.byteCode(), 0, 2,
            Opcode.DIV.byteCode(),
            Opcode.LV.byteCode(), 0, 0, 32,
            Opcode.DIV.byteCode()
        };

        TermParser p = TermParserTestSetup.getDivTermSetup();
        assertEquals("Parse", true, p.parse());
        AssmCodeChecker.assertCodeEquals("Code ", expected, TermParserTestSetup.getCode().getByteCode());
    }
    
    /**
     * Test of parse method, of class TermParser.
     */
    @Test
    public void testParseMod() {
        System.out.println("testParseMod");

        byte[] expected = {
            Opcode.LIT.byteCode(), 0, 10,
            Opcode.LV.byteCode(), 0, 0, 32,
            Opcode.MOD.byteCode(),
            Opcode.LV.byteCode(), 0, 0, 36,
            Opcode.MOD.byteCode()
        };

        TermParser p = TermParserTestSetup.getModTermSetup();
        assertEquals("Parse", true, p.parse());
        AssmCodeChecker.assertCodeEquals("Code ", expected, TermParserTestSetup.getCode().getByteCode());
    }
    
    /**
     * Test boolean "and"-term
     */
    
    @Test
    public void testParseAnd() {
        System.out.println("testParseAnd");
        
        byte[] expected = {
            Opcode.LV.byteCode(), 0, 0, 32,
            Opcode.FJMP.byteCode(), 0, 21,
            Opcode.LV.byteCode(), 0, 0, 36,
            Opcode.FJMP.byteCode(), 0, 21,
            Opcode.LV.byteCode(), 0, 0, 40,
            Opcode.JMP.byteCode(), 0, 24,
            Opcode.LIT.byteCode(), 0, 0
        };
        
        TermParser p = TermParserTestSetup.getAndTermSetup();
        assertTrue("Parse", p.parse());
        AssmCodeChecker.assertCodeEquals("Code", expected, TermParserTestSetup.getCode().getByteCode());
    }
}
