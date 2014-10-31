package test.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.categories.TestFX;
import rockthenet.Main;
import rockthenet.connections.Connection;
import rockthenet.view.Controller;
import rockthenet.view.SettingsDialogController;

import static org.loadui.testfx.Assertions.assertNodeExists;
import static org.loadui.testfx.Assertions.verifyThat;
import static org.loadui.testfx.controls.Commons.hasText;
import static org.loadui.testfx.controls.TableViews.containsCell;


/**
 * Created by Niko on 20/10/14.
 */
@Category(TestFX.class)
public class SettingsTest extends GuiTest{


    //@Override
    protected Parent getRootNode() {
        final Button btn = new Button();
        btn.setLayoutX(30);
        btn.setLayoutY(30);
        btn.setId("btn");
        btn.setText("Hello World");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                btn.setText( "was clicked" );
            }
        });
        return btn;
    }
/*
    @Test
    public void validInput_loginSuccessfully()
    {
        click("E-mail").type("test@email.com");
        click("#refreshIntervall").type("1");
        click("OK");
        verifyThat(".tableView", containsCell("1"));
    }
*/

    @Test
    public void shouldClickButton(){
        final Button button = find( "#btn" );
        click(button);
        verifyThat( "#btn", hasText("was clicked") );
    }


    /*
    @Test
    public void validTableOutput(){
        click("Connection");
        click("New");
        click("SNMPv2");
        click("OK");
        verifyThat(".tableView", containsCell("1"));
    }*/
}

