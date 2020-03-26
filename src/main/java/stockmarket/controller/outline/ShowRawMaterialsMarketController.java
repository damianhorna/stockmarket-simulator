package stockmarket.controller.outline;

import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import stockmarket.model.RawMaterial;
import stockmarket.model.RawMaterialsMarket;
import stockmarket.utils.BlurUtils;
import stockmarket.utils.PseudoDB;

public class ShowRawMaterialsMarketController {

	@FXML
	private Label emAddress;

	@FXML
	private Label emCity;

	@FXML
	private Label emCountry;

	@FXML
	private Label emName;

	@FXML
	private JFXButton emAddButton;

	@FXML
	private JFXButton emRemoveButton;

	@FXML
	private JFXComboBox<String> emAddComboBox;

	@FXML
	private Label emProfitMargin;

	@FXML
	private JFXComboBox<String> emRemoveComboBox;

	private RawMaterialsMarket rawMaterialsMarket;

	@FXML
	private MaterialDesignIconView closePopUpIco;

	private TableView<RawMaterial> rmMaterials;

	@FXML
	public void initialize() {

	}

	@FXML
	public void closePopUp(MouseEvent event) {
		Stage stage = (Stage) closePopUpIco.getScene().getWindow();
		BlurUtils.unblur();
		stage.close();
	}

	@FXML
	public void removeRawMaterial(ActionEvent event) {
		synchronized (PseudoDB.rawMaterialsLock) {
			RawMaterial rSelected = PseudoDB
					.getRawMaterialByName(emRemoveComboBox.getSelectionModel().getSelectedItem());
			rawMaterialsMarket.getQuotedRawMaterials().remove(rSelected);
			// take it from investors, refund money
			// change quantity of currency
			// wywal z orderów

			rawMaterialsMarket.removeRawMaterial(rSelected);

			refresh();
			emRemoveComboBox.getSelectionModel().clearSelection();
			BlurUtils.unblur();
			Stage stage = (Stage) emRemoveButton.getScene().getWindow();
			stage.close();
		}
	}

	private void refresh() {
		rmMaterials.setItems(null);
		rmMaterials.setItems(getRawMaterials());
		emRemoveComboBox.getItems().setAll(
				rawMaterialsMarket.getQuotedRawMaterials().stream().map(c -> c.getName()).collect(Collectors.toList()));
		emAddComboBox.getItems()
				.setAll(PseudoDB.getRawMaterials().stream()
						.filter(c -> !rawMaterialsMarket.getQuotedRawMaterials().contains(c)).map(c -> c.getName())
						.collect(Collectors.toList()));
	}

	@FXML
	public void addRawMaterial(ActionEvent event) {
		RawMaterial material = PseudoDB.getRawMaterialByName(emAddComboBox.getSelectionModel().getSelectedItem());
		rawMaterialsMarket.addMaterial(material);
		refresh();
		emAddComboBox.getSelectionModel().clearSelection();
		BlurUtils.unblur();
		Stage stage = (Stage) emAddButton.getScene().getWindow();
		stage.close();
	}

	private ObservableList<RawMaterial> getRawMaterials() {
		return FXCollections
				.observableArrayList(rawMaterialsMarket.getQuotedRawMaterials().stream().collect(Collectors.toList()));
	}

	public void initData(RawMaterialsMarket emMarket, TableView<RawMaterial> emTable) {
		this.rawMaterialsMarket = emMarket;
		emName.setText(emMarket.getName());
		emCountry.setText(emMarket.getCountry());
		emCity.setText(emMarket.getCity());
		emAddress.setText(emMarket.getAddress());
		emProfitMargin.setText(emMarket.getProfitMargin().toString() + "%");
		this.rmMaterials = emTable;
		emAddButton.setDisable(true);
		emRemoveButton.setDisable(true);
		emRemoveComboBox.getItems().setAll(
				rawMaterialsMarket.getQuotedRawMaterials().stream().map(c -> c.getName()).collect(Collectors.toList()));
		emAddComboBox.getItems().setAll(PseudoDB.getRawMaterials().stream().filter(c -> !c.isQuoted())
				.map(c -> c.getName()).collect(Collectors.toList()));

		emAddComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				emAddButton.setDisable(false);
			}

		});
		emRemoveComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				emRemoveButton.setDisable(false);
			}

		});
	}

}
