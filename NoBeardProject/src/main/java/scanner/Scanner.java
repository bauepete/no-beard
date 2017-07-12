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

package scanner;

import io.SourceReader;
import error.ErrorHandler;

/**
 *
 * @author peter
 */
public class Scanner {

    public enum Symbol {
        /// Special

        NOSY, EOFSY, ILLEGALSY,
        /// Keywords
        UNIT, DO, DONE, PUT, PUTLN, IF, ELSE, INT, BOOL, CHAR,
        TRUE, FALSE,
        /// Classes
        IDENTIFIER, NUMBER, STRING,
        /// Arithmethic
        PLUS, MINUS, TIMES, DIV, MOD,
        /// Relational operators
        LTH, GTH, EQUALS, LEQ, GEQ, NEQ,
        /// Boolean operators
        NOT, AND, OR,
        /// Delimiters
        ASSIGN, SEMICOLON, COMMA, LPAR, RPAR,
        LBRACKET, RBRACKET,
        
        /// Assembler symbols
        OPCODE, LABEL;

        @Override
        public String toString() {
            String str = super.toString();
            return str.toLowerCase();
        }
    }
    private final SourceReader srcReader;
    private final NameManager nameManager;
    private final StringManager stringManager;
    private Token currentToken;
    private final ErrorHandler errorHandler;

    public Scanner(SourceReader sr, ErrorHandler errorHandler, final NameManager nameManager) {
        srcReader = sr;
        sr.nextChar();

        this.nameManager = nameManager;
        stringManager = new StringManager(sr, errorHandler);

        currentToken = new Token();
        this.errorHandler = errorHandler;
    }

    public NameManager getNameManager() {
        return nameManager;
    }

    public StringManager getStringManager() {
        return stringManager;
    }

    public int getCurrentLine() {
        return srcReader.getCurrentLine();
    }

    /**
     * Reads the next token from the source code. After a call of nextToken the
 following holds:
 - getCurrentToken().getSymbol() returns the kind of the
 current symbol
 - if getCurrentToken().getSymbol() == IDENTIFIER,
 getCurrentToken.getValue() is a unique number identifying the symbol
 - if getCurrentToken().getSymbol() == NUMBER, getCurrentToken.getValue() holds the
 value of the number
 - if getCurrentToken().getSymbol() is different from IDENTIFIER and NUMBER
 getCurrentToken.getValue() is undefined
     *
     *** @note Before the first call of nextToken SrcReader.getCurrentChar()
     * must return the first character of the source file.
     */
    public void nextToken() {
        currentToken = new Token();
        currentToken.setSymbol(Symbol.NOSY);

        do {

            if (Character.isDigit(srcReader.getCurrentChar())) {
                handleDigit();
                return;
            }

            if (nameManager.isAPossibleStartOfName((char)srcReader.getCurrentChar())) {
                handleName();
                return;
            }
            handleTerminals();

        } while (currentToken.getSymbol() == Symbol.NOSY);
    }

    private void handleDigit() {
        int n = NumberAnalyzer.readNumber(srcReader, errorHandler);
        currentToken.setSymbol(Symbol.NUMBER);
        currentToken.setValue(n);
    }

    private void handleName() {
        nameManager.readName(currentToken);
    }

    private void handleTerminals() {
        switch (srcReader.getCurrentChar()) {
            case ' ':
            case '\t':
            case '\n':
                srcReader.nextChar();
                break;

            case -1:
                currentToken.setSymbol(Symbol.EOFSY);
                break;

            case '#':
                skipComment();
                break;

            case '+':
                currentToken.setSymbol(Symbol.PLUS);
                srcReader.nextChar();
                break;

            case '-':
                currentToken.setSymbol(Symbol.MINUS);
                srcReader.nextChar();
                break;

            case '*':
                currentToken.setSymbol(Symbol.TIMES);
                srcReader.nextChar();
                break;

            case '/':
                currentToken.setSymbol(Symbol.DIV);
                srcReader.nextChar();
                break;

            case '%':
                currentToken.setSymbol(Symbol.MOD);
                srcReader.nextChar();
                break;

            case '<':
                srcReader.nextChar();
                if (srcReader.getCurrentChar() == '=') {
                    currentToken.setSymbol(Symbol.LEQ);
                    srcReader.nextChar();
                } else {
                    currentToken.setSymbol(Symbol.LTH);
                }
                break;

            case '>':
                srcReader.nextChar();
                if (srcReader.getCurrentChar() == '=') {
                    currentToken.setSymbol(Symbol.GEQ);
                    srcReader.nextChar();
                } else {
                    currentToken.setSymbol(Symbol.GTH);
                }
                break;

            case '!':
                srcReader.nextChar();
                if (srcReader.getCurrentChar() == '=') {
                    currentToken.setSymbol(Symbol.NEQ);
                    srcReader.nextChar();
                } else {
                    currentToken.setSymbol(Symbol.NOT);
                }
                break;

            case '=':
                srcReader.nextChar();
                if (srcReader.getCurrentChar() == '=') {
                    currentToken.setSymbol(Symbol.EQUALS);
                    srcReader.nextChar();
                } else {
                    currentToken.setSymbol(Symbol.ASSIGN);
                }
                break;

            case '&':
                srcReader.nextChar();
                if (srcReader.getCurrentChar() == '&') {
                    currentToken.setSymbol(Symbol.AND);
                    srcReader.nextChar();
                }
                break;

            case '|':
                srcReader.nextChar();
                if (srcReader.getCurrentChar() == '|') {
                    currentToken.setSymbol(Symbol.OR);
                    srcReader.nextChar();
                }
                break;

            case ';':
                currentToken.setSymbol(Symbol.SEMICOLON);
                srcReader.nextChar();
                break;

            case ',':
                currentToken.setSymbol(Symbol.COMMA);
                srcReader.nextChar();
                break;

            case '(':
                currentToken.setSymbol(Symbol.LPAR);
                srcReader.nextChar();
                break;

            case ')':
                currentToken.setSymbol(Symbol.RPAR);
                srcReader.nextChar();
                break;

            case '[':
                currentToken.setSymbol(Symbol.LBRACKET);
                srcReader.nextChar();
                break;

            case ']':
                currentToken.setSymbol(Symbol.RBRACKET);
                srcReader.nextChar();
                break;

            case '"':
            case '\'':
                stringManager.readString();
                StringToken st = new StringToken();
                st.setSymbol(Symbol.STRING);
                st.setAddress(stringManager.getStringAddress());
                st.setLength(stringManager.getStringLength());
                currentToken = st;
                break;

            default:
                currentToken.setSymbol(Symbol.ILLEGALSY);
                srcReader.nextChar();
        }
    }

    /**
     * Returns the token scanned by the last call of nextToken(). All the
     * assumptions ** in next_symbol also hold for current_token.
     *
     *** @return The current token.
     *** @see nextToken()
     */
    public Token getCurrentToken() {
        return currentToken;
    }

    // ---------------------- Private methods ------------------------------
    private void skipComment() {
        srcReader.nextChar();

        while (srcReader.getCurrentChar() != -1 && srcReader.getCurrentChar() != '\n') {
            srcReader.nextChar();
        }
    }
}
