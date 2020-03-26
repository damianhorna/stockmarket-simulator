package stockmarket.controller.add;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton.ButtonType;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import stockmarket.model.Investor;
import stockmarket.utils.BlurUtils;
import stockmarket.utils.PseudoDB;
import stockmarket.utils.NumberUtils;
/**
 * The controller of pop up responsible for adding new investors to the simulation.
 * @author Damian Horna
 *
 */
public class AddInvestorPopUpController {
	
	
	
	/**
	 * surname of investor to be added
	 */
	@FXML
	private JFXTextField surname;

	/**
	 * stack pane for displaying error messages
	 */
	@FXML
	private StackPane stackPane;

	/**
	 * name of investor to be added
	 */
	@FXML
	private JFXTextField name;

	/**
	 * PESEL of investor to be added
	 */
	@FXML
	private JFXTextField pesel;

	/**
	 * budget of investor to be added
	 */
	@FXML
	private JFXTextField budget;

	/**
	 * used for closing the application
	 */
	@FXML
	private MaterialDesignIconView closePopUpIco;

	/**
	 * used for adding the investor
	 */
	@FXML
	private JFXButton addIco;

	/**
	 * closes the application
	 * @param event mouse event that closes the application
	 */
	@FXML
	public void closePopUp(MouseEvent event) {
		Stage stage = (Stage) closePopUpIco.getScene().getWindow();
		BlurUtils.unblur();
		stage.close();
	}

	/**
	 * used for adding new investor and checking the data given by the user for validity
	 */
	@FXML
	public void addInvestor() {
		String iName = name.getText();
		String iSurname = surname.getText();
		String iPesel = pesel.getText();
		String iBudget = budget.getText();
		if (iName.isEmpty()) {
			showErrorDialog("Provide valid name");
		} else if (iSurname.isEmpty()) {
			showErrorDialog("Provide valid surname");
		} else if (!NumberUtils.isPESEL(iPesel)) {
			showErrorDialog("Given PESEL is not valid");
		} else if (!NumberUtils.isNumeric(iBudget)) {
			showErrorDialog("Given budget is not valid");
		} else {
			Investor investor = new Investor(iName, iSurname, iPesel, iBudget);
			PseudoDB.addNewInwestor(investor);
			PseudoDB.showAllInvestors();
			Stage stage = (Stage) closePopUpIco.getScene().getWindow();
			BlurUtils.unblur();
			stage.close();
		}

	}

	/**
	 * for showing the error messages on the screen
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
