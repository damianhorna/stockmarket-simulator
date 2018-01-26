package stockmarket.controller.outline;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXComboBox;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import stockmarket.model.Company;
import stockmarket.model.Gambler;
import stockmarket.model.StockExchange;
import stockmarket.utils.BlurUtils;
import stockmarket.utils.PseudoDB;

/**
 * controller for stock exchange pop up
 * @author Damian Horna
 *
 */
public class ShowStockExchangeController {

	/**
	 * country of stock exchange
	 */
	@FXML
	private Label emCountry;

	/**
	 * for closing the pop up
	 */
	@FXML
	private MaterialDesignIconView closePopUpIco;

	/**
	 * city of stock exchange
	 */
	@FXML
	private Label emCity;

	/**
	 * currency of stock exchange
	 */
	@FXML
	private Label emCurrency;

	/**
	 * profit margin of stock exchange
	 */
	@FXML
	private Label emProfitMargin;

	/**
	 * address of stock exchange
	 */
	@FXML
	private Label emAddress;

	/**
	 * icon for deleting stock exchange from the simulation
	 */
	@FXML
	private MaterialDesignIconView deleteIcon;

	/**
	 * name of stock exchange
	 */
	@FXML
	private Label emName;

	/**
	 * reference to stock exchange
	 */
	private StockExchange stockExchange;

	/**
	 * table of all companies available on given stock exchange
	 */
	private TableView<Company> table;

	/**
	 * combo box with stock exchange selection
	 */
	private JFXComboBox<String> comboBox;

	/**
	 * for closing the pop up window
	 * @param event
	 */
	@FXML
	void closePopUp(MouseEvent event) {
		Stage stage = (Stage) closePopUpIco.getScene().getWindow();
		BlurUtils.unblur();
		stage.close();
	}

	/**
	 * for deleting stock exchange
	 * @param event
	 */
	@FXML
	void deleteStockExchange(MouseEvent event) {
		synchronized(PseudoDB.sharesLock) {			
			comboBox.getItems().setAll(new ArrayList<>());
			for (Company company : stockExchange.getCompanyList()) {
				for (Gambler g : company.getShareholders()) {
					g.setPossessions(g.getPossessions().stream().filter(a -> !a.getAssetType().getName().equals(company.getName()))
							.collect(Collectors.toList()));
				}
				// delete share
				PseudoDB.getShares().remove(PseudoDB.getShares().stream()
						.filter(s -> s.getName().equals(company.getName())).collect(Collectors.toList()).get(0));
				PseudoDB.setOrders(PseudoDB.getOrders().stream().filter(o->o.getAsset().getName()!=company.getName()).collect(Collectors.toList()));
				PseudoDB.getCompanies().remove(company);
			}
			
			PseudoDB.getStockExchanges().remove(stockExchange);
			comboBox.getItems().setAll(PseudoDB.getStockExchanges().stream().map(s->s.getName()).collect(Collectors.toList()));
			refresh();
			BlurUtils.unblur();
			Stage stage = (Stage) deleteIcon.getScene().getWindow();
			stage.close();	
		}
		
	}

	/**
	 * refreshes the chart
	 */
	private void refresh() {
		comboBox.getItems()
				.setAll(PseudoDB.getStockExchanges().stream().map(s -> s.getName()).collect(Collectors.toList()));
		table.setItems(null);
	}

	/**
	 * initializes the pop up with data
	 * @param se
	 * @param table
	 * @param combo
	 */
	public void initData(StockExchange se, TableView<Company> table, JFXComboBox<String> combo) {
		this.stockExchange = se;
		this.table = table;
		this.comboBox = combo;
		emCountry.setText(se.getCountry());
		emCity.setText(se.getCity());
		emCurrency.setText(se.getCurrency().getName());
		emProfitMargin.setText(se.getProfitMargin().toString() + "%");
		emAddress.setText(se.getAddress());
		emName.setText(se.getName());
	}

}
