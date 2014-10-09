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
    private TextField address;
    @FXML
    private TextField port;
    @FXML
    private TextField community;
    @FXML
    private TextField security;

    private Controller controller;
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
        address.setText("");
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
            okClicked = true;
            dialogStage.close();
            controller.establishConnection(address.getText(), Integer.parseInt(port.getText()), community.getText(), security.getText());
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


        if (address.getText() == null || address.getText().length() == 0)
            errorMessage += "Invalid IP specified!\n";

        try {
            if (port.getText() == null || port.getText().length() == 0
                    || Integer.parseInt(port.getText()) < 1 &&
                    Integer.parseInt(port.getText()) > 65535)
                errorMessage += "Valid Port range is 1-65535\n";
        } catch (NumberFormatException nfe) {
            errorMessage += "Valid Port range is 1-65535\n";
        }

        if (community.getText() == null || community.getText().length() == 0)
            errorMessage += "No Community specified!\n";

        if (security.getText() == null || security.getText().length() == 0)
            errorMessage += "No Security specified!\n";

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

    public void setController(Controller controller) {
        this.controller = controller;
    }
}