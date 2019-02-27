package kurilenko;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kurilenko.view.Controller;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("view/main-view.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/main-view.fxml"));
        Parent root = (Parent)loader.load();
        Controller controller = (Controller)loader.getController();
        primaryStage.setTitle("Digital Image Processing");
        primaryStage.setScene(new Scene(root, 1240, 800));
        primaryStage.show();
        controller.setStage(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
