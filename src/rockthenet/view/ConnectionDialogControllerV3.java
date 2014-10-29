package rockthenet.view;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.controlsfx.dialog.Dialogs;

/**
 * The controller of the dialog to establish a connection via the SNMPv3 protocol
 *
 * @author Samuel Schmidt
 * @author Elias Frantar
 * @version 2014-10-29
 */
public class ConnectionDialogControllerV3 {
	
	/* fields mapped to FXML */
    @FXML
    private TextField addressV3;
    @FXML
    private TextField portV3;
    @FXML
    private TextField username;
    @FXML
    private PasswordField authentificationPassword;
    @FXML
    private PasswordField securityPassword;

    /* other state attributes */
    private Controller controller;
    private Stage dialogStage;
    private boolean okClicked = false;

    /**
     * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
     */
    @FXML
    private void initialize() { }

    /**
     * Sets the stage of this dialog.
     * @param dialogStage the primary dialog stage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Returns if OK has been clicked.
     * @return true if OK clicked, false otherwise
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Handles the OK-button click. <br>
     * Called when the OK-button is pressed.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
        	if (controller.establishConnectionV3(addressV3.getText(), Integer.parseInt(portV3.getText()), username.getText(), authentificationPassword.getText(), securityPassword.getText())) {
                okClicked = true;
                dialogStage.close();
        	}
        }
    }

    /**
     * Handles the Cancel-button click. <br>
     * Called when the OK-button is pressed.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user's input of all input fields.
     * <p> Shows an error dialog mentioning the invalid fields in case of input errors.
     * 
     * @return true if the input is valid; false otherwise
     */
    private boolean isInputValid() {
    	String errorMessage = "";

        if (addressV3.getText().length() == 0)
            errorMessage += "Invalid address \n";

        int portValue = -1;
        try {
        	portValue = Integer.parseInt(portV3.getText());
        } catch (NumberFormatException e) { }
        if (portValue < 1 || portValue > 65535)
        	errorMessage += "Invalid port (1 - 65535) \n";

        if (username.getText().length() == 0)
            errorMessage += "No Username specified!\n";

        if (authentificationPassword.getText().length() == 0)
            errorMessage += "No Authentification Password specified!\n";
        
        if (securityPassword.getText().length() == 0)
            errorMessage += "No Security Password specified!\n";

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

    /* simple Getters and Setters; no documentation necessary */
    public void setController(Controller controller) { this.controller = controller; }
    
}
