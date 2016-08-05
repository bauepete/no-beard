    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.semantics;

import nbm.InstructionSet.Opcode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.ParserFactory;
import parser.AddExpressionParser;
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

        AddExpressionParser p = SimpleExpressionParserTestSetup.getAddTestSetup();

        assertEquals("Parse ", true, p.parse());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, SimpleExpressionParserTestSetup.getByteCode());
    }

    /**
     * Test subtraction expression.
     */
    @Test
    public void testSub() {
        System.out.println("testSub");

        byte[] expected = {
            Opcode.LV.byteCode(), 0, 0, 32,
            Opcode.LV.byteCode(), 0, 0, 36,
            Opcode.SUB.byteCode()
        };

        AddExpressionParser p = SimpleExpressionParserTestSetup.getSubTestSetup();

        assertEquals("Parse ", true, p.parse());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, SimpleExpressionParserTestSetup.getByteCode());
    }

    /**
     * Test an expression with a negation in front.
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

        AddExpressionParser p = SimpleExpressionParserTestSetup.getNegAddTestSetup();

        assertEquals("Parse ", true, p.parse());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, SimpleExpressionParserTestSetup.getByteCode());
    }

    /**
     * Test a single variable with a preceding minus.
     */
    @Test
    public void testNeg() {
        System.out.println("testNeg");

        byte[] expected = {
            Opcode.LV.byteCode(), 0, 0, 32,
            Opcode.NEG.byteCode(),};

        AddExpressionParser p = SimpleExpressionParserTestSetup.getNegTestSetup();

        assertEquals("Parse ", true, p.parse());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, SimpleExpressionParserTestSetup.getByteCode());
    }

    /**
     * Test a simple expression with a subtraction and a multiplication.
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

        AddExpressionParser p = SimpleExpressionParserTestSetup.getAddMulTestSetup();

        assertEquals("Parse ", true, p.parse());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, SimpleExpressionParserTestSetup.getByteCode());
    }

    /**
     * Test a simple expression with sub expression.
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

        AddExpressionParser p = SimpleExpressionParserTestSetup.getComplexExprTestSetup();

        assertEquals("Parse ", true, p.parse());
        AssemblerCodeChecker.assertCodeEquals("Code ", expected, SimpleExpressionParserTestSetup.getByteCode());
    }

    /**
     * Test a boolean expression using or.
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
        
        AddExpressionParser p = SimpleExpressionParserTestSetup.getOrExprTestSetup();
        assertTrue(p.parse());
        AssemblerCodeChecker.assertCodeEquals("Code", expected, SimpleExpressionParserTestSetup.getByteCode());
    }
    
    @Test
    public void testMixedOperands() {
        AddExpressionParser p = SimpleExpressionParserTestSetup.getMixedOperandSetup();
        assertFalse(p.parse());
        assertEquals(error.Error.ErrorType.OPERATOR_OPERAND_TYPE_MISMATCH.getNumber(), ParserFactory.getErrorHandler().getAllErrors().get(0).getNumber());
    }
}
