package nbmgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("nbm_layout.fxml"));
        primaryStage.setTitle("NoBeard Machine");
        primaryStage.setScene(new Scene(root, 1280, 800));
        primaryStage.show();
    }


    public static void main(String[] args) {
        //NbM.main(new String[]{"../SamplePrograms/NoBeardPrograms/HelloWorld.nb"});
        launch(args);
    }
}
