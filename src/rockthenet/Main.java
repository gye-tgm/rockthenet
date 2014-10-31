package rockthenet;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import org.controlsfx.dialog.Dialogs;
import rockthenet.view.*;

import java.io.IOException;

/**
 * @author Samuel
 */
public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private Controller mainController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.getPrimaryStage().setTitle("Rock the Firewall");
        this.getPrimaryStage().setOnCloseRequest((event) -> Platform.exit());
        initRootLayout();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/rockTheNet.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Application-Icon
//            primaryStage.getIcons().add(new
//                    Image(getClass().getResourceAsStream("resources/firewall-icon.ico")));


            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            getPrimaryStage().setScene(scene);
            getPrimaryStage().show();

            // Controller stuff
            Controller controller = loader.getController();
            controller.setMain(this);
            mainController = controller;


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Open SettingsDialog to set notification mail and refresh rate and whether
     * or not these are enabled
     *
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showSettingsDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/settingsDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Settings");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // controller stuff
            SettingsDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Opens a ConnectionDialog to set IP, Port and Community
     */
    public boolean showNewConnectionDialogV3() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/connectionDialogV3.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("New Connection - SNMPv3");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // controller stuff
            ConnectionDialogControllerV3 controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setController(mainController);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Opens a ConnectionDialog to set IP, Port and Community
     */
    public boolean showNewConnectionDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/connectionDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("New Connection - SNMPv2");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // controller stuff
            ConnectionDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setController(mainController);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Information about the application
     */
    public void showAboutDialog() {
        Dialogs.create()
                .owner(getPrimaryStage())
                .title("About")
                .masthead("Rock the net")
                .message("is a simple-to-use application to monitor and configure a hardware firewall appliance." +
                        "\n(Juniper NetScreen 5GT implemented)")
                .showInformation();
    }

    public boolean showNewRuleDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/newRuleDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("New Firewall-rule");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // controller stuff
            NewRuleController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setController(mainController);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showSSHConnectionDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/sshDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("SSH-Connection (needed for New/Edit)");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // controller stuff
            SSHDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setController(mainController);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    public boolean showEditRuleDialog(PolicyRow policy) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/newRuleDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Firewall-rule");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // controller stuff
            NewRuleController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setController(mainController);
            controller.configureAsEditDialog(policy);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Notification shown when Policies have changed on the device
     */
    public void showNotification(String title, String text) {
        Notifications.create()
                .title(title)
                .text(text)
                .showInformation();
    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
