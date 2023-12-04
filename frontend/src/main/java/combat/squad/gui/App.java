package combat.squad.gui;

import combat.squad.controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        var loader = new FXMLLoader();
        loader.setLocation(App.class.getClassLoader().getResource("javafx.fxml"));

        VBox rootVbox = loader.load();

        MainController controller = loader.getController();
        controller.initialize();

        Scene scene = new Scene(rootVbox, 600, 600);

        primaryStage.setScene(scene);

        primaryStage.titleProperty().set("Meeting Schedule App");
        primaryStage.show();
    }
}
