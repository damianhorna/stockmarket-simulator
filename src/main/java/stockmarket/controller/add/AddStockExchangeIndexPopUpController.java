package stockmarket.controller.add;

import java.util.ArrayList;
import java.util.List;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton.ButtonType;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import stockmarket.model.Company;
import stockmarket.model.StockExchange;
import stockmarket.model.StockExchangeIndex;
import stockmarket.utils.BlurUtils;
import stockmarket.utils.PseudoDB;
/**
 * The controller of pop up responsible for adding new stock exchange indexes to the simulation.
 * @author Damian Horna
 *
 */
public class AddStockExchangeIndexPopUpController {
	private static final String NONE = "CUSTOMIZED";

	
	/**
	 * name of stock exchange index to be added
	 */
	@FXML
	private JFXTextField name;

	/**
	 * combo box for selecting the stock exchange on which the index is located
	 */
	@FXML
	private JFXComboBox<String> stockExchangeComboBox;

	/**
	 * the condition under which the companies are connected within the index
	 */
	@FXML
	private JFXComboBox<String> conditionComboBox;

	/**
	 * selection of the companies
	 */
	@FXML
	private MenuButton companiesMenuButton;

	/**
	 * stack pane for displaying error messagess
	 */
	@FXML
	private StackPane stackPane;

	/**
	 * enables closing the application
	 */
	@FXML
	private MaterialDesignIconView closePopUpIco;

	/**
	 * enables adding new indexes to the simulation
	 */
	@FXML
	private JFXButton addIco;

	/**
	 * closes the application
	 * @param event event that triggers closing the application
	 */
	@FXML
	public void closePopUp(MouseEvent event) {
		Stage stage = (Stage) closePopUpIco.getScene().getWindow();
		BlurUtils.unblur();
		stage.close();
	}

	/**
	 * initializes all the data in the controller
	 */
	@FXML
	public void initialize() {
		// stock exchange combo box
		List<StockExchange> stockExchanges = PseudoDB.getStockExchanges();
		List<String> stockExchangesStr = new ArrayList<>();
		stockExchanges.forEach(s -> stockExchangesStr.add(s.getName()));
		ObservableList<String> stockExchangesOL = FXCollections.observableArrayList(stockExchangesStr);
		stockExchangeComboBox.getItems().addAll(stockExchangesOL);

		// condition combo box
		List<String> conditions = new ArrayList<>(PseudoDB.getConditions());
		ObservableList<String> currenciesOL = FXCollections.observableArrayList(conditions);
		conditionComboBox.getItems().addAll(currenciesOL);

		// selected companies menu item
		List<MenuItem> menuItems = new ArrayList<>();
		List<Company> companies = PseudoDB.getCompanies();
		for (Company c : companies) {
			CheckMenuItem item = new CheckMenuItem(c.getName());
			menuItems.add(item);
		}
		companiesMenuButton.getItems().setAll(menuItems);

		conditionComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if (newValue.equals("None")) {
					companiesMenuButton.setDisable(false);
				} else {
					companiesMenuButton.setDisable(true);
				}
			}
		});
	}

	/**
	 * adds new stock exchange index to the simulation and checks the data given by the user for validity
	 */
	@FXML
	public void addStockExchangeIndex() {
		String iName = name.getText();
		String iStockExchange = stockExchangeComboBox.getValue();
		String iCondition = conditionComboBox.getValue();
		List<String> selected = new ArrayList<>();

		// choose selected menu items
		List<MenuItem> selectedOrNot = companiesMenuButton.getItems();
		for (MenuItem i : selectedOrNot) {
			if (CheckMenuItem.class.cast(i).isSelected())
				selected.add(i.getText());
		}

		if (iName.isEmpty()) {
			showErrorDialog("Provide a name");
		} else if (stockExchangeComboBox.getSelectionModel().isEmpty()) {
			showErrorDialog("Choose stock exchange");
		} else if (conditionComboBox.getSelectionModel().isEmpty()) {
			showErrorDialog("Invalid condition");
		} else if (iCondition.equals("None") && selected.isEmpty()) {
			showErrorDialog("Choose at least one company");
		} else {
			if (conditionComboBox.getValue().equals("None")) {
				List<Company> selectedCompanies = PseudoDB.getCompaniesByNames(selected);

				StockExchangeIndex sei = new StockExchangeIndex(iName, PseudoDB.getStockExchangeByName(iStockExchange),
						selectedCompanies, NONE);
				PseudoDB.addNewStockExchangeIndex(sei);
				PseudoDB.showAllStockExchangeIndexes();
				Stage stage = (Stage) closePopUpIco.getScene().getWindow();
				BlurUtils.unblur();
				stage.close();
			} else {
				PseudoDB.getStockExchangeByName(iStockExchange);
				List<Company> selectedCompanies = new ArrayList<>();
				StockExchangeIndex sei = new StockExchangeIndex(iName, PseudoDB.getStockExchangeByName(iStockExchange),
						selectedCompanies, iCondition);
				PseudoDB.addNewStockExchangeIndex(sei);
				PseudoDB.showAllStockExchangeIndexes();
				Stage stage = (Stage) closePopUpIco.getScene().getWindow();
				BlurUtils.unblur();
				stage.close();
			}

		}

	}

	/**
	 * shows error dialog message
	 * @param msg message to be shown
	 */
	private void showErrorDialog(String msg) {
		JFXDialogLayout content = new JFXDialogLayout();
		content.setHeading(new Text("ERROR"));
		content.setBody(new Text(msg));
		JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);

		JFXButton button = new JFXButton("I understand	");
		button.setButtonType(ButtonType.RAISED);
		button.setCursor(Cursor.HAND);
		button.setOnAction(e -> dialog.close());
		content.setActions(button);
		dialog.show();
	}
}
