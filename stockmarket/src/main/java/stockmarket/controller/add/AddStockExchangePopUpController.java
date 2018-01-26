package stockmarket.controller.add;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import stockmarket.model.Currency;
import stockmarket.model.StockExchange;
import stockmarket.utils.BlurUtils;
import stockmarket.utils.PseudoDB;
import stockmarket.utils.NumberUtils;
/**
 * The controller of pop up responsible for adding new stock exchange to the simulation.
 * @author Damian Horna
 *
 */
public class AddStockExchangePopUpController {

	/**
	 * name of stock exchange to be added
	 */
	@FXML
	private JFXTextField name;

	/**
	 * name of country in which the stock exchange is located
	 */
	@FXML
	private JFXComboBox<String> country;

	/**
	 * name of the city in which the stock exchange is located
	 */
	@FXML
	private JFXTextField city;

	/**
	 * address of stock exchange
	 */
	@FXML
	private JFXTextField address;

	/**
	 * currency selection
	 */
	@FXML
	private JFXComboBox<String> currencyComboBox;

	/**
	 * profit margin of stock exchange
	 */
	@FXML
	private JFXTextField profitMargin;

	/**
	 * stack pane to display error messages
	 */
	@FXML
	private StackPane stackPane;

	/**
	 * icon used for closing the application
	 */
	@FXML
	private MaterialDesignIconView closePopUpIco;

	/**
	 * used for adding new stock exchange to the simulation
	 */
	@FXML
	private JFXButton addIco;

	/**
	 * closes the application
	 * @param event mouse event that triggers closing the application
	 */
	@FXML
	public void closePopUp(MouseEvent event) {
		Stage stage = (Stage) closePopUpIco.getScene().getWindow();
		BlurUtils.unblur();
		stage.close();
	}

	/**
	 * initializes the controller with data
	 */
	@FXML
	public void initialize() {
		List<Currency> currencies = PseudoDB.getCurrencies();
		List<String> currenciesStr = new ArrayList<>();
		currencies.forEach(c -> currenciesStr.add(c.getName()));
		ObservableList<String> currenciesOL = FXCollections.observableArrayList(currenciesStr);
		currencyComboBox.getItems().addAll(currenciesOL);

		List<String> countries = PseudoDB.getCountries(new Locale("en"));
		ObservableList<String> countriesOL = FXCollections.observableArrayList(countries);
		country.getItems().addAll(countriesOL);
	}

	/**
	 * adds new exchange market to the simulation, checking the data for validity
	 */
	@FXML
	public void addExchangeMarket() {
		String eName = name.getText();
		String eCountry = country.getValue();
		String eCity = city.getText();
		String eAddress = address.getText();
		String eProfitMargin = profitMargin.getText();
		String eCurrency = currencyComboBox.getValue();

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
		} else if (currencyComboBox.getSelectionModel().isEmpty()) {
			showErrorDialog("Choose a currency");
		} else {
			StockExchange eMarket = new StockExchange(eName, eCountry, eCity, eAddress, new BigDecimal(eProfitMargin),
					PseudoDB.getCurrencyByName(eCurrency));
			PseudoDB.addNewStockExchange(eMarket);
			PseudoDB.showAllStockExchanges();
			Stage stage = (Stage) closePopUpIco.getScene().getWindow();
			BlurUtils.unblur();
			stage.close();
		}

	}

	/**
	 * shows error dialog with a message
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
