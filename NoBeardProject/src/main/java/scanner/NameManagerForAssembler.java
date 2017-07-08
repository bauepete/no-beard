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

/**
 *
 * @author P. Bauer (p.bauer@htl-leonding.ac.at)
 */
public class NameManagerForAssembler extends NameManager {

    public NameManagerForAssembler(SourceReader sr) {
        super(sr);
    }

    @Override
    public void readName(Token t) {
        String s = readString();
        if (s.charAt(0) == '.')
            t.setSymbol(Scanner.Symbol.LABEL);
        else
            t.setSymbol(Scanner.Symbol.OPCODE);
        t.setClearName(s);
    }

    @Override
    protected boolean isValidNameCharacter() {
        return isAPossibleStartOfName((char)sr.getCurrentChar()) || sr.getCurrentChar() == '_';
    }

    @Override
    public String getStringName(int spix) {
        return "";
    }

    @Override
    boolean isAPossibleStartOfName(char c) {
        return Character.isLetter(c) || c == '.';
    }
    
}
