package nbmgui;

import javafx.application.Platform;
import machine.OutputDevice;

/**
 * Created by Egon on 22.07.2017.
 */
public class FxOutputDevice implements OutputDevice {
    private final Controller controller;

    FxOutputDevice(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void printInt(int value, int columnwidth) {
        Platform.runLater(() -> controller.getOutputView().appendText(String.format("%" + columnwidth + "d", value)));
    }

    @Override
    public void printChar(char character, int columnwidth) {
        Platform.runLater(() -> {
            controller.getOutputView().appendText(String.valueOf(character));
            outputBlanks(columnwidth-1);
        });
    }

    @Override
    public void print(String str, int columnwidth) {
        Platform.runLater(() -> {
            controller.getOutputView().appendText(str);
            outputBlanks(columnwidth-str.length());
        });
    }

    @Override
    public void println() {
        Platform.runLater(() -> controller.getOutputView().appendText("\n"));
    }

    private void outputBlanks(int number) {
        for (int i = 0; i < number; i++) {
            controller.getOutputView().appendText(" ");
        }
    }
}
