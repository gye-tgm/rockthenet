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

    private Controller controller;
    private Stage dialogStage;
    private boolean okClicked = false;
    
    private boolean editMode = false; // default is create-mode

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
    private void handleOk() throws ConnectionException {
        if (isInputValid()) {
            okClicked = true;
            
            if (editMode)
            	controller.updateRule(Integer.parseInt(id.getText()), name.getText(), sourceZone.getText(), destinationZone.getText(), sourceAddress.getText(), destinationAddress.getText(), Integer.parseInt(service.getText()), Integer.parseInt(action.getText()), Integer.parseInt(enabled.getText()));
            else
            	controller.newRule(Integer.parseInt(id.getText()), name.getText(), sourceZone.getText(), destinationZone.getText(), sourceAddress.getText(), destinationAddress.getText(), Integer.parseInt(service.getText()), Integer.parseInt(action.getText()), Integer.parseInt(enabled.getText()));
            
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

    public void setController(Controller controller) {
        this.controller = controller;
    }
    
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
}
