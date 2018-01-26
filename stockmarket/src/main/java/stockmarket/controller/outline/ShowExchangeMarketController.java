package stockmarket.controller.outline;

import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import stockmarket.model.Currency;
import stockmarket.model.ExchangeMarket;
import stockmarket.utils.BlurUtils;
import stockmarket.utils.PseudoDB;

/**
 * controller for displaying pop up with information about exchange market
 * @author Damian Horna
 *
 */
public class ShowExchangeMarketController {
	/**
	 * address of exchange market
	 */
	@FXML
	private Label emAddress;

	/**
	 * city of exchange market
	 */
	@FXML
	private Label emCity;

	/**
	 * country of exchange market
	 */
	@FXML
	private Label emCountry;

	/**
	 * name of exchange market
	 */
	@FXML
	private Label emName;

	/**
	 * add currency to exchange market button
	 */
	@FXML
	private JFXButton emAddButton;

	/**
	 * remove currency from exchange market button
	 */
	@FXML
	private JFXButton emRemoveButton;

	/**
	 * select a currency for adding
	 */
	@FXML
	private JFXComboBox<String> emAddComboBox;

	/**
	 * profit margin of an exchange market
	 */
	@FXML
	private Label emProfitMargin;

	/**
	 * remove currency from exchange market selection
	 */
	@FXML
	private JFXComboBox<String> emRemoveComboBox;

	/**
	 * reference to displayed exchange market
	 */
	private ExchangeMarket exchangeMarket;

	/**
	 * for closing the window
	 */
	@FXML
	private MaterialDesignIconView closePopUpIco;

	/**
	 * table of all currencies
	 */
	private TableView<Currency> emCurrencies;

	/**
	 * initializes the controller with data
	 */
	@FXML
	public void initialize() {

	}

	/**
	 * responsible for closing the window
	 * @param event
	 */
	@FXML
	public void closePopUp(MouseEvent event) {
		Stage stage = (Stage) closePopUpIco.getScene().getWindow();
		BlurUtils.unblur();
		stage.close();
	}

	/**
	 * remove currency from the market
	 * @param event
	 */
	@FXML
	public void removeCurrency(ActionEvent event) {
		synchronized(PseudoDB.currenciesLock) {
			Currency cSelected = PseudoDB.getCurrencyByName(emRemoveComboBox.getSelectionModel().getSelectedItem());
			exchangeMarket.getQuotedCurrencies()
					.remove(cSelected);
			//take it from investors, refund money
			//change quantity of currency
			//wywal z orderów
			
			exchangeMarket.removeCurrency(cSelected);
			
			
			refresh();
			emRemoveComboBox.getSelectionModel().clearSelection();
			BlurUtils.unblur();
			Stage stage = (Stage) emRemoveButton.getScene().getWindow();
			stage.close();
		}	
	}

	/**
	 * refreshes the chart
	 */
	private void refresh() {
		emCurrencies.setItems(null);
		emCurrencies.setItems(getCurrencies());
		emRemoveComboBox.getItems().setAll(
				exchangeMarket.getQuotedCurrencies().stream().filter(c->!c.getName().equals("USD")).map(c -> c.getName()).collect(Collectors.toList()));
		emAddComboBox.getItems()
				.setAll(PseudoDB.getCurrencies().stream().filter(c -> !exchangeMarket.getQuotedCurrencies().contains(c))
						.map(c -> c.getName()).collect(Collectors.toList()));

	}

	/**
	 * adds currency to the market
	 * @param event
	 */
	@FXML
	public void addCurrency(ActionEvent event) {
		Currency curr = PseudoDB.getCurrencyByName(emAddComboBox.getSelectionModel().getSelectedItem());
		exchangeMarket.addCurrency(curr);
		refresh();
		emAddComboBox.getSelectionModel().clearSelection();
		BlurUtils.unblur();
		Stage stage = (Stage) emAddButton.getScene().getWindow();
		stage.close();
	}

	/**
	 * gets a list of currencies on a given exchange market
	 * @return list of currencies
	 */
	private ObservableList<Currency> getCurrencies() {
		return FXCollections.observableArrayList(exchangeMarket.getQuotedCurrencies().stream().filter(c->!c.getName().equals("USD")).collect(Collectors.toList()));
	}

	/**
	 * initializes the pop up with data
	 * @param emMarket exchange market
	 * @param emTable table of currencies
	 */
	public void initData(ExchangeMarket emMarket, TableView<Currency> emTable) {
		this.exchangeMarket = emMarket;
		emName.setText(emMarket.getName());
		emCountry.setText(emMarket.getCountry());
		emCity.setText(emMarket.getCity());
		emAddress.setText(emMarket.getAddress());
		emProfitMargin.setText(emMarket.getProfitMargin().toString()+"%");
		this.emCurrencies = emTable;

		emAddButton.setDisable(true);
		emRemoveButton.setDisable(true);
		emRemoveComboBox.getItems().setAll(
				exchangeMarket.getQuotedCurrencies().stream().filter(c->!c.getName().equals("USD")).map(c -> c.getName()).collect(Collectors.toList()));
		emAddComboBox.getItems()
				.setAll(PseudoDB.getCurrencies().stream().filter(c -> !c.isQuoted())
						.map(c -> c.getName()).collect(Collectors.toList()));

		emAddComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				emAddButton.setDisable(false);
			}

		});
		emRemoveComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				emRemoveButton.setDisable(false);
			}

		});
	}

}
