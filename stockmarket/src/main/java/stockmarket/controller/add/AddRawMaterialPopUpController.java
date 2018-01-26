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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import stockmarket.model.Currency;
import stockmarket.model.RawMaterial;
import stockmarket.model.Unit;
import stockmarket.utils.BlurUtils;
import stockmarket.utils.PseudoDB;
import stockmarket.utils.RandomDataGenerator;
/**
 * The controller of pop up responsible for adding new raw materials to the simulation.
 * @author Damian Horna
 *
 */
public class AddRawMaterialPopUpController {

	/**
	 * name of raw material to be added
	 */
	@FXML
	private JFXTextField name;
	/**
	 * unit of raw material to be added
	 */
	@FXML
	private JFXComboBox<String> unit;
	/**
	 * currency of raw material to be added
	 */
	@FXML
	private JFXComboBox<String> currency;

	/**
	 * stack pane for displaying error messages
	 */
	@FXML
	private StackPane stackPane;

	/**
	 * icon responsible for closing the application window
	 */
	@FXML
	private MaterialDesignIconView closePopUpIco;

	/**
	 * responsible for adding new raw material to the simulation
	 */
	@FXML
	private JFXButton addIco;

	/**
	 * closes the application
	 * @param event mouse event forcing the closing of the application
	 */
	@FXML
	public void closePopUp(MouseEvent event) {
		Stage stage = (Stage) closePopUpIco.getScene().getWindow();
		BlurUtils.unblur();
		stage.close();
	}

	/**
	 * initializes all the data within pop up
	 */
	@FXML
	public void initialize() {

		String[] units = Unit.getNames(Unit.class);
		ObservableList<String> unitsOL = FXCollections.observableArrayList(units);
		unit.getItems().addAll(unitsOL);

		List<Currency> currencies = PseudoDB.getCurrencies();
		List<String> currenciesStr = new ArrayList<>();
		currencies.forEach(c -> currenciesStr.add(c.getName()));
		ObservableList<String> currenciesOL = FXCollections.observableArrayList(currenciesStr);
		currency.getItems().addAll(currenciesOL);
	}

	/**
	 * responsible for adding new raw material to the simulation and checking the data given by the user for validity
	 */
	@FXML
	public void addRawMaterial() {
		String rName = name.getText();
		String rUnit = unit.getValue();
		String rCurrency = currency.getValue();

		if (rName.isEmpty()) {
			showErrorDialog("Provide a name");
		} else if (unit.getSelectionModel().isEmpty()) {
			showErrorDialog("Choose unit");
		} else if (currency.getSelectionModel().isEmpty()) {
			showErrorDialog("Choose currency");
		} else {
			Currency c = PseudoDB.getCurrencyByName(rCurrency);
			RawMaterial rawMaterial = new RawMaterial(rName, Unit.valueOf(rUnit), c);
			PseudoDB.addNewRawMaterial(rawMaterial);
			if(PseudoDB.isAutoAdding()) {
				RandomDataGenerator gen = new RandomDataGenerator();
				gen.newInvestors(3);
			}
			PseudoDB.showAllRawMaterials();
			Stage stage = (Stage) closePopUpIco.getScene().getWindow();
			BlurUtils.unblur();
			stage.close();
		}

	}

	/**
	 * shows error message
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
