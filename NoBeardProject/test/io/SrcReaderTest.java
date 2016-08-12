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
package io;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class SrcReaderTest {
    SourceReader sr;
    SourceReader fr;
    
    String testString = "This is no source";

    public SrcReaderTest() {
    }

    @Before
    public void setUp() {
        sr = new SourceStringReader(testString);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of nextChar method, of class SourceReader.
     */
    @Test
    public void testFirstChars() throws Exception {
        System.out.println("testNextChars");
   
        sr.nextChar();
        assertTrue("First char not ok", sr.getCurrentChar() == 'T');
        assertEquals(1, sr.getCurrentCol());
        assertEquals(1, sr.getCurrentLine());

        sr.nextChar();
        assertTrue("Second char not ok", sr.getCurrentChar() == 'h');
        assertEquals(2, sr.getCurrentCol());
        assertEquals(1, sr.getCurrentLine());
    }
    
    @Test
    public void testLastCharAndBeyond() throws Exception {
        System.out.println("testLastCharAndBeyond");
        
        for (int i = 0; i < testString.length(); i++) {
            sr.nextChar();
        }
        
        assertTrue("Last char not ok", sr.getCurrentChar() == 'e');
        assertEquals(testString.length(), sr.getCurrentCol());
        assertEquals(1, sr.getCurrentLine());
        
        sr.nextChar();
        assertTrue("Reading beyond last char not ok", sr.getCurrentChar() == -1);
        sr.nextChar();
        assertTrue("Reading beyond last char not ok", sr.getCurrentChar() == -1);
    }
}