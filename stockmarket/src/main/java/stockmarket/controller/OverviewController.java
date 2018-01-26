package stockmarket.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTabPane;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import stockmarket.controller.outline.ShowCompanyController;
import stockmarket.controller.outline.ShowCompanyRatingsController;
import stockmarket.controller.outline.ShowCurrencyController;
import stockmarket.controller.outline.ShowExchangeMarketController;
import stockmarket.controller.outline.ShowInvestmentFundController;
import stockmarket.controller.outline.ShowInvestorController;
import stockmarket.controller.outline.ShowRawMaterialController;
import stockmarket.controller.outline.ShowRawMaterialsMarketController;
import stockmarket.controller.outline.ShowShareController;
import stockmarket.controller.outline.ShowStockExchangeController;
import stockmarket.controller.outline.ShowStockExchangeIndexController;
import stockmarket.model.Asset;
import stockmarket.model.Company;
import stockmarket.model.Currency;
import stockmarket.model.ExchangeMarket;
import stockmarket.model.InvestmentFund;
import stockmarket.model.Investor;
import stockmarket.model.RawMaterial;
import stockmarket.model.RawMaterialsMarket;
import stockmarket.model.Share;
import stockmarket.model.StockExchange;
import stockmarket.model.StockExchangeIndex;
import stockmarket.utils.BlurUtils;
import stockmarket.utils.DialogUtils;
import stockmarket.utils.PseudoDB;

/**
 * The controller of overview screen
 * @author Damian Horna
 *
 */
public class OverviewController {

	/**
	 * used for closing the window
	 */
	@FXML
	private MaterialDesignIconView closeIcon;

	/**
	 * holds everything together
	 */
	@FXML
	private AnchorPane overviewAnchorPane;

	/**
	 * contains all the tabs
	 */
	@FXML
	private JFXTabPane tabPane;

	/**
	 * contains basic information about investors
	 */
	@FXML
	private Tab investorsTab;
	/**
	 * contains basic information about stock exchanges
	 */
	@FXML
	private Tab stockExchangeTab;
	/**
	 * contains basic information about raw materials markets
	 */
	@FXML
	private Tab rawMaterialsMarketTab;
	/**
	 * contains basic information about assets
	 */
	@FXML
	private Tab assetsTab;
	/**
	 * contains basic information about indexes
	 */
	@FXML
	private Tab indexesTab;
	/**
	 * contains basic information about exchange markets
	 */
	@FXML
	private Tab exchangeMarketTab;
	/**
	 * contains basic information about investment funds
	 */
	@FXML
	private Tab investmentFundsTab;

	
	/**
	 * x position of a window 
	 */
	private static double xOffset = 0;
	/**
	 * y position of a window
	 */
	private static double yOffset = 0;

	// ---------------------------------------investors tab
	/**
	 * table that holds all investors
	 */
	@FXML
	private TableView<Investor> investorsTable;

	/**
	 * column that contains investor's name
	 */
	@FXML
	private TableColumn<Investor, String> investorsName;

	/**
	 * column that contains investor's surname
	 */
	@FXML
	private TableColumn<Investor, String> investorsSurname;

	/**
	 * column that contains investor's pesel
	 */
	@FXML
	private TableColumn<Investor, String> investorsPesel;

	/**
	 * @return returns all available investors
	 */
	public ObservableList<Investor> getInvestors() {
		ObservableList<Investor> investors = FXCollections.observableArrayList(PseudoDB.getInvestors());
		System.out.println(investors);
		return investors;
	}

