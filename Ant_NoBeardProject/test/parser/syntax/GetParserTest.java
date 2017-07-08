/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser.syntax;

import org.junit.Test;
import static org.junit.Assert.*;
import parser.GetParser;
import parser.general.GetParserTestSetup;
import parser.general.PutParserTestSetup;
import scanner.Scanner;

/**
 *
 * @author peter
 */
public class GetParserTest {
    
    public GetParserTest() {
    }

    /**
     * Test of parseSpecificPart method, of class GetParser.
     */
    @Test
    public void testParseGetInt() {
        GetParser p = GetParserTestSetup.getGetIntSetup();
        assertTrue(p.parse());
        assertEquals(Scanner.Symbol.EOFSY, PutParserTestSetup.getScanner().getCurrentToken().getSymbol());
    }
    
}
