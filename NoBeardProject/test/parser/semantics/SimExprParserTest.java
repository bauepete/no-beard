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
import parser.SimExprParser;
import parser.general.SimExprParserTestSetup;

/**
 *
 * @author peter
 */
public class SimExprParserTest {

    public SimExprParserTest() {
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
     * Test of parse method, of class SimExprParser.
     */
    @Test
    public void testAdd() {
        System.out.println("testAdd");
        
        byte[] expected = {
            Opcode.LV.byteCode(), 0, 0, 32,
            Opcode.LV.byteCode(), 0, 0, 36,
            Opcode.ADD.byteCode()
        };

        SimExprParser p = SimExprParserTestSetup.getAddTestSetup();

        assertEquals("Parse ", true, p.parse());
        AssmCodeChecker.assertCodeEquals("Code ", expected, SimExprParserTestSetup.getCode().getByteCode());
    }

    /**
     * Test of parse method, of class SimExprParser.
     */
    @Test
    public void testSub() {
        System.out.println("testSub");
        
        byte[] expected = {
            Opcode.LV.byteCode(), 0, 0, 32,
            Opcode.LV.byteCode(), 0, 0, 36,
            Opcode.SUB.byteCode()
        };

        SimExprParser p = SimExprParserTestSetup.getSubTestSetup();

        assertEquals("Parse ", true, p.parse());
        AssmCodeChecker.assertCodeEquals("Code ", expected, SimExprParserTestSetup.getCode().getByteCode());
    }

    /**
     * Test of parse method, of class SimExprParser.
     */
    @Test
    public void testNegAdd() {
        System.out.println("testNegAdd");
        
        byte[] expected = {
            Opcode.LV.byteCode(), 0, 0, 32,
            Opcode.NEG.byteCode(),
            Opcode.LV.byteCode(), 0, 0, 36,
            Opcode.ADD.byteCode()
        };

        SimExprParser p = SimExprParserTestSetup.getNegAddTestSetup();

        assertEquals("Parse ", true, p.parse());
        AssmCodeChecker.assertCodeEquals("Code ", expected, SimExprParserTestSetup.getCode().getByteCode());
    }

    /**
     * Test of parse method, of class SimExprParser.
     */
    @Test
    public void testNeg() {
        System.out.println("testNeg");
        
        byte[] expected = {
            Opcode.LV.byteCode(), 0, 0, 32,
            Opcode.NEG.byteCode(),
        };

        SimExprParser p = SimExprParserTestSetup.getNegTestSetup();

        assertEquals("Parse ", true, p.parse());
        AssmCodeChecker.assertCodeEquals("Code ", expected, SimExprParserTestSetup.getCode().getByteCode());
    }

    /**
     * Test of parse method, of class SimExprParser.
     */
    @Test
    public void testParseAddMul() {
        System.out.println("testParseAddMul");
        
        byte[] expected = {
            Opcode.LV.byteCode(), 0, 0, 32,
            Opcode.LV.byteCode(), 0, 0, 36,
            Opcode.LIT.byteCode(), 0, 3,
            Opcode.MUL.byteCode(),
            Opcode.SUB.byteCode()
        };

        SimExprParser p = SimExprParserTestSetup.getAddMulTestSetup();

        assertEquals("Parse ", true, p.parse());
        AssmCodeChecker.assertCodeEquals("Code ", expected, SimExprParserTestSetup.getCode().getByteCode());
    }

    /**
     * Test of parse method, of class SimExprParser.
     */
    @Test
    public void testComplexExpr() {
        System.out.println("testComplexExpr");
        // "-5 * (a + b)/17"
        byte[] expected = {
            Opcode.LIT.byteCode(), 0, 5,
            Opcode.LV.byteCode(), 0, 0, 32,
            Opcode.LV.byteCode(), 0, 0, 36,
            Opcode.ADD.byteCode(),
            Opcode.MUL.byteCode(),
            Opcode.LIT.byteCode(), 0, 17,
            Opcode.DIV.byteCode(),
            Opcode.NEG.byteCode(),
        };

        SimExprParser p = SimExprParserTestSetup.getComplexExprTestSetup();

        assertEquals("Parse ", true, p.parse());
        AssmCodeChecker.assertCodeEquals("Code ", expected, SimExprParserTestSetup.getCode().getByteCode());
    }
}
