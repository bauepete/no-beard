/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.syntax;

import org.junit.Test;
import static org.junit.Assert.*;
import parser.AddExpressionParser;
import parser.general.SimpleExpressionParserTestSetup;

/**
 *
 * @author peter
 */
public class SimpleExpressionParserTest {

    public SimpleExpressionParserTest() {
    }

    @Test
    public void testAdd() {
        AddExpressionParser p = SimpleExpressionParserTestSetup.getAddTestSetup();
        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testSub() {
        AddExpressionParser p = SimpleExpressionParserTestSetup.getSubTestSetup();
        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testNegAdd() {
        AddExpressionParser p = SimpleExpressionParserTestSetup.getNegAddTestSetup();
        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testNeg() {
        AddExpressionParser p = SimpleExpressionParserTestSetup.getNegTestSetup();
        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testAddMul() {
        AddExpressionParser p = SimpleExpressionParserTestSetup.getAddMulTestSetup();
        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testComplexExpr() {
        AddExpressionParser p = SimpleExpressionParserTestSetup.getComplexExprTestSetup();
        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testNoExpr() {
        AddExpressionParser p = SimpleExpressionParserTestSetup.getNoExprTestSetup();
        assertEquals("Parse ", false, p.parse());
    }
    
    @Test
    public void testOrExpr() {
        AddExpressionParser p = SimpleExpressionParserTestSetup.getOrExprTestSetup();
        assertTrue(p.parse());
    }
}