	/**
	 * loads investors to the table
	 */
	private void loadInvestors() {
		investorsName.setCellValueFactory(new PropertyValueFactory<Investor, String>("name"));
		investorsSurname.setCellValueFactory(new PropertyValueFactory<Investor, String>("surname"));
		investorsPesel.setCellValueFactory(new PropertyValueFactory<Investor, String>("PESEL"));
		investorsName.setStyle("-fx-alignment: center;");
		investorsPesel.setStyle("-fx-alignment: center;");
		investorsSurname.setStyle("-fx-alignment: center;");
		investorsTable.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown() && event.getClickCount() == 2
						&& investorsTable.getSelectionModel().getSelectedItem() != null) {
					System.out.println(investorsTable.getSelectionModel().getSelectedItem());

					try {
						showInfoAbout(investorsTable.getSelectionModel().getSelectedItem(), investorsTable);
					} catch (IOException e) {
						DialogUtils.errorDialog(e.getMessage());
						e.printStackTrace();
					}
				}

			}
		});
		investorsTable.setItems(getInvestors());
	}

	/**
	 * @param investor investor to display information about
	 * @param investors table of investors to update after optional deleting
	 * @throws IOException may throw an IO exception
	 */
	private void showInfoAbout(Investor investor, TableView<Investor> investors) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/outline/ShowInvestor.fxml"));
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
		BlurUtils.blur(closeIcon.getScene().getRoot());
		stage.show();
		ShowInvestorController controller = loader.<ShowInvestorController>getController();
		controller.initData(investor, investors);
	}
	// -------------------------end of investors tab

	// ------------------------stock exchange tab
	/**
	 * table of all companies
	 */
	@FXML
	private TableView<Company> seCompanies;

	/**
	 * combo box that holds stock exchanges to choose from
	 */
	@FXML
	private JFXComboBox<String> seComboBox;
	/**
	 * used for displaying more info about stock exchange choosed in combo box
	 */
	@FXML
	private JFXButton seMoreButton;

	/**
	 * column that holds the name of company
	 */
	@FXML
	private TableColumn<Company, String> seCompany;

	/**
	 * holds company's sector
	 */
	@FXML
	private TableColumn<Company, String> seSector;

	/**
	 * holds company's open rate
	 */
	@FXML
	private TableColumn<Company, String> seOpen;

	/**
	 * holds company's last rating
	 */
	@FXML
	private TableColumn<Company, String> seLast;

	/**
	 * holds company's change over the time of last session
	 */
	@FXML
	private TableColumn<Company, String> seChange;

	/**
	 * change of rating as percentage
	 */
	@FXML
	private TableColumn<Company, String> sePct;

	/**
	 *	gets all available companies
	 * @param se stock exchange choosed from combo box
	 * @return returns all the companies available at given stock exchange
	 */
	private ObservableList<Company> getCompanies(StockExchange se) {
		ObservableList<Company> companies = FXCollections.observableArrayList(se.getCompanyList());
		System.out.println(companies);
		return companies;
	}

	/**
	 *  used for loading all stock exchanges to the tab 
	 */
	public void loadStockExchanges() {
		seMoreButton.setDisable(true);
		seComboBox.getItems().setAll(FXCollections.observableArrayList(
				PseudoDB.getStockExchanges().stream().map(r -> r.getName()).collect(Collectors.toList())));
		seComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				seMoreButton.setDisable(false);
				StockExchange se = null;
				try {
					se = PseudoDB.getStockExchanges().stream().filter(e -> e.getName().equals(newValue))
							.collect(Collectors.toList()).get(0);
				} catch (IndexOutOfBoundsException e) {
					//means that no stock exchange is available
				}

				seCompanies.setItems(null);
				try {
					seCompanies.setItems(getCompanies(se));

				} catch (NullPointerException e) {
					//there is no stock exchange available yet
				}
			}

		});

		seCompany.setCellValueFactory(new PropertyValueFactory<Company, String>("name"));
		seSector.setCellValueFactory(new PropertyValueFactory<Company, String>("economicSector"));
		seOpen.setCellValueFactory(new Callback<CellDataFeatures<Company, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Company, String> c) {
				StringProperty obs = new SimpleStringProperty(
						PseudoDB.getShares().stream().filter(s -> s.getName().equals(c.getValue().getName()))
								.collect(Collectors.toList()).get(0).getInitialRate().toString());
				return obs;
			}
		});
		seLast.setCellValueFactory(new Callback<CellDataFeatures<Company, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Company, String> c) {
				StringProperty obs = new SimpleStringProperty(
						PseudoDB.getShares().stream().filter(s -> s.getName().equals(c.getValue().getName()))
								.collect(Collectors.toList()).get(0).getCurrentRate().toString());
				return obs;
			}
		});
		seChange.setCellValueFactory(new Callback<CellDataFeatures<Company, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Company, String> c) {
				StringProperty obs = new SimpleStringProperty(
						PseudoDB.getShares().stream().filter(s -> s.getName().equals(c.getValue().getName()))
								.collect(Collectors.toList()).get(0).getChange().toString());
				return obs;
			}
		});
		sePct.setCellValueFactory(new Callback<CellDataFeatures<Company, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Company, String> c) {
				StringProperty obs = new SimpleStringProperty(PseudoDB.getShares().stream()
						.filter(s -> s.getName().equals(c.getValue().getName())).collect(Collectors.toList()).get(0)
						.getChangePct().multiply(new BigDecimal(100)).toString());
				return obs;
			}
		});
		seCompany.setStyle("-fx-alignment: center;");
		seSector.setStyle("-fx-alignment: center;");
		seOpen.setStyle("-fx-alignment: center;");
		seLast.setStyle("-fx-alignment: center;");
		seChange.setStyle("-fx-alignment: center;");
		sePct.setStyle("-fx-alignment: center;");

		seCompanies.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown() && event.getClickCount() == 2
						&& seCompanies.getSelectionModel().getSelectedItem() != null) {
					System.out.println(seCompanies.getSelectionModel().getSelectedItem());

					try {
						showInfoAbout(seCompanies.getSelectionModel().getSelectedItem(), seCompanies);
					} catch (Exception e) {
						DialogUtils.errorDialog(e.getMessage());
						e.printStackTrace();
					}
				}

			}
		});
	}

	/**
	 * used to display information about specific stock exchange
	 * @param se stock exchange to display information about
	 * @param seTable table of all companies that may be updated
	 * @throws IOException may throw an IO exception
	 */
	private void showInfoAbout(StockExchange se, TableView<Company> seTable) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/outline/ShowStockExchange.fxml"));
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
		BlurUtils.blur(closeIcon.getScene().getRoot());
		stage.show();
		ShowStockExchangeController controller = loader.<ShowStockExchangeController>getController();
		controller.initData(se, seTable, seComboBox);
	}

	/**
	 * used to display more information about specific company
	 * @param company to display more info about
	 * @param seTable table of all companies, it might need updating
	 * @throws IOException may throw an IO exception
	 */
	private void showInfoAbout(Company company, TableView<Company> seTable) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/outline/ShowCompany.fxml"));
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
		BlurUtils.blur(closeIcon.getScene().getRoot());
		stage.show();
		ShowCompanyController controller = loader.<ShowCompanyController>getController();
		controller.initData(company, seCompanies,
				PseudoDB.getStockExchangeByName(seComboBox.getSelectionModel().getSelectedItem()));
	}

	/**
	 * handles 'more' button
	 */
	@FXML
	public void seMoreAction() {
		try {
			showInfoAbout(PseudoDB.getStockExchangeByName(seComboBox.getSelectionModel().getSelectedItem()),
					seCompanies);
		} catch (Exception e) {
			DialogUtils.errorDialog(e.getMessage());
			e.printStackTrace();
		}
	}

	// ----------------------end of stock exchange tab

	// investment funds tab
	/**
	 * table of all investment funds
	 */
	@FXML
	private TableView<InvestmentFund> investmentFunds;

	/**
	 * table column that holds investment fund's name
	 */
	@FXML
	private TableColumn<InvestmentFund, String> fundName;

	/**
	 * table column that holds name of CEO of investment fund
	 */
	@FXML
	private TableColumn<InvestmentFund, String> fundCEOName;

	/**
	 * table column that holds surname of CEO of investment fund
	 */
	@FXML
	private TableColumn<InvestmentFund, String> fundCEOSurname;

	/**
	 * used for getting all investment funds in the system
	 * @return returns all investment funds have been added
	 */
	public ObservableList<InvestmentFund> getInvestmentFunds() {
		ObservableList<InvestmentFund> inFunds = FXCollections.observableArrayList(PseudoDB.getInvestmentFunds());
		System.out.println(inFunds);
		return inFunds;
	}

	/**
	 * loads all investment funds to the table
	 */
	private void loadInvestmentFunds() {
		fundName.setCellValueFactory(new PropertyValueFactory<InvestmentFund, String>("name"));
		fundCEOName.setCellValueFactory(new PropertyValueFactory<InvestmentFund, String>("nameOfCEO"));
		fundCEOSurname.setCellValueFactory(new PropertyValueFactory<InvestmentFund, String>("surnameOfCEO"));
		fundName.setStyle("-fx-alignment: center;");
		fundCEOName.setStyle("-fx-alignment: center;");
		fundCEOSurname.setStyle("-fx-alignment: center;");
		investmentFunds.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown() && event.getClickCount() == 2
						&& investmentFunds.getSelectionModel().getSelectedItem() != null) {
					System.out.println(investmentFunds.getSelectionModel().getSelectedItem());

					try {
						showInfoAbout(investmentFunds.getSelectionModel().getSelectedItem(), investmentFunds);
					} catch (IOException e) {
						DialogUtils.errorDialog(e.getMessage());
						e.printStackTrace();
					}
				}

			}
		});
		investmentFunds.setItems(getInvestmentFunds());
	}

	/** 
	 * used for displaying more information about specific investment fund
	 * @param investmentFund a fund to display more info about
	 * @param investmentFunds table of all investment funds that might need to be updated
	 * @throws IOException may throw an IO exception
	 */
	private void showInfoAbout(InvestmentFund investmentFund, TableView<InvestmentFund> investmentFunds)
			throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/outline/ShowInvestmentFund.fxml"));
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
		BlurUtils.blur(closeIcon.getScene().getRoot());
		stage.show();
		ShowInvestmentFundController controller = loader.<ShowInvestmentFundController>getController();
		controller.initData(investmentFund, investmentFunds);
	}

	// end of investment funds tab

	// assets tab
	/**
	 * table of all assets
	 */
	@FXML
	private TableView<Asset> assetsTable;

	/**
	 *  combobox that holds the type of asset that user wants to see in the table
	 */
	@FXML
	private JFXComboBox<String> assetType;

	/**
	 * table column that holds information about the type of the asset 
	 */
	@FXML
	private TableColumn<Asset, String> assetName;

	/**
	 * table column that holds information about the value of specific asset
	 */
	@FXML
	private TableColumn<Asset, BigDecimal> assetValue;

	/**
	 * table column that holds information about the change of specific asset
	 */
	@FXML
	private TableColumn<Asset, BigDecimal> assetChange;

	/**
	 * table column that holds information about the change(%) of specific asset
	 */
	@FXML
	private TableColumn<Asset, String> assetChangePrc;

	/**
	 * button that enables comparing assets
	 */
	@FXML
	private JFXButton compareButton;
	
	/**
	 * displays the pop up used for comparing assets
	 */
	@FXML
	public void compareAssets() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/outline/ComparisonPopUp.fxml"));
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
			BlurUtils.blur(closeIcon.getScene().getRoot());
			stage.show();
		
		} catch (Exception e) {
			DialogUtils.errorDialog(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * loads all the asset into a table
	 */
	private void loadAssets() {
		assetType.getItems()
				.setAll(FXCollections.observableArrayList(Arrays.asList("Shares", "Currencies", "Raw Materials")));
		assetType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				switch (newValue) {
				case "Shares":
					ObservableList<Asset> shares = FXCollections.observableArrayList(PseudoDB.getShares());
					assetsTable.setItems(shares);

					break;
				case "Currencies":
					ObservableList<Asset> currencies = FXCollections.observableArrayList(PseudoDB.getCurrencies()
							.stream().filter(c -> !c.getName().equals("USD")).collect(Collectors.toList()));
					assetsTable.setItems(currencies);

					break;
				case "Raw Materials":

					ObservableList<Asset> rawMaterials = FXCollections.observableArrayList(PseudoDB.getRawMaterials());
					assetsTable.setItems(rawMaterials);

					break;
				}
				assetName.setCellValueFactory(new PropertyValueFactory<Asset, String>("name"));
				assetValue.setCellValueFactory(new PropertyValueFactory<Asset, BigDecimal>("currentRate"));
				assetChange.setCellValueFactory(new PropertyValueFactory<Asset, BigDecimal>("changePct"));
				assetName.setStyle("-fx-alignment: center;");
				assetValue.setStyle("-fx-alignment: center;");
				assetChange.setStyle("-fx-alignment: center;");
				assetsTable.setOnMousePressed(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (event.isPrimaryButtonDown() && event.getClickCount() == 2
								&& assetsTable.getSelectionModel().getSelectedItem() != null) {
							System.out.println(assetsTable.getSelectionModel().getSelectedItem());

							try {
								showInfoAbout(assetsTable.getSelectionModel().getSelectedItem(), assetsTable);
							} catch (Exception e) {
								DialogUtils.errorDialog(e.getMessage());
								e.printStackTrace();
							}
						}

					}
				});
			}
		});
	}

	/**
	 * shows more information about specific asset
	 * @param asset asset to display more info about
	 * @param assetsTable table of all the assets that might need updating
	 * @throws IOException may throw an IO exception
	 */
	private void showInfoAbout(Asset asset, TableView<Asset> assetsTable) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/outline/ShowShare.fxml"));
		if (asset instanceof Share) {
			if (!asset.isQuoted()) {
				loader = new FXMLLoader(getClass().getResource("/view/outline/ShowShare.fxml"));

			} else {
				loader = new FXMLLoader(getClass().getResource("/view/outline/ShowCompanyRatings.fxml"));
			}
		} else if (asset instanceof Currency) {
			loader = new FXMLLoader(getClass().getResource("/view/outline/ShowCurrency.fxml"));
		} else if (asset instanceof RawMaterial) {
			loader = new FXMLLoader(getClass().getResource("/view/outline/ShowRawMaterial.fxml"));
		}

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
		BlurUtils.blur(closeIcon.getScene().getRoot());
		stage.show();

		if (asset instanceof Share) {
			if (!asset.isQuoted()) {
				ShowShareController controller = loader.<ShowShareController>getController();
				controller.initData((Share) asset);
			}else {
				ShowCompanyRatingsController controller = loader.<ShowCompanyRatingsController>getController();
				controller.initData((Share)asset,"assetsTab");
			}
		} else if (asset instanceof Currency) {
			ShowCurrencyController controller = loader.<ShowCurrencyController>getController();
			controller.initData((Currency) asset, null);
		} else if (asset instanceof RawMaterial) {
			ShowRawMaterialController controller = loader.<ShowRawMaterialController>getController();
			controller.initData((RawMaterial) asset, null);
		}

	}

	// end of assets tab

	// exchange market tab

	/**
	 * table that holds all currencies
	 */
	@FXML
	private TableView<Currency> emCurrenciesTable;

	/**
	 * the column of two currencies pair names
	 */
	@FXML
	private TableColumn<Currency, String> emPairCol;
	/**
	 * highest rate in current session
	 */
	@FXML
	private TableColumn<Currency, BigDecimal> emHighCol;
	/**
	 * lowest rate in current session
	 */
	@FXML
	private TableColumn<Currency, BigDecimal> emLowCol;
	/**
	 * change as a result of subtraction
	 */
	@FXML
	private TableColumn<Currency, BigDecimal> emChangeCol;
	/**
	 * last rating value
	 */
	@FXML
	private TableColumn<Currency, BigDecimal> emLastCol;
	/**
	 * change as percentage
	 */
	@FXML
	private TableColumn<Currency, BigDecimal> emPctCol;
	/**
	 * the initial rate
	 */
	@FXML
	private TableColumn<Currency, BigDecimal> emOpenCol;

	/**
	 * button used for displaying more information on specific exchange market
	 */
	@FXML
	private JFXButton emMoreInfo;

	/**
	 * combo box that holds all exchange markets
	 */
	@FXML
	private JFXComboBox<String> emComboBox;

	/**
	 * used to get all currencies of specific exchange market
	 * @param em exchange market
	 * @return returns the list of all currencies from specific exchange market
	 */
	private ObservableList<Currency> getCurrencies(ExchangeMarket em) {
		ObservableList<Currency> currencies = FXCollections.observableArrayList(
				em.getQuotedCurrencies().stream().filter(c -> !c.getName().equals("USD")).collect(Collectors.toList()));
		System.out.println(currencies);
		return currencies;
	}

	/**
	 * loads all exchange markets into combobox
	 */
	private void loadExchangeMarkets() {
		emMoreInfo.setDisable(true);
		emComboBox.getItems().setAll(FXCollections.observableArrayList(
				PseudoDB.getExchangeMarkets().stream().map(e -> e.getName()).collect(Collectors.toList())));
		emComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				emMoreInfo.setDisable(false);
				ExchangeMarket em = PseudoDB.getExchangeMarkets().stream().filter(e -> e.getName().equals(newValue))
						.collect(Collectors.toList()).get(0);
				emCurrenciesTable.setItems(null);
				emCurrenciesTable.setItems(getCurrencies(em));
			}

		});

		emPairCol.setCellValueFactory(new Callback<CellDataFeatures<Currency, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Currency, String> c) {
				// p.getValue() returns the Person instance for a particular TableView row
				StringProperty obs = new SimpleStringProperty(c.getValue().getName() + "/USD");
				return obs;
			}
		});
		emHighCol.setCellValueFactory(new PropertyValueFactory<Currency, BigDecimal>("maximalRate"));
		emLowCol.setCellValueFactory(new PropertyValueFactory<Currency, BigDecimal>("minimalRate"));
		emOpenCol.setCellValueFactory(new PropertyValueFactory<Currency, BigDecimal>("initialRate"));
		emLastCol.setCellValueFactory(new PropertyValueFactory<Currency, BigDecimal>("currentRate"));
		emChangeCol.setCellValueFactory(new PropertyValueFactory<Currency, BigDecimal>("change"));
		emPctCol.setCellValueFactory(new PropertyValueFactory<Currency, BigDecimal>("changePct"));

		emPairCol.setStyle("-fx-alignment: center;");
		emHighCol.setStyle("-fx-alignment: center;");
		emLowCol.setStyle("-fx-alignment: center;");
		emOpenCol.setStyle("-fx-alignment: center;");
		emLastCol.setStyle("-fx-alignment: center;");
		emChangeCol.setStyle("-fx-alignment: center;");
		emPctCol.setStyle("-fx-alignment: center;");

		emCurrenciesTable.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown() && event.getClickCount() == 2
						&& emCurrenciesTable.getSelectionModel().getSelectedItem() != null) {
					System.out.println(emCurrenciesTable.getSelectionModel().getSelectedItem());

					try {
						showInfoAbout(emCurrenciesTable.getSelectionModel().getSelectedItem(), emCurrenciesTable);
					} catch (Exception e) {
						DialogUtils.errorDialog(e.getMessage());
						e.printStackTrace();
					}
				}

			}
		});

	}

	/**
	 * used to display more info on specific exchange market
	 */
	@FXML
	public void emMoreAction() {
		try {
			showInfoAbout(PseudoDB.getExchangeMarketByName(emComboBox.getSelectionModel().getSelectedItem()),
					emCurrenciesTable);
		} catch (Exception e) {
			DialogUtils.errorDialog(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * displays a pop up with more information about specific exchange market
	 * @param emMarket a market to display more info about
	 * @param emTable table of all currencies available on the given exchange market
	 * @throws IOException may throw an IO exception
	 */
	private void showInfoAbout(ExchangeMarket emMarket, TableView<Currency> emTable) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/outline/ShowExchangeMarket.fxml"));
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
		BlurUtils.blur(closeIcon.getScene().getRoot());
		stage.show();
		ShowExchangeMarketController controller = loader.<ShowExchangeMarketController>getController();
		controller.initData(emMarket, emTable);
	}

	/**
	 * displays a pop up with information on specific currency
	 * @param currency the currency to display more info about
	 * @param emTable table of all currencies available on exchange market
	 * @throws IOException may throw an IO exception
	 */
	private void showInfoAbout(Currency currency, TableView<Currency> emTable) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/outline/ShowCurrency.fxml"));
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
		BlurUtils.blur(closeIcon.getScene().getRoot());
		stage.show();
		ShowCurrencyController controller = loader.<ShowCurrencyController>getController();
		controller.initData(currency, emCurrenciesTable);
	}

	@FXML
	public void emCompareAction() {

	}

	// end of exchange market tab

	// raw materials market tab
	/**
	 * combo box containing all raw materials markets
	 */
	@FXML
	private JFXComboBox<String> rmComboBox;
	/**
	 * table of raw materials available on specific raw materials market
	 */
	@FXML
	private TableView<RawMaterial> rmMaterialsTable;
	/**
	 * button used for displaying more info about specific raw material
	 */
	@FXML
	private JFXButton rmMoreButton;
	/**
	 * table column that holds raw material name
	 */
	@FXML
	private TableColumn<RawMaterial, String> rmRawMaterial;
	/**
	 * table column that holds the name of currency and unit of raw material
	 */
	@FXML
	private TableColumn<RawMaterial, String> rmUnitCurrency;
	/**
	 * table column that holds the open rate of raw material
	 */
	@FXML
	private TableColumn<RawMaterial, BigDecimal> rmOpen;
	/**
	 * table column that holds the highest rate in current session of raw material
	 */
	@FXML
	private TableColumn<RawMaterial, BigDecimal> rmHigh;
	/**
	 * table column that holds the lowest rate in current session of raw material
	 */
	@FXML
	private TableColumn<RawMaterial, BigDecimal> rmLow;
	/**
	 * table column that holds the last rate in current session of raw material
	 */
	@FXML
	private TableColumn<RawMaterial, BigDecimal> rmLast;
	/**
	 * table column that holds change in current session of raw material
	 */
	@FXML
	private TableColumn<RawMaterial, BigDecimal> rmChange;
	/**
	 * table column that holds percentage change in current session of raw material
	 */
	@FXML
	private TableColumn<RawMaterial, BigDecimal> rmPct;

	/**
	 * used to get raw materials quoted on given raw materials market
	 * @param rmm raw materials market
	 * @return returns all of raw materials quoted on given raw materials market
	 */
	private ObservableList<RawMaterial> getRawMaterials(RawMaterialsMarket rmm) {
		ObservableList<RawMaterial> materials = FXCollections.observableArrayList(rmm.getQuotedRawMaterials());
		System.out.println(materials);
		return materials;
	}

	/**
	 * loads raw materials into the table
	 */
	private void loadRawMaterialsMarkets() {
		rmMoreButton.setDisable(true);
		rmComboBox.getItems().setAll(FXCollections.observableArrayList(
				PseudoDB.getRawMaterialsMarkets().stream().map(r -> r.getName()).collect(Collectors.toList())));
		rmComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				rmMoreButton.setDisable(false);
				RawMaterialsMarket rmm = PseudoDB.getRawMaterialsMarkets().stream()
						.filter(e -> e.getName().equals(newValue)).collect(Collectors.toList()).get(0);
				rmMaterialsTable.setItems(null);
				rmMaterialsTable.setItems(getRawMaterials(rmm));
			}

		});

		rmRawMaterial.setCellValueFactory(new PropertyValueFactory<RawMaterial, String>("name"));
		rmUnitCurrency
				.setCellValueFactory(new Callback<CellDataFeatures<RawMaterial, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<RawMaterial, String> c) {
						// p.getValue() returns the Person instance for a particular TableView row
						StringProperty obs = new SimpleStringProperty(
								c.getValue().getUnit().toString() + "/" + c.getValue().getCurrency().getName());
						return obs;
					}
				});
		rmOpen.setCellValueFactory(new PropertyValueFactory<RawMaterial, BigDecimal>("initialRate"));
		rmHigh.setCellValueFactory(new PropertyValueFactory<RawMaterial, BigDecimal>("maximalRate"));
		rmLow.setCellValueFactory(new PropertyValueFactory<RawMaterial, BigDecimal>("minimalRate"));
		rmLast.setCellValueFactory(new PropertyValueFactory<RawMaterial, BigDecimal>("currentRate"));
		rmChange.setCellValueFactory(new PropertyValueFactory<RawMaterial, BigDecimal>("change"));
		rmPct.setCellValueFactory(new PropertyValueFactory<RawMaterial, BigDecimal>("changePct"));

		rmRawMaterial.setStyle("-fx-alignment: center;");
		rmUnitCurrency.setStyle("-fx-alignment: center;");
		rmOpen.setStyle("-fx-alignment: center;");
		rmHigh.setStyle("-fx-alignment: center;");
		rmLow.setStyle("-fx-alignment: center;");
		rmLast.setStyle("-fx-alignment: center;");
		rmChange.setStyle("-fx-alignment: center;");
		rmPct.setStyle("-fx-alignment: center;");

		rmMaterialsTable.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown() && event.getClickCount() == 2
						&& rmMaterialsTable.getSelectionModel().getSelectedItem() != null) {
					System.out.println(rmMaterialsTable.getSelectionModel().getSelectedItem());

					try {
						showInfoAbout(rmMaterialsTable.getSelectionModel().getSelectedItem(), rmMaterialsTable);
					} catch (Exception e) {
						DialogUtils.errorDialog(e.getMessage());
						e.printStackTrace();
					}
				}

			}
		});
	}

	/**
	 * shows info on specific raw materials market
	 * @param rmMarket raw materials market to show info about
	 * @param rmTable raw materials table that might need to be updated
	 * @throws IOException may throw an IO exception
	 */
	private void showInfoAbout(RawMaterialsMarket rmMarket, TableView<RawMaterial> rmTable) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/outline/ShowRawMaterialsMarket.fxml"));
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
		BlurUtils.blur(closeIcon.getScene().getRoot());
		stage.show();
		ShowRawMaterialsMarketController controller = loader.<ShowRawMaterialsMarketController>getController();
		controller.initData(rmMarket, rmTable);
	}

	/**
	 * displays poop up with information about specific raw material
	 * @param rawMaterial raw material to show info about
	 * @param rmTable table of raw materials that might need to be updated 
	 * @throws IOException may throw an IO exception
	 */
	private void showInfoAbout(RawMaterial rawMaterial, TableView<RawMaterial> rmTable) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/outline/ShowRawMaterial.fxml"));
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
		BlurUtils.blur(closeIcon.getScene().getRoot());
		stage.show();
		ShowRawMaterialController controller = loader.<ShowRawMaterialController>getController();
		controller.initData(rawMaterial, rmMaterialsTable);
	}

	/**
	 * displays information about specific raw materials market
	 */
	@FXML
	public void rmMoreAction() {
		try {
			showInfoAbout(PseudoDB.getRawMaterialsMarketByName(rmComboBox.getSelectionModel().getSelectedItem()),
					rmMaterialsTable);
		} catch (Exception e) {
			DialogUtils.errorDialog(e.getMessage());
			e.printStackTrace();
		}
	}

	// end of raw materials market tab

	// indexes tab
	/**
	 * combo box that holds all stock exchanges
	 */
	@FXML
	private JFXComboBox<String> indComboBox;
	/**
	 * table of all indexes on given stock exchange
	 */
	@FXML
	private TableView<StockExchangeIndex> indTable;

	/**
	 * table column that holds the name of index
	 */
	@FXML
	private TableColumn<StockExchangeIndex, String> indIdxCol;
	/**
	 * table column that holds the value of index
	 */
	@FXML
	private TableColumn<StockExchangeIndex, BigDecimal> indValCol;
	/**
	 * table column that holds the change of index
	 */
	@FXML
	private TableColumn<StockExchangeIndex, BigDecimal> indChangeCol;

	/**
	 * used to obtain the list of indexes on specific stock exchange
	 * @param stockExchange stock exchange
	 * @return returns the list of indexes on specific stock exchange
	 */
	private ObservableList<StockExchangeIndex> getIndexes(StockExchange stockExchange) {
		ObservableList<StockExchangeIndex> indexes = FXCollections.observableArrayList(stockExchange.getIndexes());
		System.out.println(indexes);
		return indexes;
	}

	/**
	 * loads indexes into the table
	 */
	private void loadStockExchangesForIndexes() {
		indComboBox.getItems().setAll(FXCollections.observableArrayList(
				PseudoDB.getStockExchanges().stream().map(r -> r.getName()).collect(Collectors.toList())));
		indComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				StockExchange se = PseudoDB.getStockExchanges().stream().filter(e -> e.getName().equals(newValue))
						.collect(Collectors.toList()).get(0);
				indTable.setItems(null);
				indTable.setItems(getIndexes(se));
			}

		});

		indIdxCol.setCellValueFactory(new PropertyValueFactory<StockExchangeIndex, String>("name"));
		indValCol.setCellValueFactory(new PropertyValueFactory<StockExchangeIndex, BigDecimal>("value"));
		indChangeCol.setCellValueFactory(new PropertyValueFactory<StockExchangeIndex, BigDecimal>("change"));
		indIdxCol.setStyle("-fx-alignment: center;");
		indValCol.setStyle("-fx-alignment: center;");
		indChangeCol.setStyle("-fx-alignment: center;");

		indTable.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown() && event.getClickCount() == 2
						&& indTable.getSelectionModel().getSelectedItem() != null) {
					System.out.println(indTable.getSelectionModel().getSelectedItem());

					try {
						showInfoAbout(indTable.getSelectionModel().getSelectedItem(), indTable);
						System.out.println(indTable.getSelectionModel().getSelectedItem());
					} catch (Exception e) {
						DialogUtils.errorDialog(e.getMessage());
						e.printStackTrace();
					}
				}

			}
		});
	}

	/**
	 * displays a pop up with the information about specific stock exchange index
	 * @param stockExchangeIndex stock exchange index
	 * @param indTable table of all indexes
	 * @throws IOException may throw an IO exception
	 */
	private void showInfoAbout(StockExchangeIndex stockExchangeIndex, TableView<StockExchangeIndex> indTable) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/outline/ShowStockExchangeIndex.fxml"));
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
		BlurUtils.blur(closeIcon.getScene().getRoot());
		stage.show();
		ShowStockExchangeIndexController controller = loader.<ShowStockExchangeIndexController>getController();
		controller.initData(stockExchangeIndex, indTable);
	}

	// end of indexes market tab
	
	
	

	/**
	 * typical initialize method that initializes all the tables on every tab
	 */
	@FXML
	private void initialize() {
		loadInvestors();
		loadStockExchanges();
		loadInvestmentFunds();
		loadAssets();
		loadExchangeMarkets();
		loadRawMaterialsMarkets();
		loadStockExchangesForIndexes();
	}

	/**
	 * closes the application
	 * @param event mouse event to close the app
	 */
	@FXML
	public void closeApp(MouseEvent event) {
		Stage stage = (Stage) closeIcon.getScene().getWindow();
		stage.close();
		Platform.exit();
		System.exit(0);
	}
}
