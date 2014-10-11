package rockthenet.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.controlsfx.dialog.Dialogs;

import rockthenet.SessionSettings;
import rockthenet.validators.EmailValidator;

/**
 * The controller of the dialog to edit the application settings
 *
 * @author Samuel Schmidt, Elias Frantar
 * @version 2014-10-11
 */
public class SettingsDialogController {

    @FXML
    private TextField email;
    @FXML
    private TextField refreshIntervall;

    private Stage dialogStage;
    private boolean okClicked = false;
    
    private SessionSettings session; // instance to SessionSettings

    /**
     * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    	session = SessionSettings.getInstance();
    	
    	email.setText(session.getEmail());
        refreshIntervall.setText("" + session.getRefreshInterval());
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * @return  true if the user clicked OK, false otherwise
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        String errorMessage = "";
        
    	String emailText = email.getText();
    	if (new EmailValidator().validate(emailText))
    		session.setEmail(emailText);
    	else
    		errorMessage += "Invalid email-address \n";
    	
    	int refreshIntervalValue = -1;
    	try {
    		refreshIntervalValue = Integer.parseInt(refreshIntervall.getText());
    	} catch (Exception e) {}
    	if (refreshIntervalValue >= 1 && refreshIntervalValue <= 1000)
    		session.setRefreshInterval(refreshIntervalValue);
    	else
    		errorMessage += "Invalid refresh interval (1 - 1000) \n";
    		
    	if (errorMessage.length() > 0)
            Dialogs.create() // show error message
                    .title("Invalid Fields")
                    .masthead("Please correct invalid fields.")
                    .message(errorMessage)
                    .showError();
    	else {
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
}

