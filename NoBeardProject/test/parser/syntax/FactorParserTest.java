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
import parser.general.FactorParserTestSetup;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author peter
 */
public class FactorParserTest {

    public FactorParserTest() {
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
        FactorParser p = FactorParserTestSetup.getIdentifierTestSetup();
        assertTrue(p.parse());
    }

    @Test
    public void testParseNoFact() {
        FactorParser p = FactorParserTestSetup.getNoFactSetup();

        assertFalse(p.parse());
        assertEquals("Last error", ErrorType.SYMBOL_EXPECTED.getNumber(), p.getErrorHandler().getLastError().getNumber());
    }

    @Test
    public void testParseNumber() {
        FactorParser p = FactorParserTestSetup.getNumberTestSetup();
        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testParseString() {
        System.out.append("testParseString");
        FactorParser p = FactorParserTestSetup.getStringTestSetup();
        assertEquals("Parse ", true, p.parse());
    }

    @Test
    @Ignore
    public void testParseExpr() {
        System.out.println("testParseExpr");
        FactorParser p = FactorParserTestSetup.getExprSetup();
        assertEquals("Parse ", true, p.parseOldStyle());
    }
}
