package stockmarket.controller.outline;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import stockmarket.model.Company;
import stockmarket.model.Gambler;
import stockmarket.model.Share;
import stockmarket.model.StockExchange;
import stockmarket.utils.BlurUtils;
import stockmarket.utils.NumberUtils;
import stockmarket.utils.PseudoDB;
/**
 * Class responsible for displaying the information about selected company
 * @author Damian Horna
 *
 */
public class ShowCompanyController {
	/**
	 * x offset of a window pop up
	 */
	private static double xOffset = 0;
	/**
	 * y offset of a window pop up
	 */
	private static double yOffset = 0;

	/**
	 * number of shares of company
	 */
	@FXML
	private Label coNumOfShares;

	/**
	 * quantity of shares to buy
	 */
	@FXML
	private JFXTextField quantity;

	/**
	 * show ratings button
	 */
	@FXML
	private JFXButton coShowButton;

	/**
	 * price of shares the company is buying
	 */
	@FXML
	private JFXTextField price;

	/**
	 * used for closing the window
	 */
	@FXML
	private MaterialDesignIconView closePopUpIco;

	/**
	 * name of the company
	 */
	@FXML
	private Label coName;
	
	/**
	 * error label for buying own shares
	 */
	@FXML
	private Label errLabel;

	/**
	 * economic sector of a company
	 */
	@FXML
	private Label coSector;

	/**
	 * icon that deletes the company
	 */
	@FXML
	private MaterialDesignIconView deleteIcon;

	/**
	 * button responsible for triggering buying own shares
	 */
	@FXML
	private JFXButton coBuyButton;

	/**
	 * stack pane for displaying error messages
	 */
	@FXML
	private StackPane stackPane;

	/**
	 * current rate of company
	 */
	@FXML
	private Label coRate;

	/**
	 * company handler
	 */
	private Company company;
	/**
	 * table of all companies on a given stock exchange
	 */
	private TableView<Company> coTable;
	/**
	 * stock exchange on which company is quoted
	 */
	private StockExchange se;

	/**
	 * closes the application
	 * @param event mouse event that triggers closing 
	 */
	@FXML
	void closePopUp(MouseEvent event) {
		Stage stage = (Stage) closePopUpIco.getScene().getWindow();
		BlurUtils.unblur();
		stage.close();
	}

	/**
	 * deletes a company
	 * @param event mouse event that triggers deleting a company
	 */
	@FXML
	void deleteCompany(MouseEvent event) {
		synchronized(PseudoDB.sharesLock) {
			Stage stage = (Stage) deleteIcon.getScene().getWindow();
			BlurUtils.unblur();
			// delete gamblers' assets
			for (Gambler g : company.getShareholders()) {
				g.setPossessions(g.getPossessions().stream().filter(a -> !a.getAssetType().getName().equals(company.getName()))
						.collect(Collectors.toList()));
			}
			// delete share
			PseudoDB.getShares().remove(PseudoDB.getShares().stream()
					.filter(s -> s.getName().equals(company.getName())).collect(Collectors.toList()).get(0));
			se.getCompanyList().remove(company);
			PseudoDB.setOrders(PseudoDB.getOrders().stream().filter(o->o.getAsset().getName()!=company.getName()).collect(Collectors.toList()));
			PseudoDB.getCompanies().remove(company);
			coTable.setItems(null);
			coTable.setItems(getCompanies(se));
			stage.close();
		}
		
	}

	/**
	 * gets all companies from given stock exchange
	 * @param stockExchange stock exchange
	 * @return returns an observable list of companies
	 */
	private ObservableList<Company> getCompanies(StockExchange stockExchange) {
		return FXCollections.observableArrayList(se.getCompanyList());
	}

	/**
	 * handles buying own shares
	 * @param event action event that triggers buying shares of a company
	 */
	@FXML
	void buyShares(ActionEvent event) {
		String quan = quantity.getText();
		String pric = price.getText();
		
		BigDecimal bdQuan = new BigDecimal(1);
		BigDecimal bdPrice = new BigDecimal(1);

		
		try {
			bdQuan = new BigDecimal(quan);
			bdPrice = new BigDecimal(pric);
	
		}catch(NumberFormatException e) {
			errLabel.setText("Provided quantity and price is not in a valid format!");
			errLabel.setTextFill(Color.web("#ce021d"));
			return;
		}
		
		
		if(!NumberUtils.isNumeric(quan) || !NumberUtils.isNumeric(pric)) {
			errLabel.setText("Provided quantity and price is not in a valid format!");
			errLabel.setTextFill(Color.web("#ce021d"));
		}else if(bdQuan.compareTo(company.getNumOfShares())>0) {
			errLabel.setText("Quantity field is not valid!");
			errLabel.setTextFill(Color.web("#ce021d"));
		}
		else{
			synchronized(PseudoDB.sharesLock) {
				company.buyShares(bdQuan, bdPrice);
				coNumOfShares.setText(company.getNumOfShares().toString());
				errLabel.setText("Remember that you may not buy more shares than available");
				errLabel.setTextFill(Color.web("#07c117"));
			}	
		}
	}

	/**
	 * shows ratings of a specific company
	 * @param event action event that triggers showing ratings
	 */
	@FXML
	void showRatings(ActionEvent event) {
		try {
			System.out.println("*** in show ratings, share: "+PseudoDB.getShares().stream().filter(e->e.getName().equals(company.getName())).collect(Collectors.toList()).get(0));
			showInfoAbout(PseudoDB.getShares().stream().filter(e->e.getName().equals(company.getName())).collect(Collectors.toList()).get(0));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * initializes pop up with data
	 * @param company company to show info about
	 * @param companiesTable companies at the exchange of given company
	 * @param stockExchange stock exchange of given company
	 */
	public void initData(Company company, TableView<Company> companiesTable, StockExchange stockExchange) {
		this.company = company;
		this.coTable = companiesTable;
		this.se = stockExchange;
		coName.setText(company.getName());
		coNumOfShares.setText(company.getNumOfShares().toString());
		coSector.setText(company.getEconomicSector());
		coRate.setText(PseudoDB.getShares().stream().filter(c -> c.getName().equals(company.getName()))
				.collect(Collectors.toList()).get(0).getCurrentRate().toString());
	}

	/**
	 * shows ratings of given share
	 * @param share
	 * @throws IOException
	 */
	private void showInfoAbout(Share share) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/outline/ShowCompanyRatings.fxml"));
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
		ShowCompanyRatingsController controller = loader.<ShowCompanyRatingsController>getController();
		controller.initData(share,"seTab");
		stage.show();
	}

}
