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

    private Scanner.Symbol symbol;
    private int value;
    private String clearName;

    public Token() {
        symbol = Symbol.NOSY;
        value = 0;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public int getValue() {
        return value;
    }

    public String getClearName() {
        return clearName;
    }

    public void setSymbol(Symbol sy) {
        this.symbol = sy;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setClearName(String clearName) {
        this.clearName = clearName;
    }
}
