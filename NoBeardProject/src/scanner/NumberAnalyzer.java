/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scanner;

import io.SourceReader;
import error.ErrorHandler;

/**
 *
 * @author peter
 */
public class NumberAnalyzer {

    public static final int MAX_INTEGER = 65535;

    /**
     * readNumber is called if and only if SourceReader.getCurrentChar() returns a
 digit. readNumber scans the number beginning with this digit and converts
 it to a cardinal number which is returned.

 After a call of readNumber SourceReader.getCurrentChar() returns the first
 character of the source code that is not part of the number.
     *
     * @param sr Source reader
     * @param eh Error handler
     * @return The number scanned
     */
    public static int readNumber(SourceReader sr, ErrorHandler eh) {

        int val = 0;

        while (sr.getCurrentChar() != -1 && Character.isDigit(sr.getCurrentChar())) {
            val *= 10;
            val += sr.getCurrentChar() - '0';
            sr.nextChar();
        }
        if (val > 65535) {
            eh.throwIntegerOverflow();
        }
        return val;
    }

}
