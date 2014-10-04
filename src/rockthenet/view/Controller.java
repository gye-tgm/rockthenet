package rockthenet.view;

import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import rockthenet.Main;

/**
 * Created by Samuel on 29.09.2014.
 */
public class Controller {

    @FXML
    private Menu menuBar;
    @FXML
    private MenuItem settings;
    @FXML
    private MenuItem newConnection;
    @FXML
    private MenuItem about;


    private Main main;

    @FXML
    private void initialize() {
        settings.setOnAction((event) -> settingsDialog());
        newConnection.setOnAction((event) -> newConnectionDialog());
        about.setOnAction((event) -> aboutDialog());

    }

    /**
     * Enables the New Connection Dialog Scene(Window)
     */
    private void newConnectionDialog() {
        main.showNewConnectionDialog();
    }

    /**
     * Enables the Settings Dialog Scene(Window)
     */
    private void settingsDialog() {
        main.showSettingsDialog();
    }

    /**
     * Enables the About Dialog Scene(Window)
     */
    private void aboutDialog() {
        main.showAboutDialog();
    }

    public void setMain(Main main) {
        this.main = main;
    }
}