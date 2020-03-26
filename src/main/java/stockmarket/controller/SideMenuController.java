package stockmarket.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;

/**
 * Side Menu Controller
 * @author Damian Horna
 *
 */
public class SideMenuController {

	
	/**
	 * path to scene that contains help information
	 */
	private static final String HELP_SCREEN_FXML = "/view/HelpScreen.fxml";
	/**
	 * path to scene that contains settings
	 */
	private static final String SETTINGS_SCREEN_FXML = "/view/SettingsScreen.fxml";
	/**
	 * path to scene that contains information about simulation
	 */
	private static final String SIMULATION_SCREEN_FXML = "/view/SimulationScreen.fxml";
	/**
	 * path to scene that enables adding new objects to the simulation
	 */
	private static final String ADD_SCREEN_FXML = "/view/AddScreen.fxml";
	/**
	 * path to scene that contains overview of the simulation
	 */private static final String OVERVIEW_SCREEN_FXML = "/view/OverviewScreen.fxml";
	private MainScreenController mainScreenController;


	public void setMainScreenController(MainScreenController mainScreenController) {
		this.mainScreenController = mainScreenController;
	}

	/**
	 * toggle group of side menu buttons
	 */
	@FXML
    private ToggleGroup sideMenuButtons;

    /**
     * sets overview screen as a center
     * @param event action event
     */
    @FXML
    public void showOverview(ActionEvent event) {
    	mainScreenController.setCenter(OVERVIEW_SCREEN_FXML);
    }

    /**
     * sets add screen as a center
     * @param event action event
     */
    @FXML
    public void showAdd(ActionEvent event) {
    	mainScreenController.setCenter(ADD_SCREEN_FXML);
    }

    /**
     * sets simulation screen as a center
     * @param event action event
     */
    @FXML
    public void showSimulation(ActionEvent event) {
    	mainScreenController.setCenter(SIMULATION_SCREEN_FXML);
    }

    /**
     * sets settings screen as a center
     * @param event action event
     */
    @FXML
    public void showSettings(ActionEvent event) {
    	mainScreenController.setCenter(SETTINGS_SCREEN_FXML);
    }
    
    /**
     * sets help screen as a center
     * @param event action event
     */
    @FXML
    public void showHelp(ActionEvent event) {
    	mainScreenController.setCenter(HELP_SCREEN_FXML);
    }

}
