/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error;

/**
 *
 * @author peter
 */
public class ScanErr extends error.Error {
 
    ScanErr(int errNo, String errMsg) {
        super("ScanErr", errNo, errMsg);
    }

    public ScanErr() {
        super("ScanErr", 99, "General Scanner Error");
    }

    public class InvalidString extends ScanErr {

        public InvalidString() {
            super(0, "Unclosed string literal");
        }
    }

    public class IntegerOverflow extends ScanErr {

        public IntegerOverflow() {
            super(1, "Integer constant larger than 65535");
        }
    }
}
