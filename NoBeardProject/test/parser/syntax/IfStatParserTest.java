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
import parser.IfStatParser;
import parser.general.IfStatParserTestSetup;

/**
 *
 * @author peter
 */
@Ignore
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
     * Test of parseOldStyle method, of class IfStatParser.
     */
    @Test
    public void testSimpleIf() {
        System.out.println("parseSimpleIf");
        IfStatParser instance = IfStatParserTestSetup.getSimpleIfTestSetup();
        assertTrue(instance.parseOldStyle());
    }

    /**
     * Test of parseOldStyle method, of class IfStatParser.
     */
    @Test
    public void testIfElse() {
        System.out.println("testIfElse");
        IfStatParser instance = IfStatParserTestSetup.getIfElseTestSetup();
        assertTrue(instance.parseOldStyle());
    }
}
