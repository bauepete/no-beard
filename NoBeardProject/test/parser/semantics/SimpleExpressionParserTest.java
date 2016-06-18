    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.semantics;

import nbm.Nbm.Opcode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.SimpleExpressionParser;
import parser.general.SimpleExpressionParserTestSetup;

/**
 *
 * @author peter
 */
public class SimpleExpressionParserTest {

    public SimpleExpressionParserTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test add expression
     */
    @Test
    public void testAdd() {

        byte[] expected = {
            Opcode.LV.byteCode(), 0, 0, 32,
            Opcode.LV.byteCode(), 0, 0, 36,
            Opcode.ADD.byteCode()
        };

        SimpleExpressionParser p = SimpleExpressionParserTestSetup.getAddTestSetup();

        assertEquals("Parse ", true, p.parse());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, SimpleExpressionParserTestSetup.getByteCode());
    }

    /**
     * Test of parseOldStyle method, of class SimpleExpressionParser.
     */
    @Test
    public void testSub() {
        System.out.println("testSub");

        byte[] expected = {
            Opcode.LV.byteCode(), 0, 0, 32,
            Opcode.LV.byteCode(), 0, 0, 36,
            Opcode.SUB.byteCode()
        };

        SimpleExpressionParser p = SimpleExpressionParserTestSetup.getSubTestSetup();

        assertEquals("Parse ", true, p.parseOldStyle());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, SimpleExpressionParserTestSetup.getByteCode());
    }

    /**
     * Test of parseOldStyle method, of class SimpleExpressionParser.
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

        SimpleExpressionParser p = SimpleExpressionParserTestSetup.getNegAddTestSetup();

        assertEquals("Parse ", true, p.parseOldStyle());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, SimpleExpressionParserTestSetup.getByteCode());
    }

    /**
     * Test of parseOldStyle method, of class SimpleExpressionParser.
     */
    @Test
    public void testNeg() {
        System.out.println("testNeg");

        byte[] expected = {
            Opcode.LV.byteCode(), 0, 0, 32,
            Opcode.NEG.byteCode(),};

        SimpleExpressionParser p = SimpleExpressionParserTestSetup.getNegTestSetup();

        assertEquals("Parse ", true, p.parseOldStyle());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, SimpleExpressionParserTestSetup.getByteCode());
    }

    /**
     * Test of parseOldStyle method, of class SimpleExpressionParser.
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

        SimpleExpressionParser p = SimpleExpressionParserTestSetup.getAddMulTestSetup();

        assertEquals("Parse ", true, p.parseOldStyle());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, SimpleExpressionParserTestSetup.getByteCode());
    }

    /**
     * Test of parseOldStyle method, of class SimpleExpressionParser.
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
            Opcode.NEG.byteCode(),};

        SimpleExpressionParser p = SimpleExpressionParserTestSetup.getComplexExprTestSetup();

        assertEquals("Parse ", true, p.parseOldStyle());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, SimpleExpressionParserTestSetup.getByteCode());
    }

    /**
     * Test of parseOldStyle method, of class SimpleExpressionParser.
     */
    @Test
    public void testOrExpr() {
        System.out.println("testOrExpr");
        // a || b || c
        
        byte[] expected = {
            Opcode.LV.byteCode(), 0, 0, 32,
            Opcode.TJMP.byteCode(), 0, 21,
            Opcode.LV.byteCode(), 0, 0, 36,
            Opcode.TJMP.byteCode(), 0, 21,
            Opcode.LV.byteCode(), 0, 0, 40,
            Opcode.JMP.byteCode(), 0, 24,
            Opcode.LIT.byteCode(), 0, 1
        };
        
        SimpleExpressionParser p = SimpleExpressionParserTestSetup.getOrExprTestSetup();
        assertTrue(p.parseOldStyle());
        AssemblerCodeChecker.assertCodeEquals("Code", expected, SimpleExpressionParserTestSetup.getByteCode());
    }
}
