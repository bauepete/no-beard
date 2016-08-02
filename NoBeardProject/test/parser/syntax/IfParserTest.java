/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.syntax;

import org.junit.Test;
import static org.junit.Assert.*;
import parser.IfParser;
import parser.general.IfParserTestSetup;
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
        IfParser instance = IfParserTestSetup.getSimpleIfTestSetup();
        assertTrue(instance.parse());
        assertEquals(Scanner.Symbol.EOFSY, ParserTestSetup.getScanner().getCurrentToken().getSymbol());
    }

    /**
     * Test of parseOldStyle method, of class IfParser.
     */
    @Test
    public void testIfElse() {
        IfParser instance = IfParserTestSetup.getIfElseTestSetup();
        assertTrue(instance.parse());
        assertEquals(Scanner.Symbol.EOFSY, ParserTestSetup.getScanner().getCurrentToken().getSymbol());
    }
}
