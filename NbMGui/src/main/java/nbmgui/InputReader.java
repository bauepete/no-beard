package nbmgui;

import machine.InputDevice;
import nbmgui.controller.Controller;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;

/**
 * Created by Egon on 22.07.2017.
 */
public class InputReader implements InputDevice {
    private final Controller controller;

    public InputReader(Controller controller) {
        this.controller = controller;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public char nextChar() throws NoSuchElementException {
        return 0;
    }

    @Override
    public boolean hasNextInt() {
        return false;
    }

    @Override
    public int nextInt() throws InputMismatchException, NoSuchElementException {
        return 0;
    }
}
