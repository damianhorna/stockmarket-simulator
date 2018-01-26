package stockmarket.controller;

import java.util.Locale;

import org.apache.commons.lang3.math.NumberUtils;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXButton.ButtonType;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import stockmarket.simulation.SimulationState;
import stockmarket.utils.DialogUtils;
import stockmarket.utils.PopUpUtils;
import stockmarket.utils.PseudoDB;
import stockmarket.utils.RandomDataGenerator;

/**
 * The controller of simulation screen
 * @author Damian Horna
 *
 */
public class SimulationController {

	/**
	 * state of simulation
	 */
	SimulationState state;

	/**
	 * random data generator instance
	 */
	RandomDataGenerator generator = new RandomDataGenerator();

	/**
	 * type of object the user wants to add
	 */
	@FXML
	private JFXComboBox<String> type;

	/**
	 * quantity of objects to add
	 */
	@FXML
	private JFXTextField quantity;

	/**
	 * used for closing the window
	 */
	@FXML
	private MaterialDesignIconView closeIcon;

	/**
	 * enables saving current state of simulation
	 */
	@FXML
	private JFXButton saveButton;

	/**
	 * enables loading a specific state of simulation
	 */
	@FXML
	private JFXButton loadButton;

	/**
	 * stack pane for displaying error message
	 */
	@FXML
	private StackPane stackPane;

	/**
	 * button that strats new simulation
	 */
	@FXML
	private JFXButton newButton;

	/**
	 * the second stack pane to display information pop up
	 */
	@FXML
	private StackPane sp2;

	/**
	 * initializes comobo box with types of objects that might be added to the simulation
	 */
	@FXML
	public void initialize() {
		ObservableList<String> types = FXCollections.observableArrayList("Investor", "Investment Fund", "Company",
				"Currency", "Raw Material", "Exchange Market", "Raw Materials Market", "Stock Exchange",
				"Stock Exchange Index");
		type.getItems().addAll(types);
	}

	/**
	 * displays pop up for saving current state of simulation
	 */
	@FXML
	public void saveSimulationState() {
		PopUpUtils.showPopUp(closeIcon, "/view/manager/SaveManager.fxml");

	}

	/**
	 * displays pop up for loading a specific simulation state
	 */
	@FXML
	public void loadSimulationState() {
		PopUpUtils.showPopUp(closeIcon, "/view/manager/LoadManager.fxml");
	}

	/**
	 * handles adding new objects to the simulation
	 */
	@FXML
	public void addObjects() {
		String oType = type.getValue();
		String oQuantity = quantity.getText();
		boolean errOccurred = false;

		if (type.getSelectionModel().isEmpty()) {
			showErrorDialog("ERROR", "Select type");
		} else if (!NumberUtils.isNumber(oQuantity) || oQuantity.equals("0")) {
			showErrorDialog("ERROR", "Provide a proper quantity");
		} else {
			int quan = Integer.parseInt(oQuantity);
			switch (oType) {
			case "Investor":
				generator.newInvestors(quan);
				break;
			case "Investment Fund":
				generator.newInvestmentFunds(quan);
				break;
			case "Company":
				try {
					generator.newCompanies(quan);
					if(PseudoDB.isAutoAdding()) {
						generator.newInvestors(quan*3);	
					}
					
				} catch (IllegalArgumentException e) {
					showErrorDialog("Erorr", e.getMessage());
					errOccurred = true;
				}

				break;
			case "Currency":
				try {	
					generator.newCurrencies(quan);
					if(PseudoDB.isAutoAdding()) {
						generator.newInvestors(quan*3);	
					}
				} catch (IllegalArgumentException e) {
					showErrorDialog("ERROR", "You can't add more currencies.");
					errOccurred = true;
				}
				break;
			case "Raw Material":
				try {
					generator.newRawMaterials(quan);
					if(PseudoDB.isAutoAdding()) {
						generator.newInvestors(quan*3);	
					}
				} catch (Exception e){
					showErrorDialog("ERROR", e.getMessage());
					errOccurred = true;
				}
				
				break;
			case "Exchange Market":
				generator.newExchangeMarkets(quan);
				break;
			case "Raw Materials Market":
				generator.newRawMaterialsMarket(quan);
				break;
			case "Stock Exchange":
				try {
					generator.newStockExchange(quan);
				} catch (IllegalArgumentException e) {
					DialogUtils.showFancyErrorDialog(stackPane, "Error", e.getMessage());
					errOccurred = true;
				}

				break;
			case "Stock Exchange Index":
				try {
					generator.newStockExchangeIndex(quan);

				} catch (IllegalArgumentException e) {
					showErrorDialog("ERROR", "You can't add any indexes(no Stock Markets).");
					errOccurred = true;
				}
				break;
			}

			if (!errOccurred) {
				showErrorDialog("SUCCESS", "You've just added " + oQuantity + " " + oType + "!");
				quantity.clear();
			}
		}
	}

	/**
	 * creates new simulation, clears all the redundant data
	 */
	@FXML
	public void newSimulation() {
		RandomDataGenerator generator = new RandomDataGenerator();
		PseudoDB.getCompanies().clear();
		generator.setCountriesForCurrencies(PseudoDB.getCountries(new Locale("en")));
		PseudoDB.getStockExchangeIndexes().clear();
		PseudoDB.getCurrencies().clear();
		PseudoDB.getInvestmentFunds().clear();
		PseudoDB.getInvestors().clear();
		PseudoDB.getStockExchanges().clear();
		PseudoDB.getRawMaterials().clear();
		PseudoDB.getRawMaterialsMarkets().clear();
		PseudoDB.getExchangeMarkets().clear();
		DialogUtils.showFancyErrorDialog(sp2, "Info", "new simulation has just begun");
	}

	/**
	 * closes the application
	 * @param event mouse event that launches the closing
	 */
	@FXML
	public void closeApp(MouseEvent event) {
		Stage stage = (Stage) closeIcon.getScene().getWindow();
		stage.close();
		Platform.exit();
		System.exit(0);
	}

	/**
	 * displays the error dialog
	 * @param type type of error dialog
	 * @param msg message
	 */
	private void showErrorDialog(String type, String msg) {
		JFXDialogLayout content = new JFXDialogLayout();
		content.setHeading(new Text(type));
		content.setBody(new Text(msg));
		JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);
		;

		JFXButton button = new JFXButton("Ok");
		button.setButtonType(ButtonType.RAISED);
		button.setCursor(Cursor.HAND);
		button.setOnAction(e -> dialog.close());
		content.setActions(button);
		dialog.show();
	}
}
