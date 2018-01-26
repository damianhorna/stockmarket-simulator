package stockmarket.utils;

import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXButton.ButtonType;

import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * dialog utils class
 * @author Damian Horna
 *
 */
public class DialogUtils {

	private static ResourceBundle bundle = FXMLUtils.getResourceBundle();

	/**
	 * shows error dialog
	 * @param error message
	 */
	public static void errorDialog(String error) {
		Alert errorAlert = new Alert(Alert.AlertType.ERROR);
		errorAlert.setTitle(bundle.getString("error.title"));
		errorAlert.setHeaderText(bundle.getString("error.message"));

		errorAlert.getDialogPane().setContentText(error);
		errorAlert.showAndWait();
	}	
	
	/**
	 * shows fancy error dialog 
	 * @param stackPane stack pane to show dialog on
	 * @param type type of dialog
	 * @param msg message
	 */
	public static void showFancyErrorDialog(StackPane stackPane, String type, String msg) {
		JFXDialogLayout content = new JFXDialogLayout();
		content.setHeading(new Text(type));
		content.setBody(new Text(msg));
		JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);
		;

		JFXButton button = new JFXButton("Ok");
		button.setButtonType(ButtonType.RAISED);
		button.setCursor(Cursor.HAND);
		button.setOnAction(e -> dialog.close());
		content.setActions(button);
		dialog.show();
	}
}
