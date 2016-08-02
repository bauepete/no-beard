/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.syntax;

import org.junit.Test;
import static org.junit.Assert.*;
import parser.IfParser;
import parser.general.IfStatParserTestSetup;
import parser.general.ParserTestSetup;
import scanner.Scanner;

/**
 *
 * @author peter
 */
public class IfParserTest {

    public IfParserTest() {
    }

    /**
     * Test a simple if statement.
     */
    @Test
    public void testSimpleIf() {
        IfParser instance = IfStatParserTestSetup.getSimpleIfTestSetup();
        assertTrue(instance.parse());
        assertEquals(Scanner.Symbol.EOFSY, ParserTestSetup.getScanner().getCurrentToken().getSymbol());
    }

    /**
     * Test of parseOldStyle method, of class IfParser.
     */
    @Test
    public void testIfElse() {
        IfParser instance = IfStatParserTestSetup.getIfElseTestSetup();
        assertTrue(instance.parse());
        assertEquals(Scanner.Symbol.EOFSY, ParserTestSetup.getScanner().getCurrentToken().getSymbol());
    }
}
