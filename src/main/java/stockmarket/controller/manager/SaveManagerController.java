package stockmarket.controller.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Locale;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import stockmarket.simulation.SimulationState;
import stockmarket.utils.BlurUtils;
import stockmarket.utils.DialogUtils;
import stockmarket.utils.PseudoDB;
/**
 * Class responsible for saving current state of simulation
 * @author Damian Horna
 *
 */
public class SaveManagerController {
	/**
	 * a button for saving
	 */
	@FXML
	private JFXButton saveButton;

	/**
	 * name of the save
	 */
	@FXML
	private JFXTextField name;

	/**
	 * icon used for closing the application
	 */
	@FXML
	private MaterialDesignIconView closeIcon;

	/**
	 * current state of simulation
	 */
	private SimulationState state;

	/**
	 * path to the file that holds all the saves
	 */
	private static final String SAVES_PATH = "savesFilePath";

	/**
	 * stack pane for displaying error messages
	 */
	@FXML
	private StackPane stackPane;

	/**
	 * used for saving simulation state
	 */
	@FXML
	public void saveSimulationState() {

		if (name.getText().isEmpty()) {
			DialogUtils.showFancyErrorDialog(stackPane, "ERROR", "name may not be empty");
		} else if (name.getText().equals(SAVES_PATH)) {
			DialogUtils.showFancyErrorDialog(stackPane, "ERROR", "specify different file name");
		} else {

			System.out.println("Before: " + PseudoDB.getInvestors());
			state = new SimulationState(PseudoDB.getInvestors(), PseudoDB.getCurrencies(), PseudoDB.getCompanies(),
					PseudoDB.getExchangeMarkets(), PseudoDB.getInvestmentFunds(), PseudoDB.getRawMaterialsMarkets(),
					PseudoDB.getRawMaterials(), PseudoDB.getStockExchanges(), PseudoDB.getStockExchangeIndexes(),
					PseudoDB.getCountries(new Locale("en")), PseudoDB.isAutoAdding(), PseudoDB.getShares(),
					PseudoDB.getOrders());

			try {
				FileOutputStream outputStream = new FileOutputStream(name.getText());
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
				objectOutputStream.writeObject(state);

				objectOutputStream.flush();
				outputStream.close();
				saveSaves(name.getText());
				System.out.println(name.getText());

			} catch (Exception e) {
				System.out.println("error while saving");
				e.printStackTrace();
				DialogUtils.showFancyErrorDialog(stackPane, "ERROR", "error while saving.");
			}

			Stage stage = (Stage) closeIcon.getScene().getWindow();
			BlurUtils.unblur();
			stage.close();
		}

	}

	/**
	 * writes the names of saves to the file
	 * @param save the name of save
	 */
	private void saveSaves(String save) {
		loadSaves();
		try {
			FileWriter fileWriter = new FileWriter(SAVES_PATH);

			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			try {
				bufferedWriter.write(save);
				bufferedWriter.newLine();
				PseudoDB.getSaves().forEach(e -> {
					try {
						if(!e.equals(save)) {
							bufferedWriter.write(e);
							bufferedWriter.newLine();
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});
			} catch (IOException e1) {
				DialogUtils.showFancyErrorDialog(stackPane, "ERROR", "error while saving.");
				e1.printStackTrace();
			}

			bufferedWriter.close();
		} catch (IOException ex) {
			DialogUtils.showFancyErrorDialog(stackPane, "ERROR", "error while saving.");
		}
	}

	/**
	 * loads all the saves
	 */
	private void loadSaves() {
		String line;
		try {
			FileReader fileReader = new FileReader(SAVES_PATH);

			BufferedReader bufferedReader = new BufferedReader(fileReader);
			PseudoDB.getSaves().clear();
			while ((line = bufferedReader.readLine()) != null) {
				PseudoDB.getSaves().add(line);
			}

			bufferedReader.close();
		} catch (Exception e) {
			DialogUtils.showFancyErrorDialog(stackPane, "ERRROR", "error while loading");
		}
	}

	/**
	 * closes the applications
	 * @param event mouse event that triggers closing the window
	 */
	@FXML
	public void closeWindow(MouseEvent event) {
		Stage stage = (Stage) closeIcon.getScene().getWindow();
		BlurUtils.unblur();
		stage.close();
	}

}
