package stockmarket.controller.outline;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import stockmarket.model.Currency;
import stockmarket.model.ExchangeMarket;
import stockmarket.model.Gambler;
import stockmarket.model.RatingRecord;
import stockmarket.utils.BlurUtils;
import stockmarket.utils.DateAxis;
import stockmarket.utils.DateUtil;
import stockmarket.utils.PseudoDB;

/**
 * controller responsible for displaying data about currency
 * @author Damian Horna
 *
 */
public class ShowCurrencyController {

	/**
	 * icon for closing the window
	 */
	@FXML
	private MaterialDesignIconView closePopUpIco;
	
	/**
	 * button used for refreshing the chart
	 */
	@FXML
	private JFXButton refreshButton;
	
	/**
	 * period selection
	 */
	@FXML
	private JFXComboBox<String> periodComboBox;

	/**
	 * name of the currency
	 */
	@FXML
	private Label currencyName;
	/**
	 * current rate of the currency
	 */
	@FXML
	private Label currencyCurrentRate;
	/**
	 * the initial rate of the currency
	 */
	@FXML
	private Label currencyInitialRate;
	/**
	 * maximal rate of given currency
	 */
	@FXML
	private Label currencyMaximalRate;
	/**
	 * minimal rate of specific currency
	 */
	@FXML
	private Label currencyMinimalRate;
	/**
	 * stack pane for displaying error messages
	 */
	@FXML
	private StackPane stackPane;
	/**
	 * used for displaying a message if a currency is not quoted yet
	 */
	@FXML
	private Label currencyQuoted;
	
	/**
	 * used for deleting the currency from the simulation
	 */
	@FXML
	private MaterialDesignIconView deleteIcon;

	/**
	 * table of currencies on a market
	 */
	private TableView<Currency> currTable;
	
	/**
	 * currency reference
	 */
	private Currency currency;
	
	/**
	 * chart that presents ratings of currency over time
	 */
	private LineChart<Date, Number> lineChart;


