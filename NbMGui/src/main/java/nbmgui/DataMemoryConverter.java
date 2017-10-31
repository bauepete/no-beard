package nbmgui;

import javafx.collections.ObservableList;
import javafx.scene.control.Label;

/**
 * Created by Egon on 17.10.2017.
 */
class DataMemoryConverter {

    static void convertToInt(Controller controller, ObservableList<Integer> selectedIndices) {
        for (Integer selectedIndex : selectedIndices) {
            String[] lineContent = DataMemoryView.splitDataLine(controller.getDataMemoryListView().getItems().get(selectedIndex));
            String line = lineContent[0] + controller.getMachine().getDataMemory().loadWord(Integer.parseInt(lineContent[0]));
            controller.getDataMemoryListView().getItems().set(selectedIndex, line);
        }
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
