/*
 * Copyright ©2011 - 2016. Created by P. Bauer (p.bauer@htl-leonding.ac.at),
 * Department of Informatics and Media Technique, HTBLA Leonding,
 * Limesstr. 12 - 14, 4060 Leonding, AUSTRIA. All Rights Reserved. Permission
 * to use, copy, modify, and distribute this software and its documentation
 * for educational, research, and not-for-profit purposes, without fee and
 * without a signed licensing agreement, is hereby granted, provided that the
 * above copyright notice, this paragraph and the following two paragraphs
 * appear in all copies, modifications, and distributions. Contact the Head of
 * Informatics and Media Technique, HTBLA Leonding, Limesstr. 12 - 14,
 * 4060 Leonding, Austria, for commercial licensing opportunities.
 * 
 * IN NO EVENT SHALL HTBLA LEONDING BE LIABLE TO ANY PARTY FOR DIRECT,
 * INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST
 * PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION,
 * EVEN IF HTBLA LEONDING HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * HTBLA LEONDING SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE. THE SOFTWARE AND ACCOMPANYING DOCUMENTATION, IF ANY,
 * PROVIDED HEREUNDER IS PROVIDED "AS IS". HTBLA LEONDING HAS NO OBLIGATION
 * TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 */
package parser.syntax;

import org.junit.Test;
import static org.junit.Assert.*;
import parser.Parser;
import parser.ParserFactory;
import parser.general.NoBeardParserTestSetup;
import parser.general.ParserTestSetup;
import scanner.Scanner.Symbol;

/**
 *
 * @author peter
 */
public class NoBeardParserTest {

    public NoBeardParserTest() {
    }

    @Test
    public void testParseEmptyProgram() {
        Parser instance = NoBeardParserTestSetup.getEmptyProgramSetup();
        assertTrue(instance.parse());
        assertEquals(Symbol.EOFSY, ParserTestSetup.getScanner().getCurrentToken().getSymbol());
    }

    @Test
    public void testUnitExpected() {
        Parser instance = NoBeardParserTestSetup.getUnitExpectedTestSetup();

        boolean parsingWasSuccessFul = instance.parse();
        assertFalse("False expected", parsingWasSuccessFul);
        assertEquals("unit expected but found identifier", ParserFactory.getErrorHandler().getAllErrors().get(0).getMessage());
    }

    @Test
    public void testUnitIdentifierExpected() {
        Parser instance = NoBeardParserTestSetup.getUnitIdentifierExpected();

        assertFalse("False expected", instance.parse());
        assertEquals("Last error", error.Error.ErrorType.SYMBOL_EXPECTED.getNumber(), ParserFactory.getErrorHandler().getLastError().getNumber());
    }
}
