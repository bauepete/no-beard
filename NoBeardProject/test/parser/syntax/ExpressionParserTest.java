/*
 * Copyright ©2015, 2016. Created by P. Bauer (p.bauer@htl-leonding.ac.at),
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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import parser.ExpressionParser;
import parser.general.ExpressionParserTestSetup;

/**
 *
 * @author peter
 */
public class ExpressionParserTest {
    
    public ExpressionParserTest() {
    }

    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test a simple relation.
     */
    @Test
    public void testParseSimpleRel() {
        System.out.println("parseSimpleRel");
        ExpressionParser p = ExpressionParserTestSetup.getSimpleRel();
        assertTrue(p.parse());
    }

    /**
     * Test a relation using an and.
     */
    @Test
    public void testParseAndRel() {
        System.out.println("parseAndRel");
        ExpressionParser p = ExpressionParserTestSetup.getAndRel();
        assertTrue(p.parse());
    }

    /**
     * Test a relation using an or.
     */
    @Test
    public void testParseOrRel() {
        System.out.println("parseOrRel");
        ExpressionParser p = ExpressionParserTestSetup.getOrRel();
        assertTrue(p.parse());
    }

    /**
     * Test of parseOldStyle method, of class ExpressionParser.
     */
    @Test
    public void testParseAndOrRel() {
        System.out.println("parseAndOrRel");
        ExpressionParser p = ExpressionParserTestSetup.getAndOrRel();
        assertTrue(p.parseOldStyle());
    }
}
