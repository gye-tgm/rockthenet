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

    private Main main;

    @FXML
    private void initialize() {
        settings.setOnAction((event) -> settingsDialog());
        //newConnection.setOnAction((event) -> newConnection());

        /**settings.setOnAction(event -> {
         MenuItem settings = (MenuItem) event.getSource();
         if (settings.) {
         //new SettingsDialog(this,"Settings");
         Action response = Dialogs.create()
         //.owner( isOwnerSelected ? stage : null)
         .title("You do want dialogs right?")
         //.masthead(isMastheadVisible() ? "Just Checkin'" : null)
         .message( "I was a bit worried that you might not want them, so I wanted to double check.")
         .showWarning();
         }
         });
         **/
    }

    /**
     * Enables the New Connection Dialog Scene(Window)
     */
    private void newConnection() {
        main.showNewConnectionDialog();
    }

    /**
     * Enables the Settings Dialog Scene(Window)
     */
    private void settingsDialog() {
        main.showSettingsDialog();
    }

    public void setMain(Main main) {
        this.main = main;
    }
}