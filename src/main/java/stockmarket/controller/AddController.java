package stockmarket.controller;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import stockmarket.utils.PopUpUtils;

/** Handles adding new objects to the simulation.
 * @author Damian Horna, 132240
 *
 */
public class AddController {
	
	/**
	 * path to pop up used for adding stock exchange index to the simulation
	 */
	private static final String ADD_STOCK_EXCHANGE_INDEX_POP_UP_FXML = "/view/pop-ups/AddStockExchangeIndexPopUp.fxml";
	/**
	 * path to pop up used for adding stock exchange to the simulation
	 */
	private static final String ADD_STOCK_EXCHANGE_POP_UP_FXML = "/view/pop-ups/AddStockExchangePopUp.fxml";
	/**
	 * path to pop up used for adding raw material to the simulation
	 */
	private static final String ADD_RAW_MATERIAL_POP_UP_FXML = "/view/pop-ups/AddRawMaterialPopUp.fxml";
	/**
	 * path to pop up used for adding raw materials market to the simulation
	 */
	private static final String ADD_RAW_MATERIALS_MARKET_POP_UP_FXML = "/view/pop-ups/AddRawMaterialsMarketPopUp.fxml";
	/**
	 * path to pop up used for adding investment fund to the simulation
	 */
	private static final String ADD_INVESTMENT_FUND_POP_UP_FXML = "/view/pop-ups/AddInvestmentFundPopUp.fxml";
	/**
	 * path to pop up used for adding exchange market to the simulation
	 */
	private static final String ADD_EXCHANGE_MARKET_POP_UP_FXML = "/view/pop-ups/AddExchangeMarketPopUp.fxml";
	/**
	 * path to pop up used for adding currency to the simulation
	 */
	private static final String ADD_CURRENCY_POP_UP_FXML = "/view/pop-ups/AddCurrencyPopUp.fxml";
	/**
	 * path to pop up used for adding company to the simulation
	 */
	private static final String ADD_COMPANY_POP_UP_FXML = "/view/pop-ups/AddCompanyPopUp.fxml";
	/**
	 * path to pop up used for adding investor to the simulation
	 */
	private static final String ADD_INVESTOR_POP_UP_FXML = "/view/pop-ups/AddInvestorPopUp.fxml";
	
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
		Platform.exit();
		System.exit(0);
	}
	
	
	/** used for adding investor
	 * @param event action event that activates adding investor
	 */
	@FXML
	private void addInvestor(ActionEvent event) {
		PopUpUtils.showPopUp(closeIcon, ADD_INVESTOR_POP_UP_FXML);
	}
	
	/** used for adding stock exchange
	 * @param event action event that activates adding stock exchange
	 */
	@FXML
	private void addStockExchange(ActionEvent event) {
		PopUpUtils.showPopUp(closeIcon, ADD_STOCK_EXCHANGE_POP_UP_FXML);
	}
	
	/** used for adding exchange market
	 * @param event action event that activates adding exchange market
	 */
	@FXML
	private void addExchangeMarket(ActionEvent event) {
		PopUpUtils.showPopUp(closeIcon, ADD_EXCHANGE_MARKET_POP_UP_FXML);
	}
	
	/** used for adding currency
	 * @param event action event that activates adding currency
	 */
	@FXML
	private void addCurrency(ActionEvent event) {
		PopUpUtils.showPopUp(closeIcon, ADD_CURRENCY_POP_UP_FXML);
	}
	
	/** used for adding raw material
	 * @param event action event that activates adding raw material
	 */
	@FXML
	private void addRawMaterial(ActionEvent event) {
		PopUpUtils.showPopUp(closeIcon, ADD_RAW_MATERIAL_POP_UP_FXML);
	}
	
	/** used for adding raw materials market
	 * @param event action event that activates adding raw materials market
	 */
	@FXML
	private void addRawMaterialsMarket(ActionEvent event) {
		PopUpUtils.showPopUp(closeIcon, ADD_RAW_MATERIALS_MARKET_POP_UP_FXML);
	}
	
	/** used for adding investment fund
	 * @param event action event that activates adding investment fund
	 */
	@FXML
	private void addInvestmentFund(ActionEvent event) {
		PopUpUtils.showPopUp(closeIcon, ADD_INVESTMENT_FUND_POP_UP_FXML);
	}
	
	/** used for adding company
	 * @param event action event that activates adding company
	 */
	@FXML
	private void addCompany(ActionEvent event) {
		PopUpUtils.showPopUp(closeIcon, ADD_COMPANY_POP_UP_FXML);
	}
	
	/** used for adding exchange index
	 * @param event action event that activates adding exchange index
	 */
	@FXML
	private void addExchangeIndex(ActionEvent event) {
		PopUpUtils.showPopUp(closeIcon, ADD_STOCK_EXCHANGE_INDEX_POP_UP_FXML);
	}
	
}
