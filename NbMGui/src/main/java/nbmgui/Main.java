package nbmgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("NoBeard Machine");

        initRootLayout();
    }

    public void initRootLayout() {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("RootLayout.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add("style.css");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
