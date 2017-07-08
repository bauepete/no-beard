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
package asm;

import config.Configuration;
import io.SourceReader;
import io.SourceStringReader;
import parser.AssemblerParser;
import parser.Parser;
import parser.ParserFactory;
import scanner.NameManagerForAssembler;

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class NoBeardAssembler {

    private static boolean wasSuccessFull = false;

    public static String getVersion() {
        return Configuration.getVersion();
    }

    public static boolean assemble() {
        Parser p = ParserFactory.create(AssemblerParser.class);
        wasSuccessFull = p.parse();
        return wasSuccessFull;
    }

    public static void setSourceString(String sourceString) {
        SourceReader sourceReader = new SourceStringReader(sourceString);
        ParserFactory.setup(sourceReader, new NameManagerForAssembler(sourceReader));
        wasSuccessFull = false;
    }

    public static boolean wasSuccessfull() {
        return wasSuccessFull;
    }

    public static byte[] getByteCode() {
        return ParserFactory.getCodeGenerator().getByteCode();
    }

    public static byte[] getStringStorage() {
        return ParserFactory.getScanner().getStringManager().getStringStorage();
    }
}
