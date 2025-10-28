package seedu.job.ui;

import static java.util.Objects.requireNonNull;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

/**
 * A ui for the status bar that is displayed at the header of the application.
 */
public class ResultDisplay extends UiPart<Region> {

    private static final String FXML = "ResultDisplay.fxml";
    private static final String ERROR_STYLE_CLASS = "error";

    @FXML
    private Label resultDisplay;

    public ResultDisplay() {
        super(FXML);
    }

    public void setFeedbackToUser(String feedbackToUser) {
        requireNonNull(feedbackToUser);
        resultDisplay.setText(feedbackToUser);
        resultDisplay.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    public void setErrorToUser(String errorMessage) {
        requireNonNull(errorMessage);
        resultDisplay.setText(errorMessage);
        if (!resultDisplay.getStyleClass().contains(ERROR_STYLE_CLASS)) {
            resultDisplay.getStyleClass().add(ERROR_STYLE_CLASS);
        }
    }

}
