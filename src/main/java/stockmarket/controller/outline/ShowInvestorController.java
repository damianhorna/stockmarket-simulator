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
import stockmarket.model.Investor;
import stockmarket.utils.BlurUtils;
import stockmarket.utils.PseudoDB;

/**
 * controller responsible for pop up that displays information about investor
 * @author Damian Horna
 *
 */
public class ShowInvestorController {
	/**
	 * the name of investor
	 */
	@FXML
	private Label investorName;
	
	/**
	 * the pesel of investor
	 */
	@FXML
	private Label investorPesel;
	
	/**
	 * stack pane for displaying error messages
	 */
	@FXML
	private StackPane stackPane;
	
	/**
	 * budget of a specific investor
	 */
	@FXML
	private Label investorBudget;
	
	/**
	 * icon for deleting an investor
	 */
	@FXML
	private MaterialDesignIconView deleteIcon;
	
	/**
	 * reference to the investor
	 */
	private Investor investor;
	
	/**
	 * icon for closing the pop up
	 */
	@FXML
	private MaterialDesignIconView closePopUpIco;
	
	/**
	 * table of all investors
	 */
	private TableView<Investor> investors;


	/**
	 * initializes the pop up with data
	 */
	@FXML
	public void initialize() {
	}

	/**
	 * initializes the pop up with data before it is even displayed
	 * @param theInvestor investor
	 * @param theInvestors table of investors
	 */
	public void initData(Investor theInvestor, TableView<Investor> theInvestors) {
		this.investor = theInvestor;
		investorName.setText(investor.getName()+ " " + investor.getSurname());
		investorPesel.setText(investor.getPESEL());
		investorBudget.setText("$"+investor.getBudget().toString());
		this.investors = theInvestors;
	}
	
	/**
	 * responsible for closing
	 * @param event
	 */
	@FXML
	public void closePopUp(MouseEvent event) {
		Stage stage = (Stage) closePopUpIco.getScene().getWindow();
		BlurUtils.unblur();
		stage.close();
	}
	
	/**
	 * deletes the investor from the simulation
	 * @param event
	 */
	@FXML
	public void deleteInvestor(MouseEvent event) {
		Stage stage = (Stage) deleteIcon.getScene().getWindow();
		BlurUtils.unblur();
		PseudoDB.getInvestors().remove(investor);
		investors.setItems(null);
		investors.setItems(getInvestors());
		stage.close();
	}

	/**
	 * gets all investors in the simulation
	 * @return returns observable list of investors
	 */
	private ObservableList<Investor> getInvestors() {
		return FXCollections.observableArrayList(PseudoDB.getInvestors());
	}
	
}
