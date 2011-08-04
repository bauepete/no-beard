/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error.synerr;

import error.Error;

/**
 *
 * @author peter
 */
public class SynErr extends Error {
 
    public SynErr(int errNo, String msg, int lineNumber) {
        super("SynErr", errNo, msg, lineNumber);
    }
}
