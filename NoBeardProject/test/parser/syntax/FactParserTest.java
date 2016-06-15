/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.syntax;

import error.Error.ErrorType;
import parser.FactorParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import parser.general.FactParserTestSetup;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author peter
 */
public class FactParserTest {

    public FactParserTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of parseOldStyle method, of class FactorParser.
     */
    @Test
    public void testParseIdentifier() {
        FactorParser p = FactParserTestSetup.getIdentifierTestSetup();
        assertTrue(p.parse());
    }

    @Test
    public void testParseNoFact() {
        FactorParser p = FactParserTestSetup.getNoFactSetup();

        assertFalse(p.parse());
        assertEquals("Last error", ErrorType.SYMBOL_EXPECTED.getNumber(), p.getErrorHandler().getLastError().getNumber());
    }

    @Test
    public void testParseNumber() {
        FactorParser p = FactParserTestSetup.getNumberTestSetup();
        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testParseString() {
        System.out.append("testParseString");
        FactorParser p = FactParserTestSetup.getStringTestSetup();
        assertEquals("Parse ", true, p.parse());
    }

    @Test
    @Ignore
    public void testParseExpr() {
        System.out.println("testParseExpr");
        FactorParser p = FactParserTestSetup.getExprSetup();
        assertEquals("Parse ", true, p.parseOldStyle());
    }
}
