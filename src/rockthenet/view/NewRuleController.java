package rockthenet.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialogs;
import rockthenet.connections.ConnectionException;

/**
 * The controller of the dialog for creating/editing a rule
 * <p> This controller can either be in <i>create</i> or in <i>edit</i> mode. Editing-mode can be activated via
 * {@link #configureAsEditDialog(PolicyRow)}.
 *
 * @author Samuel Schmidt
 * @author Elias Frantar
 * @version 2014-10-29
 */
public class NewRuleController {

    /* fields mapped to FXML */
    @FXML
    private TextField id;
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

    /* other state attributes */
    private Controller controller;
    private Stage dialogStage;
    private boolean okClicked = false;

    private boolean editMode = false; // default is create-mode

    /**
     * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage the primary dialog stage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Returns if OK has been clicked.
     *
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
    private void handleOk() throws ConnectionException {
        if (isInputValid()) {
            okClicked = true;

            if (editMode)
                controller.updateRule(Integer.parseInt(id.getText()), name.getText(), sourceZone.getText(),
                        destinationZone.getText(), sourceAddress.getText(), destinationAddress.getText(),
                        Integer.parseInt(service.getText()), Integer.parseInt(action.getText()),
                        Integer.parseInt(enabled.getText()));
            else
                controller.newRule(Integer.parseInt(id.getText()), name.getText(), sourceZone.getText(),
                        destinationZone.getText(), sourceAddress.getText(), destinationAddress.getText(),
                        Integer.parseInt(service.getText()), Integer.parseInt(action.getText()),
                        Integer.parseInt(enabled.getText()));

            dialogStage.close();
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

        try {
            Integer.parseInt(id.getText());
        } catch (NumberFormatException nfe) {
            errorMessage += "Invalid Id specified!\n";
        }

        if (name.getText().length() == 0)
            errorMessage += "No text specified!\n";

        if (sourceZone.getText().length() == 0)
            errorMessage += "No Source Zone specified!\n";

        if (destinationZone.getText().length() == 0)
            errorMessage += "No Destination Zone specified!\n";

        if (sourceAddress.getText().length() == 0)
            errorMessage += "No Source Address specified!\n";

        if (destinationAddress.getText().length() == 0)
            errorMessage += "No Destination Address specified!\n";

        try {
            Integer.parseInt(service.getText());
        } catch (NumberFormatException nfe) {
            errorMessage += "Invalid Service specified (0-1)!\n";
        }

        try {
            Integer.parseInt(action.getText());
        } catch (NumberFormatException nfe) {
            errorMessage += "Invalid Action specified (0-1)!\n";
        }

        try {
            Integer.parseInt(enabled.getText());
        } catch (NumberFormatException nfe) {
            errorMessage += "Invalid Enabled specified (0-1)!\n";
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

    /**
     * Configures this controller as edit-dialog-controller for the given policy.
     *
     * @param policy the policy to edit with this controller
     */
    public void configureAsEditDialog(PolicyRow policy) {
        this.id.setText("" + policy.getId());
        this.name.setText(policy.getName());
        this.sourceZone.setText(policy.getSrcZone());
        this.destinationAddress.setText(policy.getDstZone());
        this.sourceAddress.setText(policy.getSrcAddress());
        this.destinationAddress.setText(policy.getDstAddress());
        this.service.setText("" + policy.getService());
        this.action.setText("" + policy.getAction());
        this.enabled.setText("" + policy.getActiveStatus());

        this.id.setDisable(true);
        editMode = true;
    }

    /* simple Getters and Setters; no documentation necessary */
    public void setController(Controller controller) {
        this.controller = controller;
    }
}
