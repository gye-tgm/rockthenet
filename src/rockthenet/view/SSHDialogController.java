package rockthenet.view;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialogs;
import rockthenet.connections.ConnectionException;

/**
 * The controller of the dialog to establish a connection via SNMPv2
 *
 * @author Samuel Schmidt, Elias Frantar
 */
public class SSHDialogController {

    @FXML
    private TextField address;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

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
     * Returns true if the user clicked OK, false otherwise.
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() throws ConnectionException {
        if (isInputValid()) {
            if (controller.sshConnection(address.getText(), username.getText(), password.getText())) {
                okClicked = true;
                dialogStage.close();
            }
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
     * Validates the user input in the Text/PasswordFields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (address.getText().length() == 0)
            errorMessage += "Invalid address \n";

        if (username.getText().length() == 0)
            errorMessage += "No Username specified!\n";

        if (password.getText().length() == 0)
            errorMessage += "No Password specified!\n";

        if (errorMessage.length() != 0) {
            Dialogs.create() // show error Dialog
                    .title("Invalid Fields")
                    .masthead("Please correct invalid fields")
                    .message(errorMessage)
                    .showError();
            return false;
        }

        return true;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}