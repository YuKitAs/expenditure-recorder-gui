import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("expenditure-recorder-gui.fxml"));
        primaryStage.setTitle("Expenditure Recorder");
        Scene scene = new Scene(root, 501, 500);
        Font.loadFont(getClass().getResourceAsStream("KGTraditionalFractions.ttf"), 30);
        Font.loadFont(getClass().getResourceAsStream("Roundo-SemiBold.otf"), 15);
        scene.getStylesheets().addAll(getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
