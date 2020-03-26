package stockmarket.controller.outline;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import stockmarket.model.Asset;
import stockmarket.utils.BlurUtils;
import stockmarket.utils.DialogUtils;
import stockmarket.utils.PseudoDB;

/**
 * controller responsible for pop up showing comparison of two assets
 * @author Damian Horna
 *
 */
public class ShowComparisonController {

	/**
	 * icon for closing
	 */
	@FXML
	private MaterialDesignIconView closePopUpIco;

	/**
	 * shows the comparison between two assets
	 */
	@FXML
	private JFXButton showComparisonButton;

	/**
	 * combo box for choosing first asset
	 */
	@FXML
	private JFXComboBox<String> asset1ComboBox;

	/**
	 * combo box for choosing second asset
	 */
	@FXML
	private JFXComboBox<String> asset2ComboBox;
	
	/**
	 * x offset of a pop up window
	 */
	private static double xOffset = 0;
	/**
	 * y offset of a pop up window
	 */
	private static double yOffset = 0;

	/**
	 * initializes the pop up with data, disables a comparison button, adds listeners
	 */
	@FXML
	public void initialize() {
		showComparisonButton.setDisable(true);
		
		List<String> assets = getAllAssetsNames();
		
		asset1ComboBox.getItems().setAll(assets);
		asset2ComboBox.getItems().setAll(assets);
		
		asset1ComboBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> obs, String newValue, String oldValue) {
				if (!(asset2ComboBox.getValue() == null)) {
					showComparisonButton.setDisable(false);
				}
			}
		});

		asset2ComboBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> obs, String newValue, String oldValue) {
				if (!(asset1ComboBox.getValue() == null)) {
					showComparisonButton.setDisable(false);
				}
			}
		});
	}
	
	/**
	 * gets the names of all assets in the simulation
	 * @return returns the names of all assets
	 */
	private List<String> getAllAssetsNames() {
		List<String> assets = new ArrayList<>();
		for(Asset a : PseudoDB.getShares()) {
			if(a.isQuoted()) {
				assets.add(a.getName());
			}
		}
		for(Asset a : PseudoDB.getCurrencies()) {
			if(a.isQuoted()) {
				assets.add(a.getName());
			}
		}
		for(Asset a : PseudoDB.getRawMaterials()) {
			if(a.isQuoted()) {
				assets.add(a.getName());
			}
		}
		return assets;
	}
	
	/**
	 * returns all the assets available in the simulation
	 * @return returns assets
	 */
	private List<Asset> getAllAssets() {
		List<Asset> assets = new ArrayList<>();
		for(Asset a : PseudoDB.getShares()) {
			if(a.isQuoted()) {
				assets.add(a);
			}
		}
		for(Asset a : PseudoDB.getCurrencies()) {
			if(a.isQuoted()) {
				assets.add(a);
			}
		}
		for(Asset a : PseudoDB.getRawMaterials()) {
			if(a.isQuoted()) {
				assets.add(a);
			}
		}
		return assets;
	}

	/**
	 * shows comparison between two assets
	 * @param event
	 */
	@FXML
	public void showComparison(ActionEvent event) {
		String name1 = asset1ComboBox.getValue();
		String name2 = asset2ComboBox.getValue();
		
		Asset asset1 = getAssetByName(name1);
		Asset asset2 = getAssetByName(name2);
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/outline/ComparisonRatingsPopUp.fxml"));
			Stage stage = new Stage(StageStyle.UNDECORATED);
			Pane pane = (Pane) loader.load();

			stage.setScene(new Scene(pane));

			pane.setOnMousePressed(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					xOffset = event.getSceneX();
					yOffset = event.getSceneY();
				}
			});
			pane.setOnMouseDragged(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					stage.setX(event.getScreenX() - xOffset);
					stage.setY(event.getScreenY() - yOffset);
				}
			});
			stage.show();
			ShowComparisonRatingsController controller = loader.<ShowComparisonRatingsController>getController();
			controller.initData(asset1,asset2);
		} catch (Exception e) {
			DialogUtils.errorDialog(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * gets asset by name
	 * @param name name of the asset
	 * @return the asset with a given name
	 */
	private Asset getAssetByName(String name) {
		return getAllAssets().stream().filter(a->a.getName().equals(name)).collect(Collectors.toList()).get(0);
	}

	/**
	 * closes the pop up
	 * @param event
	 */
	@FXML
	public void closePopUp(MouseEvent event) {
		Stage stage = (Stage) closePopUpIco.getScene().getWindow();
		BlurUtils.unblur();
		stage.close();
	}

}
