package stockmarket.controller.add;

import java.util.ArrayList;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton.ButtonType;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import stockmarket.model.Currency;
import stockmarket.utils.BlurUtils;
import stockmarket.utils.PseudoDB;
import stockmarket.utils.RandomDataGenerator;
/**
 * The controller of pop up responsible for adding new currencies to the simulation.
 * @author Damian Horna
 *
 */
public class AddCurrencyPopUpController {
	
	/**
	 * name of currency
	 */
	@FXML
	private JFXTextField name;

	/**
	 * list of countries
	 */
	@FXML
	private MenuButton countries;

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
	 * icon for adding the currency
	 */
	@FXML
	private JFXButton addIco;

	/**
	 * closes the pop up
	 * @param event mouse event that forces closing the pop up
	 */
	@FXML
	public void closePopUp(MouseEvent event) {
		Stage stage = (Stage) closePopUpIco.getScene().getWindow();
		BlurUtils.unblur();
		stage.close();
	}

	
	/**
	 * initializes the combo box with the list of availbale countries 
	 */
	@FXML
	public void initialize() {
		RandomDataGenerator generator = new RandomDataGenerator();
		List<MenuItem> menuItems = new ArrayList<>();
		List<String> allCountries = generator.getCountriesForCurrencies();
		for (String s : allCountries) {
			CheckMenuItem item = new CheckMenuItem(s);
			menuItems.add(item);
		}
		countries.getItems().setAll(menuItems);
	}

	/**
	 * adds currency to pseudo db  and checks the data given by the user for validity
	 */
	@FXML
	public void addCurrency() {
		String cName = name.getText();

		List<String> selected = new ArrayList<>();
		List<MenuItem> selectedOrNot = countries.getItems();
		for (MenuItem i : selectedOrNot) {
			if (CheckMenuItem.class.cast(i).isSelected())
				selected.add(i.getText());
		}

		if (cName.isEmpty()) {
			showErrorDialog("Provide a name");
		} else if (selected.isEmpty()) {
			showErrorDialog("Choose at least one country");
		} else {
			Currency currency = new Currency(cName, selected);
			PseudoDB.addNewCurrency(currency);
			if(PseudoDB.isAutoAdding()) {
				RandomDataGenerator gen = new RandomDataGenerator();
				gen.newInvestors(3);
			}
			PseudoDB.showAllCurrencies();
			Stage stage = (Stage) closePopUpIco.getScene().getWindow();
			BlurUtils.unblur();
			stage.close();
		}

	}

	/**
	 * displays error dialog
	 * @param msg message
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
