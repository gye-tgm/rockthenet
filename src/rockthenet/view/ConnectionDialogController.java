package rockthenet.view;

/**
 * Controller for the New Connection Dialog
 * @author Samuel Schmidt
 */

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialogs;

/**
 * Dialog to edit the settings
 *
 * @author Samuel Schmidt
 */
public class ConnectionDialogController {

    @FXML
    private TextField ip;

    private Stage dialogStage;
    private boolean okClicked = false;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Sets the stage of this dialog.
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the person to be edited in the dialog.
     */
    public void setIP() {
        ip.setText("");
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            System.out.println(ip.getText());
            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (ip.getText() == null || ip.getText().length() == 0) {
            errorMessage += "Invalid Connection specified!\n";

            if (errorMessage.length() == 0) {
                return true;
            } else {
                // Show the error message.
                Dialogs.create()
                        .title("Invalid Fields")
                        .masthead("Please correct invalid fields")
                        .message(errorMessage)
                        .showError();
                return false;
            }
        }
        return false;
    }
}