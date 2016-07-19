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
import parser.ParserFactory;
import parser.VariableDeclarationParser;
import parser.general.ParserTestSetup;
import scanner.Scanner;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class VariableDeclarationParserTest extends ParserTestSetup {
    
    public VariableDeclarationParserTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test simple integer declaration.
     */
    @Test
    public void testSimpleInt() {
        VariableDeclarationParser instance = setupTestObjectsAndParser("int x;");
        boolean expResult = true;
        boolean result = instance.parse();
        assertEquals(expResult, result);
        assertEquals(Scanner.Symbol.EOFSY, getScanner().getCurrentToken().getSy());
    }

    private VariableDeclarationParser setupTestObjectsAndParser(final String srcLine) {
        setupInfraStructure(srcLine);
        VariableDeclarationParser instance = ParserFactory.create(VariableDeclarationParser.class);
        return instance;
    }

    /**
     * Test simple char declaration.
     */
    @Test
    public void testSimpleChar() {
        VariableDeclarationParser instance = setupTestObjectsAndParser("char x;");
        assertTrue(instance.parse());
        assertEquals(Scanner.Symbol.EOFSY, getScanner().getCurrentToken().getSy());
    }
    /**
     * Test invalid variable declaration.
     */
    @Test
    public void testInvalidDeclaration() {
        VariableDeclarationParser instance = setupTestObjectsAndParser("x");
        assertFalse(instance.parse());
    }
    
    @Test
    public void testSimpleBool() {
        VariableDeclarationParser instance = setupTestObjectsAndParser("bool x;");
        assertTrue(instance.parse());
        assertEquals(Scanner.Symbol.EOFSY, getScanner().getCurrentToken().getSy());
    }
    
    @Test
    public void testIntArray() {
        VariableDeclarationParser instance = setupTestObjectsAndParser("int[20] x;");
        assertTrue(instance.parse());
    }
    
    @Test
    public void testInvalidArray() {
        VariableDeclarationParser instance = setupTestObjectsAndParser("bool[k] x;");
        assertFalse(instance.parse());
    }
}
