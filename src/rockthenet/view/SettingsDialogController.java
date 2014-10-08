package rockthenet.view;

/**
 * Controller for the Settings Dialog
 * @author Samuel Schmidt
 */

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.controlsfx.dialog.Dialogs;

import rockthenet.SessionSettings;
import rockthenet.validators.EmailValidator;

/**
 * Dialog to edit the settings
 *
 * @author Samuel Schmidt
 */
public class SettingsDialogController {

    @FXML
    private TextField email;
    @FXML
    private CheckBox emailNotificationsEnabled;
    @FXML
    private TextField refreshIntervall;
    @FXML
    private CheckBox refreshIntervallEnabled;

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
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the person to be edited in the dialog.
     */
    public void setFields() {
        email.setText("");
        emailNotificationsEnabled.setSelected(true);
        refreshIntervall.setText("");
        refreshIntervallEnabled.setSelected(true);
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return
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
            SessionSettings.getInstance().setRefreshInterval(Integer.parseInt(refreshIntervall.getText()));
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
        EmailValidator emailValidator = new EmailValidator();

        if (email.getText() == null || email.getText().length() == 0 || !emailValidator.validate(email.getText())) {
            errorMessage += "Invalid e-mail address!\n";
        }

        if (refreshIntervall.getText() == null || refreshIntervall.getText().length() == 0) {
            errorMessage += "Invalid refresh interval!\n";
        }

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
}

