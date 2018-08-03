/*
 * Copyright ©2016. Created by P. Bauer (p.bauer@htl-leonding.ac.at),
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
package scanner;

import io.SourceReader;
import io.SourceStringReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import scanner.Scanner.Symbol;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class NameManagerForAssemblerTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testValidNameCharacter() {
        SourceReader sourceReader = new SourceStringReader("l_");
        sourceReader.nextChar();
        NameManagerForAssembler instance = new NameManagerForAssembler(sourceReader);
        assertEquals(true, instance.isValidNameCharacter());

        sourceReader.nextChar();
        assertEquals(true, instance.isValidNameCharacter());
    }

    @Test
    public void testInvalidNameCharacter() {
        SourceReader sourceReader = new SourceStringReader("§");
        sourceReader.nextChar();
        NameManagerForAssembler instance = new NameManagerForAssembler(sourceReader);
        boolean result = instance.isValidNameCharacter();
        assertEquals(false, result);
    }

    /**
     * Test of getStringName method, of class NameManagerForAssembler.
     */
    @Test
    public void testGetStringName() {
        int spix = 0;
        NameManagerForAssembler instance = new NameManagerForAssembler(new SourceStringReader(""));
        String expResult = "";
        String result = instance.getStringName(spix);
        assertEquals(expResult, result);
    }

    /**
     * Test of isAPossibleStartOfName method, of class NameManagerForAssembler.
     */
    @Test
    public void testIsAPossibleStartOfName() {
        NameManagerForAssembler instance = new NameManagerForAssembler(new SourceStringReader(""));
        assertEquals(true, instance.isAPossibleStartOfName('a'));
        assertEquals(true, instance.isAPossibleStartOfName('z'));
        assertEquals(false, instance.isAPossibleStartOfName('_'));
        assertEquals(true, instance.isAPossibleStartOfName('.'));
    }

    /**
     * Test reading an assembler instruction.
     */
    @Test
    public void testReadInstruction() {
        Token t = new Token();
        SourceReader sourceReader = new SourceStringReader("lit halt");
        sourceReader.nextChar();
        NameManagerForAssembler instance = new NameManagerForAssembler(sourceReader);
        instance.readName(t);
        assertEquals(Symbol.OPCODE, t.getSymbol());
        assertEquals("lit", t.getClearName());

        sourceReader.nextChar();
        instance.readName(t);
        assertEquals(Symbol.OPCODE, t.getSymbol());
        assertEquals("halt", t.getClearName());
    }

    @Test
    public void testReadLabel() {
        Token t = new Token();
        SourceReader sourceReader = new SourceStringReader(".endif .file_not_found");
        NameManagerForAssembler instance = new NameManagerForAssembler(sourceReader);
        
        sourceReader.nextChar();
        instance.readName(t);
        assertEquals(Symbol.LABEL, t.getSymbol());
        assertEquals(".endif", t.getClearName());
        
        sourceReader.nextChar();
        instance.readName(t);
        assertEquals(Symbol.LABEL, t.getSymbol());
        assertEquals(".file_not_found", t.getClearName());
    }
}
