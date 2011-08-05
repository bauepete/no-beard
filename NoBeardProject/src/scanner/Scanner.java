/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scanner;

/**
 *
 * @author peter
 */
public class Scanner {

    public enum Symbol {
        /// Special

        NOSY, EOFSY, ILLEGALSY,
        /// Keywords
        PUTSY, UNITSY, DOSY, DONESY, INTSY, BOOLSY, CHARSY,
        /// Classes
        IDENTSY, NUMBERSY, STRINGSY,
        /// Arithmethic
        PLUSSY, MINUSSY, TIMESSY, DIVSY, MODSY,
        /// Delimiters
        ASSIGNSY, SEMICOLONSY, COLONSY, LPARSY, RPARSY,
        LBRACKETSY, RBRACKETSY;

        @Override
        public String toString() {
            String str = super.toString();
            return str.substring(0, str.length() - 2).toLowerCase();
        }
    }
    private SrcReader srcReader;
    private NameManager nameManager;
    private StringManager stringManager;
    private Token currentToken;

    public Scanner(SrcReader sr) {
        srcReader = sr;
        sr.nextChar();

        nameManager = new NameManager(sr);
        stringManager = new StringManager(sr);

        currentToken = new Token();
    }

    public NameManager getNameManager() {
        return nameManager;
    }
    
    public int getCurrentLine() {
        return srcReader.getCurrentLine();
    }

    /** Reads the next token from the source code. After a call of nextToken the
     * following holds:
     * - getCurrentToken().getSy() returns the kind of the current symbol
     * - if getCurrentToken().getSy() == IDENTSY, getCurrentToken.getValue()
     * is a unique number identifying the symbol
     * - if getCurrentToken().getSy() == NUMBERSY, getCurrentToken.getValue()
     * holds the value of the number
     * - if getCurrentToken().getSy() is different from IDENTSY and NUMBERSY
     * getCurrentToken.getValue() is undefined
     *** @note Before the first call of nextToken SrcReader.getCurrentChar()
     * must return the first character of the source file.
     */
    public void nextToken() {
        currentToken.setSy(Symbol.NOSY);

        do {

            if (Character.isDigit(srcReader.getCurrentChar())) {
                int n = NumberAnalyzer.readNumber(srcReader);
                currentToken.setSy(Symbol.NUMBERSY);
                currentToken.setValue(n);
                return;
            }

            if (Character.isLetter(srcReader.getCurrentChar())) {
                nameManager.readName(currentToken);
                return;
            }

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
                    currentToken.setSy(Symbol.PLUSSY);
                    srcReader.nextChar();
                    break;

                case '-':
                    currentToken.setSy(Symbol.MINUSSY);
                    srcReader.nextChar();
                    break;

                case '*':
                    currentToken.setSy(Symbol.TIMESSY);
                    srcReader.nextChar();
                    break;

                case '/':
                    currentToken.setSy(Symbol.DIVSY);
                    srcReader.nextChar();
                    break;

                case '%':
                    currentToken.setSy(Symbol.MODSY);
                    srcReader.nextChar();
                    break;

                case '=':
                    currentToken.setSy(Symbol.ASSIGNSY);
                    srcReader.nextChar();
                    break;

                case ';':
                    currentToken.setSy(Symbol.SEMICOLONSY);
                    srcReader.nextChar();
                    break;

                case ',':
                    currentToken.setSy(Symbol.COLONSY);
                    srcReader.nextChar();
                    break;

                case '(':
                    currentToken.setSy(Symbol.LPARSY);
                    srcReader.nextChar();
                    break;

                case ')':
                    currentToken.setSy(Symbol.RPARSY);
                    srcReader.nextChar();
                    break;

                case '[':
                    currentToken.setSy(Symbol.LBRACKETSY);
                    srcReader.nextChar();
                    break;

                case ']':
                    currentToken.setSy(Symbol.RBRACKETSY);
                    srcReader.nextChar();
                    break;

                case '"':
                case '\'':
                    stringManager.readString();
                    currentToken.setSy(Symbol.STRINGSY);
                    break;

                default:
                    currentToken.setSy(Symbol.ILLEGALSY);
                    srcReader.nextChar();
            }

        } while (currentToken.getSy() == Symbol.NOSY);
    }

    /** Returns the token scanned by the last call of nextToken(). All the assumptions
     *** in next_symbol also hold for current_token.
     *** @return The current token.
     *** @see nextToken()
     */
    public Token getCurrentToken() {
        return currentToken;
    }
    
    /**
     * 
     * @return The start address of the last recently detected string in the
     * string list.
     */
    public int getStringAddress() {
        return stringManager.getStringAddress();
    }
    
    /**
     * 
     * @return The length of the last recently detected string.
     */
    public int getStringLength() {
        return stringManager.getStringLength();
    }

    // ---------------------- Private methods ------------------------------
    private void skipComment() {
        srcReader.nextChar();

        while (srcReader.getCurrentChar() != -1 && srcReader.getCurrentChar() != '\n') {
            srcReader.nextChar();
        }
    }
}
