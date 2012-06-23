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

    public class InvalidString extends ScanErr {

        public InvalidString() {
            super(0, "Unclosed string literal");
        }
    }
}
