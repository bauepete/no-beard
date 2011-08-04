/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scanner;

/**
 * NoBeard string manager assists the scanner to detect strings.
 * @author peter
 */
public class StringManager {
    
    private final int MAXSTRING = 2048;
    
    private char[] stringStorage;
    private int firstFree;
    
    private int stringAddress;
    private int stringLength;
    
    public StringManager() {
        stringStorage = new char[MAXSTRING];
    }
    
    /**
     * readString is called if and only if the SrcReader.getCurrentChar()
     * returns an apostrophe (') or a quote ("). readString() scans the string
     * beginning with this character, stores it into the string storage.
     * If the string is invalid (e.g., it doesn't end on the same line as it
     * begins) or if a string storage overflow is encountered, a lexical error
     * is reported.
     * After a call of readString() SrcReader.getCurrentChar() returns the
     * first character in the source code that follows the string's closing
     * apostrophe or quote.
     */
    void readString() {
        
    }

    /**
     * 
     * @return The address of the last recently detected string.
     */
    public int getStringAddress() {
        return stringAddress;
    }

    /**
     * 
     * @return The length of the last recently detected string.
     */
    public int getStringLength() {
        return stringLength;
    }
    
}
