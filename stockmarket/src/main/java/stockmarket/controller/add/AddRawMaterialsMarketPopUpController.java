package stockmarket.controller.add;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXButton.ButtonType;
import com.jfoenix.controls.JFXComboBox;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import stockmarket.model.RawMaterialsMarket;
import stockmarket.utils.BlurUtils;
import stockmarket.utils.PseudoDB;
import stockmarket.utils.NumberUtils;
/**
 * The controller of pop up responsible for adding new raw materials markets to the simulation.
 * @author Damian Horna
 *
 */
public class AddRawMaterialsMarketPopUpController {
	/**
	 * name of raw materials market to be added
	 */
	@FXML
	private JFXTextField name;
	
	/**
	 * country of raw materials market to be added
	 */
	@FXML
	private JFXComboBox<String> country;

	/**
	 * city of raw materials market to be added
	 */
	@FXML
	private JFXTextField city;

	/**
	 * address of raw materials market to be added
	 */
	@FXML
	private JFXTextField address;

	/**
	 * profit margin of raw materials market
	 */
	@FXML
	private JFXTextField profitMargin;

	/**
	 * stack pane for displaying error messages
	 */
	@FXML
	private StackPane stackPane;

	/**
	 * by clicking it the application closes
	 */
	@FXML
	private MaterialDesignIconView closePopUpIco;

	/**
	 * fires adding new raw materials market to the simulation
	 */
	@FXML
	private JFXButton addIco;

	/**
	 * closes the application
	 * @param event mouse event that forces the application to close
	 */
	@FXML
	public void closePopUp(MouseEvent event) {
		Stage stage = (Stage) closePopUpIco.getScene().getWindow();
		BlurUtils.unblur();
		stage.close();
	}

	/**
	 * initializes all the fields in the pop up
	 */
	@FXML
	public void initialize() {
		List<String> countries = PseudoDB.getCountries(new Locale("en"));
		ObservableList<String> countriesOL = FXCollections.observableArrayList(countries);
		country.getItems().addAll(countriesOL);
	}

	/**
	 * responsible for adding new raw materials market to the simulation and checking whether given data is correct
	 */
	@FXML
	public void addRawMaterialsMarket() {
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
			RawMaterialsMarket eMarket = new RawMaterialsMarket(eName, eCountry, eCity, eAddress,
					new BigDecimal(eProfitMargin));
			PseudoDB.addNewRawMaterialsMarket(eMarket);
			PseudoDB.showAllRawMaterialsMarkets();
			Stage stage = (Stage) closePopUpIco.getScene().getWindow();
			BlurUtils.unblur();
			stage.close();
		}

	}

	/**
	 * shows the error dialog message
	 * @param msg message to be shown
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
