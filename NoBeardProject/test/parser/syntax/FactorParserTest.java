/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.syntax;

import error.Error.ErrorType;
import parser.FactorParser;
import org.junit.Test;
import parser.general.FactorParserTestSetup;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class FactorParserTest {

    public FactorParserTest() {
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
        FactorParser p = FactorParserTestSetup.getStringTestSetup();
        assertEquals("Parse ", true, p.parse());
    }

    @Test
    public void testParseExpr() {
        FactorParser p = FactorParserTestSetup.getExprSetup();
        assertEquals("Parse ", true, p.parse());
    }
}
