package stockmarket.controller.outline;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import stockmarket.model.InvestmentFund;
import stockmarket.utils.BlurUtils;
import stockmarket.utils.PseudoDB;

/**
 * controller for displaying investment fund
 * @author DamianHorna
 *
 */
public class ShowInvestmentFundController {
	/**
	 * budget of investment fund
	 */
	@FXML
	private Label fundBudget;

	/**
	 * icon for closing the pop up
	 */
	@FXML
	private MaterialDesignIconView closePopUpIco;

	/**
	 * name of CEO of the fund
	 */
	@FXML
	private Label fundCEOName;

	/**
	 * surname of ceo of the fund
	 */
	@FXML
	private Label fundCEOSurname;

	/**
	 * name of investment fund
	 */
	@FXML
	private Label fundName;

	/**
	 * icon for deleting investment fund
	 */
	@FXML
	private MaterialDesignIconView deleteIcon;

	/**
	 * stack pane for displaying error messages
	 */
	@FXML
	private StackPane stackPane;

	/**
	 * number of investment fund shares
	 */
	@FXML
	private Label fundNumOfFundShares;

	/**
	 * value of one fund share
	 */
	@FXML
	private Label fundSharesValue;

	/**
	 * reference to investment fund that is currently being displayed to the user
	 */
	private InvestmentFund investmentFund;

	/**
	 * table of all investment funds
	 */
	private TableView<InvestmentFund> investmentFunds;

	/**
	 * initializes the controller with data
	 */
	@FXML
	public void initialize() {
	}

	/**
	 * initializes the controller with data before it is displayed
	 * @param theInvestmentFund single one investment fund
	 * @param theInvestmentFunds table of all investment funds
	 */
	public void initData(InvestmentFund theInvestmentFund, TableView<InvestmentFund> theInvestmentFunds) {
		this.investmentFund=theInvestmentFund;
		fundName.setText(theInvestmentFund.getName());
		fundCEOName.setText(theInvestmentFund.getNameOfCEO());
		fundCEOSurname.setText(theInvestmentFund.getSurnameOfCEO());
		fundBudget.setText("$"+theInvestmentFund.getBudget().toString());
		fundSharesValue.setText("$"+theInvestmentFund.getInvestmentFundSharesValue().toString());
		Integer i = (Integer)theInvestmentFund.getNumOfInvestmentFundShares();
		fundNumOfFundShares.setText(i.toString());
		this.investmentFunds=theInvestmentFunds;
	}

	/**
	 * for closing the pop up
	 * @param event
	 */
	@FXML
	public void closePopUp(MouseEvent event) {
		Stage stage = (Stage) closePopUpIco.getScene().getWindow();
		BlurUtils.unblur();
		stage.close();
	}

	/**
	 * deletes the investment fund from the simulation
	 * @param event
	 */
	@FXML
	public void deleteFund(MouseEvent event) {
		Stage stage = (Stage) deleteIcon.getScene().getWindow();
		BlurUtils.unblur();
		PseudoDB.getInvestmentFunds().remove(investmentFund);
		investmentFunds.setItems(null);
		investmentFunds.setItems(getInvestmentFunds());
		stage.close();
	}

	/**
	 * gets all investment funds
	 * @return returns observable list of investment funds
	 */
	private ObservableList<InvestmentFund> getInvestmentFunds() {
		return FXCollections.observableArrayList(PseudoDB.getInvestmentFunds());
	}
}
