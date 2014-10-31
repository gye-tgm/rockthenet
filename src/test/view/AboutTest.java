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
import rockthenet.view.Controller;

import static org.loadui.testfx.Assertions.verifyThat;
import static org.loadui.testfx.controls.Commons.hasText;

/**
 * Created by Niko on 29/10/14.
 */
@Category(TestFX.class)
public class AboutTest extends GuiTest {

    @Override
    protected Parent getRootNode() {
        final Main main = new Main();
        try {
            //main.init();
            //main.start(main.primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return main.rootLayout;
    }

    @Test
    public void aboutDialog()
    {
        click("Help");
        click("About");
        click("OK");
        verifyThat(".tableView", hasText("Name"));
    }

/*    @Test
    public void validInput_loginSuccessfully()
    {
        click("E-mail").type("test@email.com");
        click("#refreshIntervall").type("1");
        click("OK");
        verifyThat(".tableView", hasText("was clicked"));
    }*/
}
