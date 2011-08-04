/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scanner;

/**
 *
 * @author peter
 */
public interface SrcReader {
    
    public void nextChar();
    public int getCurrentChar();
    public int getCurrentCol();
    public int getCurrentLine();
    
}
