/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scanner;

import scanner.Scanner.Symbol;


/**
 *
 * @author peter
 */
public class Token {
    
    private Scanner.Symbol sy;
    private int value;

    public Token() {
        sy = Symbol.NOSY;
        value = 0;
    }
    
    public Symbol getSy() {
        return sy;
    }

    public int getValue() {
        return value;
    }

    public void setSy(Symbol sy) {
        this.sy = sy;
    }

    public void setValue(int value) {
        this.value = value;
    }
    
    
}
