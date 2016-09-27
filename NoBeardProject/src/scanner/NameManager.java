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
public abstract class NameManager {
    protected final SourceReader sr;

    public NameManager(SourceReader sr) {
        this.sr = sr;
    }

    /**
     * Reads a name. readName() is called if and only if
     * SourceReader.getCurrentChar() contains a letter. readName() scans the
     * identifier starting with this letter, checks whether it is a keyword and
     * returns the appropriate token. If the name read is a keyword, Token.sy is
     * set to the corresponding Symbol. If the name read is an identifier,
     * Token.sy is set to IDENTIFIER and Token.value is set to a unique
     * identifier (spix).
     *
     * After a call of readName SourceReader.getCurrentChar() returns the first
     * character of the source code that is not part of the identifier.
     *
     * @param t Token which corresponds to the name read.
     * @see Token
     * @see Symbol
     */
    public abstract void readName(Token t);

    protected String readString() {
        StringBuilder s = new StringBuilder();
        while (isValidNameCharacter()) {
            s.append((char) (sr.getCurrentChar()));
            sr.nextChar();
        }
        return s.toString();
    }

    protected abstract boolean isValidNameCharacter();

    /**
     * Returns the string which corresponds to the given spix.
     *
     * @param spix The unique identifier.
     * @return Corresponding string if spix can be found otherwise null.
     */
    public abstract String getStringName(int spix);

    abstract boolean isAPossibleStartOfName(char c);
    
}
