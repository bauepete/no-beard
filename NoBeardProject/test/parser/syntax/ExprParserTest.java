/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.syntax;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import parser.ExprParser;
import parser.general.ExprParserTestSetup;

/**
 *
 * @author peter
 */
public class ExprParserTest {

    public ExprParserTest() {
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
     * Test of parse method, of class ExprParser.
     */
    @Test
    public void testAdd() {
        System.out.println("testAdd");
        ExprParser p = ExprParserTestSetup.getAddTestSetup();
        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testSub() {
        System.out.println("testSub");
        ExprParser p = ExprParserTestSetup.getSubTestSetup();
        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testNegAdd() {
        System.out.println("testNegAdd");
        ExprParser p = ExprParserTestSetup.getNegAddTestSetup();
        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testNeg() {
        System.out.println("testNeg");
        ExprParser p = ExprParserTestSetup.getNegTestSetup();
        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testAddMul() {
        System.out.println("testAddMul");
        ExprParser p = ExprParserTestSetup.getAddMulTestSetup();
        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testComplexExpr() {
        System.out.println("testComplexExpr");
        ExprParser p = ExprParserTestSetup.getComplexExprTestSetup();
        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testNoExpr() {
        System.out.println("testNoExpr");
        ExprParser p = ExprParserTestSetup.getNoExprTestSetup();
        assertEquals("Parse ", false, p.parse());
    }
}
