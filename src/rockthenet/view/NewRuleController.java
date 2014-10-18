package rockthenet.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialogs;
import rockthenet.connections.ConnectionException;

/**
 * The controller of the dialog to establish a connection via SNMPv2
 *
 * @author Samuel Schmidt, Elias Frantar
 */
public class NewRuleController {

    @FXML
    private TextField name;
    @FXML
    private TextField sourceZone;
    @FXML
    private TextField destinationZone;
    @FXML
    private TextField sourceAddress;
    @FXML
    private TextField destinationAddress;
    @FXML
    private TextField service;
    @FXML
    private TextField action;
    @FXML
    private TextField enabled;


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
            controller.newRule(name.getText(), sourceZone.getText(), destinationZone.getText(), sourceAddress.getText(),
                    destinationAddress.getText(), Integer.parseInt(service.getText()), Integer.parseInt(action.getText()), Integer.parseInt(enabled.getText()));
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
     * Validates the user input in the Text/PasswordFields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (name.getText() == null || name.getText().length() == 0)
            errorMessage += "No text specified!\n";

        if (sourceZone.getText() == null || sourceZone.getText().length() == 0)
            errorMessage += "No Source Zone specified!\n";

        if (destinationZone.getText() == null || destinationZone.getText().length() == 0)
            errorMessage += "No Destination Zone specified!\n";

        if (sourceAddress.getText() == null || sourceAddress.getText().length() == 0)
            errorMessage += "No Source Address specified!\n";

        if (destinationAddress.getText() == null || destinationAddress.getText().length() == 0)
            errorMessage += "No Destination Address specified!\n";
        try {
            if (service.getText() == null || service.getText().length() == 0) {
                Integer.parseInt(service.getText());
                errorMessage += "No Service specified!\n";
            }
        } catch (NumberFormatException nfe) {
            errorMessage += "Valid Action Input is 0/1!\n";
        }
        try {
            if (action.getText() == null || action.getText().length() == 0 || (Integer.parseInt(action.getText()) != 0 && Integer.parseInt(action.getText()) != 1))
                errorMessage += "Valid Action Input is 0/1!\n";
        } catch (NumberFormatException nfe) {
            errorMessage += "Valid Action Input is 0/1!\n";
        }

        try {
            if (enabled.getText() == null || enabled.getText().length() == 0 || (Integer.parseInt(enabled.getText()) != 0 && Integer.parseInt(enabled.getText()) != 1))
                errorMessage += "Valid Enabled Input is 0/1!\n";
        } catch (NumberFormatException nfe) {
            errorMessage += "Valid Enabled Input is 0/1!\n";
        }

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