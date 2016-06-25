/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.syntax;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
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
     * Test of parseOldStyle method, of class AddExpressionParser.
     */
    @Test
    public void testAdd() {
        System.out.println("testAdd");
        AddExpressionParser p = SimpleExpressionParserTestSetup.getAddTestSetup();
        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testSub() {
        System.out.println("testSub");
        AddExpressionParser p = SimpleExpressionParserTestSetup.getSubTestSetup();
        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testNegAdd() {
        System.out.println("testNegAdd");
        AddExpressionParser p = SimpleExpressionParserTestSetup.getNegAddTestSetup();
        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testNeg() {
        System.out.println("testNeg");
        AddExpressionParser p = SimpleExpressionParserTestSetup.getNegTestSetup();
        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testAddMul() {
        System.out.println("testAddMul");
        AddExpressionParser p = SimpleExpressionParserTestSetup.getAddMulTestSetup();
        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testComplexExpr() {
        System.out.println("testComplexExpr");
        AddExpressionParser p = SimpleExpressionParserTestSetup.getComplexExprTestSetup();
        assertEquals("Parse ", true, p.parseOldStyle());
    }

    @Test
    public void testNoExpr() {
        System.out.println("testNoExpr");
        AddExpressionParser p = SimpleExpressionParserTestSetup.getNoExprTestSetup();
        assertEquals("Parse ", false, p.parse());
    }
    
    @Test
    public void testOrExpr() {
        System.out.println("testOrExpr");
        AddExpressionParser p = SimpleExpressionParserTestSetup.getOrExprTestSetup();
        assertTrue(p.parse());
    }
}
