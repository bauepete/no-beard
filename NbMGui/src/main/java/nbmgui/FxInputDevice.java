package nbmgui;

import javafx.application.Platform;
import machine.InputDevice;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by Egon on 22.07.2017.
 */
public class FxInputDevice implements InputDevice {

    private final Controller controller;

    FxInputDevice(Controller controller) {
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
        Scanner s = new Scanner(controller.getInput());
        return s.hasNextInt();
    }

    @Override
    public int nextInt() throws NoSuchElementException {
        return Integer.parseInt(controller.getInput());
    }

    private void waitForInput() {
        try {
            Platform.runLater(() -> controller.enableInputView(true));
            controller.getSemaphore().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
