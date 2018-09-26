package nbmgui;

import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Created by Egon on 17.10.2017.
 */
class DataMemoryConverter {

    static void convertToInt(Controller controller, int selectedLineIndex, int dataCellIndex) {
        String[] lineContent = DataMemoryView.splitDataLine(controller.getDataMemoryListView().getItems().get(selectedLineIndex));
        StringBuilder line = new StringBuilder();
        int address = Integer.parseInt(lineContent[0]);
        if (dataCellIndex == 1) {
            line = new StringBuilder(lineContent[0] + "#int#" + controller.getMachine().getDataMemory().loadWord(address)); // #int# is needed to separate raw data and integer
        } else {
            for (int i = 0; i < lineContent.length; i++) {
                if (i == dataCellIndex) {
                    address = Integer.parseInt(lineContent[0]) + i - 1;
                    line.append("#int#").append(controller.getMachine().getDataMemory().loadWord(address));
                    if (selectedLineIndex < controller.getDataMemoryListView().getItems().size()-1)
                        fillNextLineWithSpaces(controller, dataCellIndex, selectedLineIndex+1);
                    break;
                }
                line.append(lineContent[i]);
            }
        }
        controller.getDataMemoryListView().getItems().set(selectedLineIndex, line.toString());
    }

    private static void fillNextLineWithSpaces(Controller controller, int dataCellIndex, int selectedLineIndex) {
        String[] lineContent = DataMemoryView.splitDataLine(controller.getDataMemoryListView().getItems().get(selectedLineIndex));
        StringBuilder line = new StringBuilder(lineContent[0]);
        for (int i = 1; i < lineContent.length; i++) {
            if (i < dataCellIndex)
                line.append("   ");
            else
                line.append(lineContent[i]);
        }
        controller.getDataMemoryListView().getItems().set(selectedLineIndex, line.toString());
    }


    static void convertLineToChar(Controller controller, ObservableList<Integer> selectedIndices) {
        for (Integer selectedIndex : selectedIndices) {
            if (controller.getDataMemoryListView().getItems().get(selectedIndex).length() <= 8)
                continue;
            String[] lineContent = DataMemoryView.splitDataLine(controller.getDataMemoryListView().getItems().get(selectedIndex));
            StringBuilder line = new StringBuilder(lineContent[0]);
            for (int j = 1; j < lineContent.length; j++) {
                int ascii = Integer.parseInt(lineContent[j]);
                if ((ascii != 0))
                    line.append((char) ascii);
                else
                    line.append(lineContent[j]);
            }
            controller.getDataMemoryListView().getItems().set(selectedIndex, line.toString());
        }
    }

    static void convertLineToRawData(Controller controller, ObservableList<Integer> selectedIndices) {
        for (Integer selectedIndex : selectedIndices) {
            String result = controller.getRawDataMemoryList().get(selectedIndex);
            controller.getDataMemoryListView().getItems().set(selectedIndex, result);
        }
    }

    static void convertDataToChar(Label rawData) {
        int ascii = Integer.parseInt(rawData.getText());
        if ((ascii != 0))
            rawData.setText(String.valueOf((char) ascii));
    }
}