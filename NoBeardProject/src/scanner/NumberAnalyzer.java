/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scanner;

import error.ErrorHandler;
import error.ScanErr;
import error.ScanErr.IntegerOverflow;

/**
 *
 * @author peter
 */
public class NumberAnalyzer {
    /**
     * readNumber is called if and only if SrcReader.getCurrentChar() returns
     * a digit. readNumber scans the number beginning with this digit and
     * converts it to a cardinal number which is returned.
     * 
     * After a call of readNumber SrcReader.getCurrentChar() returns the first
     * character of the source code that is not part of the number.
     * @param sr
     * @return 
     */
    public static int readNumber(SrcReader sr) {
        
        int val = 0;
        
        while (sr.getCurrentChar()!= -1 && Character.isDigit(sr.getCurrentChar())) {
            val *= 10;
            val += sr.getCurrentChar() - '0';
            sr.nextChar();
        }
        if (val > 65535)
            ErrorHandler.getInstance().raise(new ScanErr().new IntegerOverflow());
        return val;
    }
    
}
