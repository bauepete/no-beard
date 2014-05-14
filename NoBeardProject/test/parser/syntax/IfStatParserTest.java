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
import parser.IfStatParser;
import parser.general.IfStatParserTestSetup;

/**
 *
 * @author peter
 */
public class IfStatParserTest {

    public IfStatParserTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of parse method, of class IfStatParser.
     */
    @Test
    public void testSimpleIf() {
        System.out.println("parseSimpleIf");
        IfStatParser instance = IfStatParserTestSetup.getSimpleIfTestSetup();
        assertTrue(instance.parse());
    }

    /**
     * Test of parse method, of class IfStatParser.
     */
    @Test
    public void testIfElse() {
        System.out.println("testIfElse");
        IfStatParser instance = IfStatParserTestSetup.getIfElseTestSetup();
        assertTrue(instance.parse());
    }
}
