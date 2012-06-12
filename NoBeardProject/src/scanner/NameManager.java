/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scanner;

import java.util.HashMap;
import java.util.Map.Entry;
import scanner.Scanner.Symbol;

/**
 *
 * @author peter
 */
public class NameManager {

    private SrcReader sr;
    private HashMap<String, Integer> names;
    private static final HashMap<String, Scanner.Symbol> keywords =
            new HashMap<String, Scanner.Symbol>(){{
                put("unit", Symbol.UNITSY);
                put("do", Symbol.DOSY);
                put("put", Symbol.PUTSY);
                put("putln", Symbol.PUTLNSY);
                put("done", Symbol.DONESY);
                put("int", Symbol.INTSY);
                put("char", Symbol.CHARSY);
                put("bool", Symbol.BOOLSY);
            }};
    
    public NameManager(SrcReader sr) {
        this.sr = sr;
        names = new HashMap<String, Integer>();
    }

    /**
     * Reads a name. readName() is called if and only if SrcReader.getCurrentChar()
     * contains a letter. readName() scans the identifier starting with this letter,
     * checks whether it is a keyword and returns the appropriate token. If the
     * name read is a keyword, Token.sy is set to the corresponding Symbol.
     * If the name read is an identifier, Token.sy is set to IDENTSY and
     * Token.value is set to a unique identifier (spix).
     * @param t Token which corresponds to the name read.
     * @see Token
     * @see Symbol
     */
    public void readName(Token t) {
        String s = readString();
        Scanner.Symbol sy = getTokenType(s);
        t.setSy(sy);
        if (sy == Scanner.Symbol.IDENTSY) {

            int spix = addName(s);

            t.setSy(Scanner.Symbol.IDENTSY);
            t.setValue(spix);
        }
    }
    
    /**
     * Returns the string which corresponds to the given spix.
     * @param spix The unique identifier.
     * @return Corresponding string if spix can be found otherwise null.
     */
    public String getStringName(int spix) {
        String name = null;
        if (names.containsValue(spix)) {
            for (Entry<String, Integer> entry: names.entrySet()) {
                if (entry.getValue() == spix) {
                    name = entry.getKey();
                }
            }
        }
        return name;
    }

    private String readString() {
        StringBuilder s = new StringBuilder();

        while (Character.isLetter(sr.getCurrentChar()) || Character.isDigit(sr.getCurrentChar())) {
            s.append((char)(sr.getCurrentChar()));
            sr.nextChar();
        }
        return s.toString();
    }

    // if s is found in keywords it returns the keyword symbol otherwise
    // it returns IDENTSY
    private Scanner.Symbol getTokenType(String s) {
        if (keywords.containsKey(s)) {
            return keywords.get(s);
        }
        else {
            return Scanner.Symbol.IDENTSY;
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
}
