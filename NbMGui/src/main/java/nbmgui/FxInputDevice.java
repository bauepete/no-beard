package nbmgui;

import machine.InputDevice;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;

/**
 * Created by Egon on 22.07.2017.
 */
public class FxInputDevice implements InputDevice {

    private final Controller controller;

    public FxInputDevice(Controller controller) {
        this.controller = controller;
    }

    @Override
    public boolean hasNext() {
        waitForInput();
        return !controller.getInput().isEmpty();
    }

    @Override
    public char nextChar() throws NoSuchElementException {
        return controller.getInput().charAt(0);
    }

    @Override
    public boolean hasNextInt() {
        waitForInput();
        return controller.getInput().chars().allMatch( Character::isDigit );
    }

    @Override
    public int nextInt() throws InputMismatchException, NoSuchElementException {
        return Integer.parseInt(controller.getInput());
    }

    private void waitForInput() {
        try {
            controller.getInputView().setDisable(false);
            controller.setDebuggerButtonsDisable(true);
            controller.getSemaphore().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
