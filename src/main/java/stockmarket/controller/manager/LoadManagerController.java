package stockmarket.controller.manager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import stockmarket.model.Currency;
import stockmarket.simulation.SimulationState;
import stockmarket.utils.BlurUtils;
import stockmarket.utils.DialogUtils;
import stockmarket.utils.PseudoDB;
import stockmarket.utils.RandomDataGenerator;
/**
 * Class responsible for loading the data from a file into the application
 * @author Damian Horna
 *
 */
public class LoadManagerController {

	/**
	 * name of save
	 */
	@FXML
	private JFXComboBox<String> name;

	/**
	 * button to load a specific save
	 */
	@FXML
	private JFXButton load;

	/**
	 * current state of simulation
	 */
	private SimulationState state;

	/**
	 * load button
	 */
	@FXML
	private JFXButton loadButton;

	/**
	 * the file that contains all the saves
	 */
	private static final String SAVES_PATH = "savesFilePath";

	/**
	 * stack pane for displaying error messages
	 */
	@FXML
	private StackPane stackPane;

	/**
	 * used for closing the application
	 */
	@FXML
	private MaterialDesignIconView closeIcon;

	/**
	 * initializes the pop up with data
	 */
	@FXML
	public void initialize() {

		name.setStyle("-fx-alignment: center;");
		loadSaves();

	}

	/**
	 * used for loading saves from file
	 */
	public void loadSaves() {
		String line;
		try {
			FileReader fileReader = new FileReader(SAVES_PATH);

			BufferedReader bufferedReader = new BufferedReader(fileReader);
			PseudoDB.getSaves().clear();
			while ((line = bufferedReader.readLine()) != null) {
				PseudoDB.getSaves().add(line);
			}

			bufferedReader.close();

			name.getItems().addAll(PseudoDB.getSaves());
		} catch (Exception e) {
			DialogUtils.showFancyErrorDialog(stackPane, "ERRROR", "error while loading");
		}
	}

	/**
	 * load specific simulation state, choosed by user
	 */
	@FXML
	public void loadSimulationState() {
		String path = (String) name.getSelectionModel().getSelectedItem();

		if (path.isEmpty()) {
			DialogUtils.showFancyErrorDialog(stackPane, "ERROR", "name may not be empty");
		} else {
			try {
				tryToLoad(path);
			} catch (Exception e) {
				DialogUtils.showFancyErrorDialog(stackPane, "ERROR", "I could not load selected file");
				e.printStackTrace();
			}
		}
	}

	/**
	 * looks for a specific file in the system and if it exists - loads data into simulation
	 * @param path path to a save
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void tryToLoad(String path) throws FileNotFoundException, IOException, ClassNotFoundException {

		FileInputStream inputStream = new FileInputStream(path);
		ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

		if ((state = (SimulationState) objectInputStream.readObject()) == null) {
			DialogUtils.showFancyErrorDialog(stackPane, "ERROR", "Specified file doesn't exist.");
		} else {
			RandomDataGenerator generator = new RandomDataGenerator();
			List<String> temp = new ArrayList<>();
			for (Currency c : state.getCurrencies()) {
				temp.addAll(c.getCountryList());
			}

			generator.setCountriesForCurrencies(
					state.getCountries().stream().filter(c -> !temp.contains(c)).collect(Collectors.toList()));
			generator.getCurrenciesAvailable()
					.removeAll(state.getCurrencies().stream().map(e -> e.getName()).collect(Collectors.toList()));
			generator.getRawMaterialsAvailable()
					.removeAll(state.getRawMaterials().stream().map(r -> r.getName()).collect(Collectors.toList()));

			objectInputStream.close();
			inputStream.close();
			PseudoDB.setCompanies(state.getCompanies());
			PseudoDB.setCountries(state.getCountries());
			PseudoDB.setCurrencies(state.getCurrencies());
			PseudoDB.setExchangeMarkets(state.getExchangeMarkets());
			PseudoDB.setInvestmentFunds(state.getInvestmentFunds());
			PseudoDB.setInvestors(state.getInvestors());
			PseudoDB.setRawMaterials(state.getRawMaterials());
			PseudoDB.setRawMaterialsMarkets(state.getRawMaterialsMarkets());
			PseudoDB.setStockExchangeIndexes(state.getStockExchangeIndexes());
			PseudoDB.setStockExchanges(state.getStockExchanges());
			PseudoDB.setAutoAdding(state.isAutoAdding());
			PseudoDB.setShares(state.getShares());
			PseudoDB.setOrders(state.getOrders());
			
			PseudoDB.getInvestors().forEach(i->{
				Thread t = new Thread(i);
				t.start();
			});
			
			PseudoDB.getCompanies().forEach(c->{
				Thread t = new Thread(c);
				t.start();
			});
			
			System.out.println("After: " + PseudoDB.getInvestors());
			Stage stage = (Stage) closeIcon.getScene().getWindow();
			BlurUtils.unblur();
			stage.close();
		}
	}

	/**
	 * @param event mouse event that triggers closing the application
	 */
	@FXML
	public void closeWindow(MouseEvent event) {
		Stage stage = (Stage) closeIcon.getScene().getWindow();
		BlurUtils.unblur();
		stage.close();
	}
}
