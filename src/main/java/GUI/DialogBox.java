package GUI;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents a dialog box consisting of an ImageView to represent the speaker's face
 * and a label containing text from the speaker.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image img) {
        try {
            java.net.URL fxmlUrl = MainWindow.class.getResource("/view/DialogBox.fxml");
            if (fxmlUrl == null) {
                fxmlUrl = MainWindow.class.getResource("/resources/view/DialogBox.fxml");
            }
            if (fxmlUrl == null) {
                java.io.File f1 = new java.io.File("src/main/resources/view/DialogBox.fxml");
                java.io.File f2 = new java.io.File("src/main/java/resources/view/DialogBox.fxml");
                if (f1.isFile()) {
                    fxmlUrl = f1.toURI().toURL();
                } else if (f2.isFile()) {
                    fxmlUrl = f2.toURI().toURL();
                }
            }
            if (fxmlUrl == null) {
                throw new IllegalStateException("FXML not found: /view/DialogBox.fxml (also tried /resources/view/DialogBox.fxml and filesystem fallbacks)");
            }
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        displayPicture.setImage(img);
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
    }

    public static DialogBox getUserDialog(String text, Image img) {
        return new DialogBox(text, img);
    }

    public static DialogBox getRatDialog(String text, Image img) {
        var db = new DialogBox(text, img);
        db.flip();
        return db;
    }
}
