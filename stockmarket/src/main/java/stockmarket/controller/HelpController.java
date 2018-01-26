package stockmarket.controller;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Help
 * @author Damian Horna
 *
 */
public class HelpController {
	
	/**
	 *  icon used for closing the window
	 */
	@FXML
    private MaterialDesignIconView closeIcon;

	/** used for close the window
	 * @param event mouse event that activates closing the window
	 */
	@FXML
	public void closeApp(MouseEvent event) {
		Stage stage = (Stage) closeIcon.getScene().getWindow();
		stage.close();
	}
}
