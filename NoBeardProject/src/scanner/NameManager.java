/*
 * Copyright (C) 2012  Peter Bauer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package scanner;

import java.util.HashMap;
import java.util.Map.Entry;
import scanner.Scanner.Symbol;

/**
 *
 * @author Peter Bauer The NameManager is responsible to store and retrieve
 * names. It furthermore calculates the spix. An arithmetic representation of a
 * name.
 */
public class NameManager {

    private final SrcReader sr;
    private final HashMap<String, Integer> names;

    public NameManager(SrcReader sr) {
        this.sr = sr;
        names = new HashMap<>();
    }

    /**
     * Reads a name. readName() is called if and only if
     * SrcReader.getCurrentChar() contains a letter. readName() scans the
     * identifier starting with this letter, checks whether it is a keyword and
     * returns the appropriate token. If the name read is a keyword, Token.sy is
     * set to the corresponding Symbol. If the name read is an identifier,
     * Token.sy is set to IDENTIFIER and Token.value is set to a unique
     * identifier (spix).
     *
     * After a call of readName SrcReader.getCurrentChar() returns the first
     * character of the source code that is not part of the identifier.
     *
     * @param t Token which corresponds to the name read.
     * @see Token
     * @see Symbol
     */
    public void readName(Token t) {
        String s = readString();
        Symbol readSymbol = getTokenType(s);
        t.setSymbol(readSymbol);
        if (readSymbol == Symbol.IDENTIFIER) {
            int spix = addName(s);
            t.setValue(spix);
            t.setClearName(s);
        }
    }

    private String readString() {
        StringBuilder s = new StringBuilder();

        while (Character.isLetter(sr.getCurrentChar()) || Character.isDigit(sr.getCurrentChar())) {
            s.append((char) (sr.getCurrentChar()));
            sr.nextChar();
        }
        return s.toString();
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
}
