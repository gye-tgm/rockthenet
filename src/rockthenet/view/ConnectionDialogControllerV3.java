package rockthenet.view;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.controlsfx.dialog.Dialogs;

/**
 * Created by Samuel on 09.10.2014.
 */
public class ConnectionDialogControllerV3 {
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
        	if (controller.establishConnectionV3(addressV3.getText(), Integer.parseInt(portV3.getText()), username.getText(), authentificationPassword.getText(), securityPassword.getText())) {
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

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
