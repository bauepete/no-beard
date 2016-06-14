/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scanner;

import error.SourceCodeInfo;

/**
 *
 * @author peter
 */
public interface SrcReader extends SourceCodeInfo {
    
    public void nextChar();
    public int getCurrentChar();    
}
