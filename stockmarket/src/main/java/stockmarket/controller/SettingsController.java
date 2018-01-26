package stockmarket.controller;

import java.util.Locale;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXToggleButton;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import stockmarket.utils.DialogUtils;
import stockmarket.utils.PseudoDB;

/**
 * The controller of settings
 * @author Damian Horna
 *
 */
public class SettingsController {
	
	/**
	 *  icon used for closing the window
	 */
	@FXML
	private MaterialDesignIconView closeIcon;

	/**
	 * enables auto adding investors and investment funds to the simulation
	 */
	@FXML
	private JFXToggleButton autoAddingButton;

	/**
	 * language selection
	 */
	@FXML
	private JFXComboBox<String> langComboBox;

	/**
	 * initializes all the fields with data - languages and saves
	 */
	@FXML
	private void initialize() {
		ObservableList<String> languages = FXCollections.observableArrayList("en", "pl");
		langComboBox.getItems().addAll(languages);
		langComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				Scene scene = langComboBox.getScene();
				try {
					scene.setRoot(FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"),
							ResourceBundle.getBundle("bundles.messages", new Locale(newValue))));
				} catch (Exception e) {
					DialogUtils.errorDialog(e.getMessage());
				}
				Locale.setDefault(new Locale(newValue));
			}
		});
		if (PseudoDB.isAutoAdding()) {
			autoAddingButton.setSelected(true);
		}
	}

	/**
	 * method responsible for enabling auto adding investors
	 */
	@FXML
	public void enableAutoAdding() {
		if(!autoAddingButton.isSelected()) {
			PseudoDB.setAutoAdding(false);
		}else PseudoDB.setAutoAdding(true);
	}

	/** used for close the window
	 * @param event mouse event that activates closing the window
	 */
	@FXML
	public void closeApp(MouseEvent event) {
		Stage stage = (Stage) closeIcon.getScene().getWindow();
		stage.close();
		Platform.exit();
		System.exit(0);
	}
}
