/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.syntax;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
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
    public void testParseSimpleRel() {
        System.out.println("parseSimpleRel");
        ExprParser p = ExprParserTestSetup.getSimpleRel();
        assertTrue(p.parse());
    }

    /**
     * Test of parse method, of class ExprParser.
     */
    @Test
    public void testParseAndRel() {
        System.out.println("parseAndRel");
        ExprParser p = ExprParserTestSetup.getAndRel();
        assertTrue(p.parse());
    }

    /**
     * Test of parse method, of class ExprParser.
     */
    @Test
    public void testParseOrRel() {
        System.out.println("parseOrRel");
        ExprParser p = ExprParserTestSetup.getOrRel();
        assertTrue(p.parse());
    }

    /**
     * Test of parse method, of class ExprParser.
     */
    @Test
    public void testParseAndOrRel() {
        System.out.println("parseAndOrRel");
        ExprParser p = ExprParserTestSetup.getAndOrRel();
        assertTrue(p.parse());
    }
}
