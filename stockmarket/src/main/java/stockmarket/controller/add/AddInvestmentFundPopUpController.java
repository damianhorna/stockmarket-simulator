package stockmarket.controller.add;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXButton.ButtonType;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import stockmarket.model.InvestmentFund;
import stockmarket.utils.BlurUtils;
import stockmarket.utils.PseudoDB;
import stockmarket.utils.NumberUtils;
/**
 * The controller of pop up responsible for adding new investment funds to the simulation.
 * @author Damian Horna
 *
 */
public class AddInvestmentFundPopUpController {
	/**
	 * name if CEO of investment fund
	 */
	@FXML
	private JFXTextField nameOfCEO;

	/**
	 * stack pane for displaying error message
	 */
	@FXML
	private StackPane stackPane;

	/**
	 * name of investment fund
	 */
	@FXML
	private JFXTextField name;

	/**
	 * surname of CEO of investment fund
	 */
	@FXML
	private JFXTextField surnameOfCEO;

	/**
	 * budget of investment fund
	 */
	@FXML
	private JFXTextField budget;

	/**
	 * icon for closing the application
	 */
	@FXML
	private MaterialDesignIconView closePopUpIco;

	/**
	 * button for adding new investment fund to the simulation
	 */
	@FXML
	private JFXButton addIco;

	/**
	 * used to close the window
	 * @param event mouse event that launches closing
	 */
	@FXML
	public void closePopUp(MouseEvent event) {
		Stage stage = (Stage) closePopUpIco.getScene().getWindow();
		BlurUtils.unblur();
		stage.close();
	}

	/**
	 * adds investment fund to the simulation  and checks the data given by the user for validity
	 */
	@FXML
	public void addInvestmentFund() {
		String iName = name.getText();
		String iNameOfCEO = nameOfCEO.getText();
		String iSurnameOfCEO = surnameOfCEO.getText();
		String iBudget = budget.getText();
		if (iName.isEmpty()) {
			showErrorDialog("Provide valid name");
		} else if (iNameOfCEO.isEmpty()) {
			showErrorDialog("Provide valid name of CEO");
		} else if (iSurnameOfCEO.isEmpty()) {
			showErrorDialog("Provide valid surname of CEO");
		} else if (!NumberUtils.isNumeric(iBudget)) {
			showErrorDialog("Given budget is not valid");
		} else {
			InvestmentFund investmentFund = new InvestmentFund(iName, iNameOfCEO, iSurnameOfCEO, iBudget);
			PseudoDB.addNewInwestmentFund(investmentFund);
			PseudoDB.showAllInvestmentFunds();
			Stage stage = (Stage) closePopUpIco.getScene().getWindow();
			BlurUtils.unblur();
			stage.close();
		}

	}

	/**
	 * shows error message when incorrect input
	 * @param msg message
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
