/*
 * Copyright ©2012 - 2016. Created by P. Bauer (p.bauer@htl-leonding.ac.at),
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
import java.util.HashMap;
import java.util.Map.Entry;
import scanner.Scanner.Symbol;

/**
 *
 * @author Peter Bauer The NameManagerForCompiler is responsible to store and retrieve
 names. It furthermore calculates the spix. An arithmetic representation of a
 name.
 */
public class NameManagerForCompiler extends NameManager {

    private final HashMap<String, Integer> names;

    public NameManagerForCompiler(SourceReader sr) {
        super(sr);
        names = new HashMap<>();
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
    @Override
    public void readName(Token t) {
        String s = readString();
        Symbol readSymbol = getTokenType(s);
        t.setSymbol(readSymbol);
        t.setClearName(s);
        if (readSymbol == Symbol.IDENTIFIER) {
            int spix = addName(s);
            t.setValue(spix);
        }
    }


    @Override
    protected boolean isValidNameCharacter() {
        char currentChar = (char)sr.getCurrentChar();
        return Character.isLetter(currentChar) || Character.isDigit(currentChar)
                || currentChar == '_' || currentChar == '$';
    }

    // if s is found in keywords it returns the keyword symbol otherwise
    // it returns IDENTIFIER
    private Scanner.Symbol getTokenType(String s) {
        Scanner.Symbol sy = KeywordTable.getSymbol(s);
        if (sy == Symbol.NOSY) {
            return Symbol.IDENTIFIER;
        } else {
            return sy;
        }
    }

    private int addName(String s) {
        int spix;
        if (names.containsKey(s)) {
            spix = names.get(s);
        } else {
            spix = names.size();
            names.put(s, spix);
        }
        return spix;
    }

    /**
     * Returns the string which corresponds to the given spix.
     *
     * @param spix The unique identifier.
     * @return Corresponding string if spix can be found otherwise null.
     */
    @Override
    public String getStringName(int spix) {
        String name = null;
        if (names.containsValue(spix)) {
            for (Entry<String, Integer> entry : names.entrySet()) {
                if (entry.getValue() == spix) {
                    name = entry.getKey();
                }
            }
        }
        return name;
    }

    @Override
    boolean isAPossibleStartOfName(char c) {
        return Character.isLetter(c) || c == '_' || c == '$';
    }
}
