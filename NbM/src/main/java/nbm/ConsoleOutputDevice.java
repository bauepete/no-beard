/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nbm;

import machine.OutputDevice;

/**
 *
 * @author peter
 */
public class ConsoleOutputDevice implements OutputDevice {

    @Override
    public void printInt(int value, int columnwidth) {
        String formatString = "%" + columnwidth + "d";
        System.out.printf(formatString, value);
    }

    @Override
    public void printChar(char character, int columnwidth) {
        System.out.print((char) character);
        outputBlanks(columnwidth - 1);
    }

    private void outputBlanks(int number) {
        for (int i = 0; i < number; i++) {
            System.out.print(" ");
        }
    }

    @Override
    public void print(String str, int columnwidth) {
        System.out.print(str);
        outputBlanks(columnwidth - str.length());
    }

    @Override
    public void println() {
        System.out.println();
    }
}
