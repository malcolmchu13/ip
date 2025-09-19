package gui;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import rat.Rat;
import java.io.File;
/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Rat rat;

    private Image userImage;
    private Image ratImage;

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        // Load images with fallbacks for IDE vs Gradle resource layouts
        userImage = loadImage("images/DaUser.png");
        ratImage = loadImage("images/RAT.png");
    }

    private Image loadImage(String relativePath) {
        var url = getClass().getResource("/" + relativePath);
        if (url == null) {
            url = getClass().getResource("/resources/" + relativePath);
        }
        if (url != null) {
            return new Image(url.toExternalForm());
        }
        // Fallback to filesystem paths for IDE runs that didn't copy resources
        File f1 = new File("src/main/resources/" + relativePath);
        if (f1.isFile()) {
            return new Image(f1.toURI().toString());
        }
        File f2 = new File("src/main/java/resources/" + relativePath);
        if (f2.isFile()) {
            return new Image(f2.toURI().toString());
        }
        throw new IllegalStateException("Resource not found: " + relativePath);
    }

    /** Injects the Duke instance */
    public void setRat(Rat d) {
        assert d != null : "MainWindow requires a non-null Rat instance";
        rat = d;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        assert rat != null : "Rat instance must be set before handling input";
        String input = userInput.getText();
        assert input != null : "User input text should not be null";
        String response = rat.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getRatDialog(response, ratImage)
        );
        userInput.clear();
    }
}
