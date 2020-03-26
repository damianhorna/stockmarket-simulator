package stockmarket.controller.add;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXButton.ButtonType;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import stockmarket.model.Company;
import stockmarket.utils.BlurUtils;
import stockmarket.utils.PseudoDB;
import stockmarket.utils.RandomDataGenerator;

/**
 * The controller of pop up responsible for adding new companies to the simulation.
 * @author Damian Horna
 *
 */
public class AddCompanyPopUpController {
	
	/**
	 *  name of company
	 */
	@FXML
	private JFXTextField name;
	
	/**
	 * sector of company
	 */
	@FXML
	private JFXComboBox<String> sectorComboBox;
	
	/**
	 * stack pane to display error message
	 */
	@FXML
	private StackPane stackPane;
	
	/**
	 * icon for closing the pop up
	 */
	@FXML
	private MaterialDesignIconView closePopUpIco;

	
	/**
	 * icon for adding the ocmpany
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
	 * initializes the combo box with available sectors
	 */
	@FXML
	public void initialize() {
		ObservableList<String> sectors = FXCollections.observableArrayList(PseudoDB.getSectors());
		sectorComboBox.getItems().addAll(sectors);
	}

	/**
	 * adds company to pseudo database and checks the data given by the user for validity
	 */
	@FXML
	public void addCompany() {
		String cName = name.getText();
		String cSector = (String) sectorComboBox.getValue();

		
		if(cName.isEmpty()) {
			showErrorDialog("Name may not be empty");
		}else if (cSector==null){
			showErrorDialog("Choose a sector");
		}else {
			Company company = new Company(cName,cSector);
			PseudoDB.addNewCompany(company);
			if(PseudoDB.isAutoAdding()) {
				RandomDataGenerator gen = new RandomDataGenerator();
				gen.newInvestors(3);
			}
			
			PseudoDB.shawAllCompanies();
			Stage stage = (Stage) closePopUpIco.getScene().getWindow();
			BlurUtils.unblur();
			stage.close();
		}
	}
	
	/**
	 * for displaying error message
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
		button.setOnAction(e ->dialog.close());
		content.setActions(button);
		dialog.show();
	}

}
