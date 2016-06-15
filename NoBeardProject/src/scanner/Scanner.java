/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scanner;

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
        PUT, PUTLN, UNIT, DO, DONE, IF, ELSE, INT, BOOL, CHAR,
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
        LBRACKET, RBRACKET;

        @Override
        public String toString() {
            String str = super.toString();
            return str.toLowerCase();
        }
    }
    private final SrcReader srcReader;
    private final NameManager nameManager;
    private final StringManager stringManager;
    private Token currentToken;
    private final ErrorHandler errorHandler;

    public Scanner(SrcReader sr, ErrorHandler errorHandler) {
        srcReader = sr;
        sr.nextChar();

        nameManager = new NameManager(sr);
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

    /** Reads the next token from the source code. After a call of nextToken the
 following holds:
 - getCurrentToken().getSy() returns the kind of the current symbol
 - if getCurrentToken().getSy() == IDENTIFIER, getCurrentToken.getValue()
 is a unique number identifying the symbol
 - if getCurrentToken().getSy() == NUMBER, getCurrentToken.getValue()
 holds the value of the number
 - if getCurrentToken().getSy() is different from IDENTIFIER and NUMBER
 getCurrentToken.getValue() is undefined
     *** @note Before the first call of nextToken SrcReader.getCurrentChar()
     * must return the first character of the source file.
     */
    public void nextToken() {
        currentToken.setSy(Symbol.NOSY);

        do {

            if (Character.isDigit(srcReader.getCurrentChar())) {
                handleDigit();
                return;
            }

            if (Character.isLetter(srcReader.getCurrentChar())) {
                handleName();
                return;
            }
            handleTerminals();

        } while (currentToken.getSy() == Symbol.NOSY);
    }

    private void handleDigit() {
        int n = NumberAnalyzer.readNumber(srcReader, errorHandler);
        currentToken.setSy(Symbol.NUMBER);
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
                currentToken.setSy(Symbol.EOFSY);
                break;

            case '#':
                skipComment();
                break;

            case '+':
                currentToken.setSy(Symbol.PLUS);
                srcReader.nextChar();
                break;

            case '-':
                currentToken.setSy(Symbol.MINUS);
                srcReader.nextChar();
                break;

            case '*':
                currentToken.setSy(Symbol.TIMES);
                srcReader.nextChar();
                break;

            case '/':
                currentToken.setSy(Symbol.DIV);
                srcReader.nextChar();
                break;

            case '%':
                currentToken.setSy(Symbol.MOD);
                srcReader.nextChar();
                break;

            case '<':
                srcReader.nextChar();
                if (srcReader.getCurrentChar() == '=') {
                    currentToken.setSy(Symbol.LEQ);
                    srcReader.nextChar();
                } else {
                    currentToken.setSy(Symbol.LTH);
                }
                break;

            case '>':
                srcReader.nextChar();
                if (srcReader.getCurrentChar() == '=') {
                    currentToken.setSy(Symbol.GEQ);
                    srcReader.nextChar();
                } else {
                    currentToken.setSy(Symbol.GTH);
                }
                break;

            case '!':
                srcReader.nextChar();
                if (srcReader.getCurrentChar() == '=') {
                    currentToken.setSy(Symbol.NEQ);
                    srcReader.nextChar();
                } else {
                    currentToken.setSy(Symbol.NOT);
                }
                break;

            case '=':
                srcReader.nextChar();
                if (srcReader.getCurrentChar() == '=') {
                    currentToken.setSy(Symbol.EQUALS);
                    srcReader.nextChar();
                } else {
                    currentToken.setSy(Symbol.ASSIGN);
                }
                break;

            case '&':
                srcReader.nextChar();
                if (srcReader.getCurrentChar() == '&') {
                    currentToken.setSy(Symbol.AND);
                    srcReader.nextChar();
                }
                break;

            case '|':
                srcReader.nextChar();
                if (srcReader.getCurrentChar() == '|') {
                    currentToken.setSy(Symbol.OR);
                    srcReader.nextChar();
                }
                break;

            case ';':
                currentToken.setSy(Symbol.SEMICOLON);
                srcReader.nextChar();
                break;

            case ',':
                currentToken.setSy(Symbol.COMMA);
                srcReader.nextChar();
                break;

            case '(':
                currentToken.setSy(Symbol.LPAR);
                srcReader.nextChar();
                break;

            case ')':
                currentToken.setSy(Symbol.RPAR);
                srcReader.nextChar();
                break;

            case '[':
                currentToken.setSy(Symbol.LBRACKET);
                srcReader.nextChar();
                break;

            case ']':
                currentToken.setSy(Symbol.RBRACKET);
                srcReader.nextChar();
                break;

            case '"':
            case '\'':
                stringManager.readString();
                StringToken st = new StringToken();
                st.setSy(Symbol.STRING);
                st.setAddress(stringManager.getStringAddress());
                st.setLength(stringManager.getStringLength());
                currentToken = st;
                break;

            default:
                currentToken.setSy(Symbol.ILLEGALSY);
                srcReader.nextChar();
        }
    }

    /** Returns the token scanned by the last call of nextToken(). All the assumptions
     *** in next_symbol also hold for current_token.
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
