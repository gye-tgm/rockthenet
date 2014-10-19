package rockthenet.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialogs;

/**
 * The controller of the dialog to establish a connection via SNMPv2
 *
 * @author Samuel Schmidt, Elias Frantar
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
    private void initialize() { }

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
    private void handleOk() {
        if (isInputValid()) {
            if (controller.establishReadConnection(address.getText(), Integer.parseInt(port.getText()), community.getText(), security.getText())) { // try connecting
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
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (address.getText().length() == 0)
            errorMessage += "Invalid address \n";

        int portValue = -1;
        try {
        	portValue = Integer.parseInt(port.getText());
        } catch (NumberFormatException e) { }
        if (portValue < 1 || portValue > 65535)
        	errorMessage += "Invalid port (1 - 65535) \n";

        if (community.getText().length() == 0)
            errorMessage += "No Community specified!\n";

        if (security.getText().length() == 0)
            errorMessage += "No Security specified!\n";

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