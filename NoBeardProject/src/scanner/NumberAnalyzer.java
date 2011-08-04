/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scanner;

/**
 *
 * @author peter
 */
public class NumberAnalyzer {
    
    public static int readNumber(SrcReader sr) {
        
        int val = 0;
        
        while (sr.getCurrentChar()!= -1 && Character.isDigit(sr.getCurrentChar())) {
            val *= 10;
            val += sr.getCurrentChar() - '0';
            sr.nextChar();
        }
        return val;
    }
    
}
