/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import error.SourceCodeInfo;

/**
 *
 * @author peter
 */
public interface SourceReader extends SourceCodeInfo {
    
    public void nextChar();
    public int getCurrentChar();    
}
