package rockthenet;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialogs;
import rockthenet.view.ConnectionDialogController;
import rockthenet.view.Controller;
import rockthenet.view.SettingsDialogController;

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
        this.primaryStage.setTitle("Rock the Firewall");
        this.primaryStage.setOnCloseRequest((event) -> Platform.exit());
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
            primaryStage.setScene(scene);
            primaryStage.show();

            // Controller stuff
            Controller controller = loader.getController();
            controller.setMain(this, primaryStage);
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
            dialogStage.initOwner(primaryStage);
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
     *
     */
    public boolean showNewConnectionDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/connectionDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("New Connection");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
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
                .owner(primaryStage)
                .title("About")
                .masthead("Rock the net")
                .message("is a simple-to-use application to monitor and configure a hardware firewall appliance." +
                        "\n(Juniper NetScreen 5GT implemented)")
                .showInformation();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

