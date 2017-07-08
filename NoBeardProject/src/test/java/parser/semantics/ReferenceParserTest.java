/*
 * Copyright ©2015. Created by P. Bauer (p.bauer@htl-leonding.ac.at), Department
 * of Informatics and Media Technique, HTBLA Leonding, Limesstr. 12 - 14,
 * 4060 Leonding, AUSTRIA. All Rights Reserved. Permission to use, copy, modify,
 * and distribute this software and its documentation for educational,
 * research, and not-for-profit purposes, without fee and without a signed
 * licensing agreement, is hereby granted, provided that the above copyright
 * notice, this paragraph and the following two paragraphs appear in all
 * copies, modifications, and distributions. Contact the Head of Informatics,
 * Media Technique and Design, HTBLA Leonding, Limesstr. 12 - 14, 4060 Leonding,
 * Austria, for commercial licensing opportunities.
 * 
 * IN NO EVENT SHALL HTBLA LEONDING BE  LIABLE TO ANY PARTY FOR DIRECT,
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
package parser.semantics;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import parser.ParserFactory;
import parser.ReferenceParser;
import parser.general.ReferenceParserTestSetup;
import scanner.Scanner.Symbol;
import symboltable.Operand;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class ReferenceParserTest {

    public ReferenceParserTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test parsing a simple identifier.
     */
    @Test
    public void testParseSimpleIdentifier() {
        ReferenceParser p = ReferenceParserTestSetup.getSimpleIdentifierTestSetup();
        assertTrue(p.parse());
        assertEquals(Operand.Kind.VARIABLE, p.getOperand().getKind());
    }
    
    @Test
    public void testScannerPosition() {
        ReferenceParser p = ReferenceParserTestSetup.getSimpleIdentifierTestSetup();
        p.parse();
        assertEquals(Symbol.EOFSY, ReferenceParserTestSetup.getScanner().getCurrentToken().getSymbol());
    }
    
    @Test
    public void testReferenceUndefinedName() {
        ReferenceParser p = ReferenceParserTestSetup.getUndefinedNameTestSetup();
        assertFalse(p.parse());
    }

    @Test
    public void testParsingNonVariableFails() {
        ReferenceParser p = ReferenceParserTestSetup.getNoVariableTestSetup();
        assertFalse(p.parse());

        error.Error e = ParserFactory.getErrorHandler().getAllErrors().get(0);
        assertEquals(error.Error.ErrorType.OPERAND_KIND_EXPECTED.getNumber(), e.getNumber());
    }
}
