package GUI;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import rat.Rat;

/**
 * A GUI for Rat using FXML.
 */
public class Main extends Application {

    private Rat rat = new Rat("./data/Rat.txt");

    @Override
    public void start(Stage stage) {
        try {
            java.net.URL fxmlUrl = Main.class.getResource("/view/MainWindow.fxml");
            if (fxmlUrl == null) {
                fxmlUrl = Main.class.getResource("/resources/view/MainWindow.fxml");
            }
            if (fxmlUrl == null) {
                java.io.File f1 = new java.io.File("src/main/resources/view/MainWindow.fxml");
                java.io.File f2 = new java.io.File("src/main/java/resources/view/MainWindow.fxml");
                if (f1.isFile()) {
                    fxmlUrl = f1.toURI().toURL();
                } else if (f2.isFile()) {
                    fxmlUrl = f2.toURI().toURL();
                }
            }
            if (fxmlUrl == null) {
                throw new IllegalStateException("FXML not found: /view/MainWindow.fxml (also tried /resources/view/MainWindow.fxml and filesystem fallbacks)");
            }
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setRat(rat);  // inject the Duke instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
