package stockmarket.controller.outline;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import stockmarket.model.RatingRecord;
import stockmarket.model.StockExchange;
import stockmarket.model.StockExchangeIndex;
import stockmarket.utils.BlurUtils;
import stockmarket.utils.DateAxis;
import stockmarket.utils.DateUtil;
import stockmarket.utils.PseudoDB;

/**
 * controller for stock exchange index pop up
 * @author Damian Horna
 *
 */
public class ShowStockExchangeIndexController {

	/**
	 * for closing the application window
	 */
	@FXML
	private MaterialDesignIconView closePopUpIco;

	/**
	 * name of index
	 */
	@FXML
	private Label indName;
	/**
	 * value of index
	 */
	@FXML
	private Label indValue;
	/**
	 * sector of companies in the index
	 */
	@FXML
	private Label indSector;
	/**
	 * stack pane for displaying error messages
	 */
	@FXML
	private StackPane stackPane;
	/**
	 * button for refreshing the chart
	 */
	@FXML
	private JFXButton refreshButton;
	
	/**
	 * icon for deleting the index
	 */
	@FXML
	private MaterialDesignIconView deleteIcon;

	/**
	 * stock exchange selection
	 */
	@FXML
	private JFXComboBox<String> periodComboBox;

	/**
	 * reference to index
	 */
	private StockExchangeIndex index;
	
	/**
	 * table of all indexes on given stock exchange
	 */
	TableView<StockExchangeIndex> indTable;
	
	/**
	 * line chart with index ratings
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
					allRecords = index.getRatingRecords();
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
					allRecords = index.getRatingRecords();
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
					allRecords = index.getRatingRecords();
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
					allRecords = index.getRatingRecords();
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
	 * @param stockExchange
	 * @return returns all indexes on given stock exchange
	 */
	private ObservableList<StockExchangeIndex> getStockExchangeIndexes(StockExchange stockExchange) {
		ObservableList<StockExchangeIndex> indexes = FXCollections.observableArrayList(
				stockExchange.getIndexes());
		System.out.println(indexes);
		return indexes;
	}
	
	
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
	 * @param sei
	 * @param indTable
	 */
	public void initData(StockExchangeIndex sei, TableView<StockExchangeIndex> indTable) {
		this.index = sei;
		this.indTable = indTable;	
		indName.setText(sei.getName());
		
		indValue.setText(sei.getValue().toString());
		indSector.setText(sei.getCondition());

		Date day1 = DateUtil.getMidnightYesterday();
		Date day2 = DateUtil.getMidnightTonight();
		List<RatingRecord> all = sei.getRatingRecords();
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
	 * deletes an index from simulation
	 * @param event
	 */
	@FXML
	void deleteIndex(MouseEvent event) {
			Stage stage = (Stage) deleteIcon.getScene().getWindow();
			BlurUtils.unblur();
			PseudoDB.getStockExchangeIndexes().remove(index);
			StockExchange se = index.getStockExchange();
			se.getIndexes().remove(index);
			indTable.setItems(null);
			indTable.setItems(getStockExchangeIndexes(se));
			stage.close();
	}
	
	/**
	 * closes the window pop up
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
