package stockmarket.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import stockmarket.utils.FXMLUtils;

/**
 * The controller of main screen
 * @author Damian Horna
 *
 */
public class MainScreenController {

	
	/**
	 * handler for main screen
	 */
	@FXML
	private MainScreenController mainScreenController;
	
	/**
	 * handler for border pane
	 */
	@FXML
	private BorderPane borderPane;
	
	/**
	 * handler for side menu
	 */
	@FXML
	private SideMenuController sideMenuScreenController;
	
	/**
	 * initialize method used to set main screen at the stage
	 */
	@FXML
	private void initialize() {
		sideMenuScreenController.setMainScreenController(this);
	}
	
	
	/** setting selected scene on the stage
	 * @param path a path to selected scene
	 */
	public void setCenter(String path) {
		borderPane.setCenter(FXMLUtils.fxmlLoader(path));
	}
}
