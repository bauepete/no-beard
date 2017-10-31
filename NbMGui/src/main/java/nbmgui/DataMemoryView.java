package nbmgui;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Egon on 17.10.2017.
 */
class DataMemoryView
{
    static void update(Controller controller, ObservableList<String> content) {
        controller.getDataMemoryListView().setItems(content);
        controller.getDataMemoryListView().setCellFactory(list -> new ListCell<String>() {
            int framePointer = controller.getMachine().getCallStack().getFramePointer();
            int stackPointer = controller.getMachine().getCallStack().getStackPointer();
            static final int INDEX_OF_ADDRESS = 0;

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    createDataLine(item);
                }
            }

            private void createDataLine(String item) {
                HBox line = new HBox();
                int firstAddressInLine = getIndex() * 4;
                convertStringToLabels(firstAddressInLine, item).forEach(line.getChildren()::add);
                setGraphic(line);
            }

            private List<Label> convertStringToLabels(int firstAddressInLine, String line) {
                String[] lineContent = splitDataLine(line);
                List<Label> result = createLabelForAddress(lineContent[INDEX_OF_ADDRESS]);
                int currentAddress = firstAddressInLine;
                for (int i = 1; i < lineContent.length; i++) {
                    if (currentAddress == framePointer)
                        result.add(createHighlightedLabel(lineContent[i], "#0038AC"));
                    else if (currentAddress == stackPointer)
                        result.add(createHighlightedLabel(lineContent[i], "#AC080E"));
                    else
                        result.add(createNormalLabel(lineContent[i]));
                    currentAddress++;
                }
                return result;
            }

            List<Label> createLabelForAddress(String address) {
                Label l = new Label(address);
                l.setId("addressCell");
                l.setPadding(new Insets(0.0, 0.0, 0.0, 70.0));
                return new ArrayList<>(Collections.singletonList(l));
            }

            Label createHighlightedLabel(String content, String bgColor) {
                Label l = new Label(content);
                l.setStyle("-fx-background-color: " + bgColor + ";" +
                        "-fx-text-fill: white;");
                l.setId("dataCell");
                setContextMenuToDataCell(l);
                return l;
            }

            Label createNormalLabel(String content) {
                Label l = new Label(content);
                l.setId("dataCell");
                setContextMenuToDataCell(l);
                return l;
            }

            void setContextMenuToDataCell(Label dataCell) {
                dataCell.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.SECONDARY) {
                        MenuItem menuItem = new MenuItem("View char");
                        menuItem.setOnAction(menuEvent -> DataMemoryConverter.convertDataToChar(dataCell));
                        controller.getDataMemoryListView().getContextMenu().getItems().add(menuItem);
                    }
                });
            }
        });
        setContextMenuToListView(controller);
        AnchorPane.setTopAnchor(controller.getDataMemoryListView(), controller.getDataMemoryHeaderHeight());
    }

    private static void setContextMenuToListView(Controller controller) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem viewInt = new MenuItem("View Integer");
        MenuItem viewChar = new MenuItem("View characters");
        MenuItem viewRawData = new MenuItem("View raw data");
        viewChar.setOnAction(event -> DataMemoryConverter.convertLineToChar(controller, controller.getDataMemoryListView().getSelectionModel().getSelectedIndices()));
        viewInt.setOnAction(event -> DataMemoryConverter.convertToInt(controller, controller.getDataMemoryListView().getSelectionModel().getSelectedIndices()));
        viewRawData.setOnAction(event -> DataMemoryConverter.convertLineToRawData(controller, controller.getDataMemoryListView().getSelectionModel().getSelectedIndices()));
        contextMenu.getItems().addAll(viewInt, viewChar, viewRawData);
        contextMenu.setOnHidden(event -> {
            if (controller.getDataMemoryListView().getContextMenu().getItems().size() > 3)
                controller.getDataMemoryListView().getContextMenu().getItems().remove(3);
        });
        controller.getDataMemoryListView().setContextMenu(contextMenu);
        controller.getDataMemoryListView().setOnContextMenuRequested(event -> {
            contextMenu.show(controller.getDataMemoryListView(), event.getScreenX(), event.getScreenY());
            event.consume();
        });
    }

    static String[] splitDataLine(String line) {
        switch (line.length()) {
            case 16:
                return new String[] {line.substring(0, 4), line.substring(4, 7), line.substring(7, 10), line.substring(10, 13), line.substring(13, 16)};
            case 14:
                return new String[] {line.substring(0, 4), line.substring(4, 5), line.substring(5, 8), line.substring(8, 11), line.substring(11, 14)};
            case 12:
                return new String[] {line.substring(0, 4), line.substring(4, 5), line.substring(5, 6), line.substring(6, 9), line.substring(9, 12)};
            case 10:
                return new String[] {line.substring(0, 4), line.substring(4, 5), line.substring(5, 6), line.substring(6, 7), line.substring(7, 10)};
            case 8:
                return new String[] {line.substring(0, 4), line.substring(4, 5), line.substring(5, 6), line.substring(6, 7), line.substring(7, 8)};
            default:
                return new String[] {line.substring(0, 4), line.substring(4), " ", " ", " "};
        }
    }
}
