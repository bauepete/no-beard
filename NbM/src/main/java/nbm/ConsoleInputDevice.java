/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nbm;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import machine.InputDevice;

/**
 *
 * @author peter
 */
public class ConsoleInputDevice implements InputDevice{
    
    private final Scanner scanner = new Scanner(System.in);
    
    @Override
    public boolean hasNext() {
        return scanner.hasNext();
    }

    @Override
    public char nextChar() throws NoSuchElementException {
        scanner.useDelimiter("");
        String ch = scanner.next();
        return ch.charAt(0);
    }

    @Override
    public boolean hasNextInt() {
        return scanner.hasNextInt();
    }

    @Override
    public int nextInt() throws InputMismatchException, NoSuchElementException {
        return scanner.nextInt();
    }
    
}
