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
public class SynErr implements Error {
    private String msg;
    private int errNo;
    
    public SynErr(String msg, int errNo) {
        this.msg = msg;
        this.errNo = errNo;
    }

    @Override
    public String getErrorClass() {
        return "SynErr";
    }

    @Override
    public String getLongName() {
        return "syntax errors";
    }

    @Override
    public String getMessage() {
        return msg;
    }

    @Override
    public int getErrNo() {
        return errNo;
    }
    
}
