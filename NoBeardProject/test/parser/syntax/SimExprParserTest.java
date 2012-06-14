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
import parser.SimExprParser;
import parser.general.ExprParserTestSetup;

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
        SimExprParser p = ExprParserTestSetup.getAddTestSetup();
        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testSub() {
        System.out.println("testSub");
        SimExprParser p = ExprParserTestSetup.getSubTestSetup();
        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testNegAdd() {
        System.out.println("testNegAdd");
        SimExprParser p = ExprParserTestSetup.getNegAddTestSetup();
        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testNeg() {
        System.out.println("testNeg");
        SimExprParser p = ExprParserTestSetup.getNegTestSetup();
        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testAddMul() {
        System.out.println("testAddMul");
        SimExprParser p = ExprParserTestSetup.getAddMulTestSetup();
        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testComplexExpr() {
        System.out.println("testComplexExpr");
        SimExprParser p = ExprParserTestSetup.getComplexExprTestSetup();
        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testNoExpr() {
        System.out.println("testNoExpr");
        SimExprParser p = ExprParserTestSetup.getNoExprTestSetup();
        assertEquals("Parse ", false, p.parse());
    }
}