	/**
	 * initializes the controller with data
	 */
	@FXML
	public void initialize() {
		periodComboBox.getItems().setAll("1D","1W","1M","1Y");
		periodComboBox.getSelectionModel().select(0);

		periodComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			List<RatingRecord> allRecords;
			List<RatingRecord> meaningfullRecords;
			ObservableList<XYChart.Series<Date, Number>> series = FXCollections.observableArrayList();

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				switch (newValue) {
				case "1D":
					Date day1 = DateUtil.getMidnightYesterday();
					Date day2 = DateUtil.getMidnightTonight();
					allRecords = currency.getRatingRecords();
					meaningfullRecords = new ArrayList<>();
					for (RatingRecord r : allRecords)
						if (DateUtil.isWithinRange(r.getTime(), day1, day2))
							meaningfullRecords.add(r);
					ObservableList<XYChart.Data<Date, Number>> seriesToday = getSeries(meaningfullRecords, "1D");
					series.clear();
					series.add(new XYChart.Series<>("Quotings today", seriesToday));
					lineChart.getData().clear();
					lineChart.getData().addAll(series);
					break;
				case "1W":
					Date week1 = DateUtil.getLastWeek();
					Date week2 = DateUtil.getNextWeek();
					allRecords = currency.getRatingRecords();
					meaningfullRecords = new ArrayList<>();
					for (RatingRecord r : allRecords)
						if (DateUtil.isWithinRange(r.getTime(), week1, week2))
							meaningfullRecords.add(r);
					ObservableList<XYChart.Data<Date, Number>> seriesThisWeek = getSeries(meaningfullRecords, "1W");
					series.clear();
					series.add(new XYChart.Series<>("Quotings this week", seriesThisWeek));
					lineChart.getData().clear();
					lineChart.getData().addAll(series);
					break;
				case "1M":

					Date month1 =DateUtil. getLastMonth();
					Date month2 = DateUtil.getNextMonth();
					allRecords = currency.getRatingRecords();
					meaningfullRecords = new ArrayList<>();
					for (RatingRecord r : allRecords)
						if (DateUtil.isWithinRange(r.getTime(), month1, month2))
							meaningfullRecords.add(r);
					ObservableList<XYChart.Data<Date, Number>> seriesThisMonth = getSeries(meaningfullRecords, "1M");
					series.clear();
					series.add(new XYChart.Series<>("Quotings this month", seriesThisMonth));
					lineChart.getData().clear();
					lineChart.getData().addAll(series);
					break;
				case "1Y":

					Date year1 = DateUtil.getLastYear();
					Date year2 = DateUtil.getNextYear();
					allRecords = currency.getRatingRecords();
					meaningfullRecords = new ArrayList<>();
					for (RatingRecord r : allRecords)
						if (DateUtil.isWithinRange(r.getTime(), year1, year2))
							meaningfullRecords.add(r);
					ObservableList<XYChart.Data<Date, Number>> seriesThisYear = getSeries(meaningfullRecords, "1Y");
					series.clear();
					series.add(new XYChart.Series<>("Quotings this year", seriesThisYear));
					lineChart.getData().clear();
					lineChart.getData().addAll(series);
					break;

				}
			}

		});

	}
	
	/**
	 * gets all currencies on specific exchange market
	 * @param em exchange market
	 * @return returns the list of currencies
	 */
	private ObservableList<Currency> getCurrencies(ExchangeMarket em) {
		ObservableList<Currency> currencies = FXCollections.observableArrayList(
				em.getQuotedCurrencies().stream().filter(c -> !c.getName().equals("USD")).collect(Collectors.toList()));
		System.out.println(currencies);
		return currencies;
	}
	
	/**
	 * gets the series of meaningful ratings
	 * @param meaningfull meaningful ratings
	 * @param period period of ratings
	 * @return observable list of series
	 */
	private ObservableList<XYChart.Data<Date, Number>> getSeries(List<RatingRecord> meaningfull, String period) {
		ObservableList<XYChart.Data<Date, Number>> series = FXCollections.observableArrayList();
		switch (period) {
		case "1D":
			for (RatingRecord r : meaningfull) {
				series.add(new XYChart.Data<Date, Number>(r.getTime(), r.getRate()));
			}
			break;
		case "1W":
			for (RatingRecord r : meaningfull) {
				series.add(new XYChart.Data<Date, Number>(r.getTime(), r.getRate()));
			}
			break;
		case "1M":
			for (RatingRecord r : meaningfull) {
				series.add(new XYChart.Data<Date, Number>(r.getTime(), r.getRate()));
			}
			break;
		case "1Y":
			for (RatingRecord r : meaningfull) {
				series.add(new XYChart.Data<Date, Number>(r.getTime(), r.getRate()));
			}
			break;
		}
		return series;
	}

	/**
	 * initializes the controller with data
	 * @param theCurrency
	 * @param currenciesTable
	 */
	public void initData(Currency theCurrency, TableView<Currency> currenciesTable) {
		
		this.currency = theCurrency;
		currTable = currenciesTable;
		currencyName.setText(currency.getName());
		if (currency.isQuoted()) {
			currencyQuoted.setText("");
		}
		currencyCurrentRate.setText(currency.getCurrentRate().toString());
		currencyInitialRate.setText(currency.getInitialRate().toString());
		currencyMaximalRate.setText(currency.getMaximalRate().toString());
		currencyMinimalRate.setText(currency.getMinimalRate().toString());
		
		Date day1 = DateUtil.getMidnightYesterday();
		Date day2 = DateUtil.getMidnightTonight();
		List<RatingRecord> all = currency.getRatingRecords();
		List<RatingRecord> meaningfull = new ArrayList<>();
		for (RatingRecord r : all)
			if (DateUtil.isWithinRange(r.getTime(), day1, day2))
				meaningfull.add(r);
		// ------------------------------------------------------------------
		ObservableList<XYChart.Series<Date, Number>> series = FXCollections.observableArrayList();
		ObservableList<XYChart.Data<Date, Number>> series2Data = getSeries(meaningfull, "1D");

		series.add(new XYChart.Series<>("Quotings today", series2Data));

		NumberAxis numberAxis = new NumberAxis();
		DateAxis dateAxis = new DateAxis();
		lineChart = new LineChart<>(dateAxis, numberAxis, series);
		lineChart.relocate(36, 56);
		Pane root = (Pane) closePopUpIco.getScene().getRoot();
		root.getChildren().add(lineChart);
		lineChart.setCreateSymbols(false);

	}

	
	/**
	 * deletes currency from simulation
	 * @param event
	 */
	@FXML
	void deleteCurrency(MouseEvent event) {
		synchronized(PseudoDB.currenciesLock) {
			Stage stage = (Stage) deleteIcon.getScene().getWindow();
			BlurUtils.unblur();
			// delete gamblers' assets
			for (Gambler g : currency.getOwners()) {
				g.setPossessions(g.getPossessions().stream().filter(a -> !a.getAssetType().getName().equals(currency.getName()))
						.collect(Collectors.toList()));
			}
			// delete currency
			PseudoDB.getCurrencies().remove(currency);
			ExchangeMarket em = PseudoDB.getExchangeMarkets().stream().filter(e->e.getQuotedCurrencies().contains(currency)).collect(Collectors.toList()).get(0);
			em.getQuotedCurrencies().remove(currency);
			
			
			PseudoDB.setOrders(PseudoDB.getOrders().stream().filter(o->o.getAsset().getName()!=currency.getName()).collect(Collectors.toList()));
			try {
				currTable.setItems(null);	
				currTable.setItems(getCurrencies(em));
			}catch(Exception e) {
				//from assets tab
			}
			
			
			stage.close();
		}
		
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
	
	/**
	 * for refreshing the chart
	 * @param event
	 */
	@FXML
	public void refreshChart(ActionEvent event) {
		
	}
}
