package stockmarket.controller.outline;

import java.util.Arrays;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import stockmarket.model.Share;
import stockmarket.utils.BlurUtils;
import stockmarket.utils.PseudoDB;

/**
 * controller of share pop up
 * @author Damian Horna
 *
 */
public class ShowShareController {


	/**
	 * for closing the window
	 */
	@FXML
	private MaterialDesignIconView closePopUpIco;

	/**
	 * name of the company
	 */
	@FXML
	private Label companyName;
	/**
	 * number of shares 
	 */
	@FXML
	private Label shareNumOfShares;
	/**
	 * current rate of share
	 */
	@FXML
	private Label shareCurrentRate;
	/**
	 * the initial rate of share
	 */
	@FXML
	private Label shareInitialRate;
	/**
	 * maximal rate of share during session
	 */
	@FXML
	private Label shareMaximalRate;
	/**
	 * minimal rate of share during session
	 */
	@FXML
	private Label shareMinimalRate;
	/**
	 * volume
	 */
	@FXML
	private Label shareVolume;
	/**
	 * stack pane for displaying error messages
	 */
	@FXML
	private StackPane stackPane;
	/**
	 * label whether quoted or not
	 */
	@FXML
	private Label shareQuoted;
	/**
	 * for refreshing the chart
	 */
	@FXML
	private JFXButton refreshButton;
	
	/**
	 * period selection
	 */
	@FXML
	private JFXComboBox<String> periodComboBox;

	/**
	 * reference to share
	 */
	private Share share;


	/**
	 * initializes the comobo box with data for period selection
	 */
	@FXML
	public void initialize() {
		periodComboBox.getItems().setAll("1D","1W","1M","1Y");

	}

	/**
	 * initializes the pop up with share data
	 * @param theShare
	 */
	public void initData(Share theShare) {
		this.share = theShare;
		companyName.setText("Company: "+share.getName());
		if (share.isQuoted()) {
			shareQuoted.setText("");
		}
		shareNumOfShares.setText(
				PseudoDB.getCompaniesByNames(Arrays.asList(share.getName())).get(0).getNumOfShares().toString());
		shareCurrentRate.setText(share.getCurrentRate().toString());
		shareInitialRate.setText(share.getInitialRate().toString());
		shareMaximalRate.setText(share.getMaximalRate().toString());
		try {
			shareVolume.setText(share.getRatingRecords().get(share.getRatingRecords().size()-1).getVolume().toString());
		} catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("null pointer caught in show share controller at 61");
			shareVolume.setText("0");
		}
		shareMinimalRate.setText(share.getMinimalRate().toString());
	}

	/**
	 * for closing the window
	 * @param event
	 */
	@FXML
	public void closePopUp(MouseEvent event) {
		Stage stage = (Stage) closePopUpIco.getScene().getWindow();
			BlurUtils.unblur();
		stage.close();
	}
	
	/**
	 * refreshes the chart
	 * @param event
	 */
	@FXML
	public void refreshChart(ActionEvent event) {
		
	}

}
