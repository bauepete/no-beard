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
import org.junit.Ignore;
import parser.SimExprParser;
import parser.general.SimExprParserTestSetup;

/**
 *
 * @author peter
 */
@Ignore
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
     * Test of parseOldStyle method, of class SimExprParser.
     */
    @Test
    public void testAdd() {
        System.out.println("testAdd");
        SimExprParser p = SimExprParserTestSetup.getAddTestSetup();
        assertEquals("Parse ", true, p.parseOldStyle());
    }

    @Test
    public void testSub() {
        System.out.println("testSub");
        SimExprParser p = SimExprParserTestSetup.getSubTestSetup();
        assertEquals("Parse ", true, p.parseOldStyle());
    }

    @Test
    public void testNegAdd() {
        System.out.println("testNegAdd");
        SimExprParser p = SimExprParserTestSetup.getNegAddTestSetup();
        assertEquals("Parse ", true, p.parseOldStyle());
    }

    @Test
    public void testNeg() {
        System.out.println("testNeg");
        SimExprParser p = SimExprParserTestSetup.getNegTestSetup();
        assertEquals("Parse ", true, p.parseOldStyle());
    }

    @Test
    public void testAddMul() {
        System.out.println("testAddMul");
        SimExprParser p = SimExprParserTestSetup.getAddMulTestSetup();
        assertEquals("Parse ", true, p.parseOldStyle());
    }

    @Test
    public void testComplexExpr() {
        System.out.println("testComplexExpr");
        SimExprParser p = SimExprParserTestSetup.getComplexExprTestSetup();
        assertEquals("Parse ", true, p.parseOldStyle());
    }

    @Test
    public void testNoExpr() {
        System.out.println("testNoExpr");
        SimExprParser p = SimExprParserTestSetup.getNoExprTestSetup();
        assertEquals("Parse ", false, p.parseOldStyle());
    }
    
    @Test
    public void testOrExpr() {
        System.out.println("testOrExpr");
        SimExprParser p = SimExprParserTestSetup.getOrExprTestSetup();
        assertTrue(p.parseOldStyle());
    }
}
