/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package machine;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author peter
 */
public class FakeOutputDevice implements OutputDevice {
    List<String> output = new LinkedList<>();
    
    @Override
    public void printInt(int value, int columnwidth) {
        output.add(Integer.toString(value));
    }

    @Override
    public void printChar(char character, int columnwidth) {
        output.add(Character.toString(character));
    }

    @Override
    public void print(String str, int columnwidth) {
        output.add(str);
    }

    @Override
    public void println() {
        output.add("\n");
    }
    
}
