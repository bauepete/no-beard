/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package error.semerr;

import error.Error;

/**
 *
 * @author peter
 */
public class SemErr implements Error {
    
    private String msg;
    protected int errNo;
    
    public SemErr(String msg, int errNo) {
        this.msg = msg;
        this.errNo = errNo;
    }
    
    @Override
    public String getErrorClass() {
        return "SemErr";
    }
    
    @Override
    public  String getLongName() {
        return "semantic error";
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
