package stockmarket.controller.add;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton.ButtonType;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import stockmarket.model.ExchangeMarket;
import stockmarket.utils.BlurUtils;
import stockmarket.utils.PseudoDB;
import stockmarket.utils.NumberUtils;
/**
 * The controller of pop up responsible for adding new exchange markets to the simulation.
 * @author Damian Horna
 *
 */
public class AddExchangeMarketPopUpController {

	/**
	 * name of exchange market
	 */
	@FXML
	private JFXTextField name;
	
	/**
	 * country of exchange market
	 */
	@FXML
	private JFXComboBox<String> country;

	/**
	 * city of exchange market
	 */
	@FXML
	private JFXTextField city;
	
	/**
	 * address of exchange market
	 */
	@FXML
	private JFXTextField address;

	/**
	 * profit margin of exchange market
	 */
	@FXML
	private JFXTextField profitMargin;

	/**
	 * stack pane for displaying error messages
	 */
	@FXML
	private StackPane stackPane;

	/**
	 * icon for closing the pop up
	 */
	@FXML
	private MaterialDesignIconView closePopUpIco;

	/**
	 * icon for adding new exchange market to the simulation
	 */
	@FXML
	private JFXButton addIco;

	/**
	 * closes the application
	 * @param event mouse event closes the app
	 */
	@FXML
	public void closePopUp(MouseEvent event) {
		Stage stage = (Stage) closePopUpIco.getScene().getWindow();
		BlurUtils.unblur();
		stage.close();
	}

	/**
	 * initializes country list with available countries
	 */
	@FXML
	public void initialize() {
		List<String> countries = PseudoDB.getCountries(new Locale("en"));
		ObservableList<String> countriesOL = FXCollections.observableArrayList(countries);
		country.getItems().addAll(countriesOL);
	}
	
	/**
	 * adds new exchange market to the simulation  and checks the data given by the user for validity
	 */
	@FXML
	public void addExchangeMarket() {
		String eName = name.getText();
		String eCountry = country.getValue();
		String eCity = city.getText();
		String eAddress = address.getText();
		String eProfitMargin = profitMargin.getText();

		
		if (eName.isEmpty()) {
			showErrorDialog("Provide valid name");
		} else if (country.getSelectionModel().isEmpty()) {
			showErrorDialog("Provide a country");
		} else if (eCity.isEmpty()) {
			showErrorDialog("Provide a city");
		} else if (eCountry.isEmpty()) {
			showErrorDialog("Provide adress");
		} else if (!NumberUtils.isNumeric(eProfitMargin)) {
			showErrorDialog("Given profit margin is not valid");
		} else {
			ExchangeMarket eMarket = new ExchangeMarket(eName, eCountry, eCity, eAddress, new BigDecimal(eProfitMargin));
			PseudoDB.addNewExchangeMarket(eMarket);
			PseudoDB.showAllExchangeMarkets();
			Stage stage = (Stage) closePopUpIco.getScene().getWindow();
			BlurUtils.unblur();
			stage.close();
		}

	}

	/**
	 * displays pop up with error message
	 * @param msg message
	 */
	private void showErrorDialog(String msg) {
		JFXDialogLayout content = new JFXDialogLayout();
		content.setHeading(new Text("ERROR"));
		content.setBody(new Text(msg));
		JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
		;

		JFXButton button = new JFXButton("I understand");
		button.setButtonType(ButtonType.RAISED);
		button.setCursor(Cursor.HAND);
		button.setOnAction(e -> dialog.close());
		content.setActions(button);
		dialog.show();
	}

}
