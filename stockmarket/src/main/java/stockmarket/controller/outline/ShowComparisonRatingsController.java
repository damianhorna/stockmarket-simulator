package stockmarket.controller.outline;

import java.math.BigDecimal;
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
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import stockmarket.model.Asset;
import stockmarket.model.RatingRecord;
import stockmarket.utils.DateAxis;
import stockmarket.utils.DateUtil;

/**
 * controller responsible for showing comparison ratings of selected assets
 * @author Damian Horna
 *
 */
public class ShowComparisonRatingsController {

	/**
	 * button that refreshes the chart
	 */
	@FXML
	private JFXButton refreshButton;

	/**
	 * period selection
	 */
	@FXML
	private JFXComboBox<String> periodComboBox;

	/**
	 * assets to be presented
	 */
	private Asset asset1, asset2;

	/**
	 * line chart presenting two assets
	 */
	private LineChart<Date, Number> lineChart;

	/**
	 * icon responsible for closing the window
	 */
	@FXML
	private MaterialDesignIconView closePopUpIco;

	/**
	 * initializes all the data within pop up
	 */
	@FXML
	public void initialize() {
		periodComboBox.getItems().setAll("1D", "1W", "1M", "1Y");
		periodComboBox.getSelectionModel().select(0);

		periodComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			List<RatingRecord> allRecordsAsset1 = new ArrayList<>();
			List<RatingRecord> meaningfullRecordsAsset1 = new ArrayList<>();
			List<RatingRecord> allRecordsAsset2 = new ArrayList<>();
			List<RatingRecord> meaningfullRecordsAsset2 = new ArrayList<>();
			ObservableList<XYChart.Series<Date, Number>> series = FXCollections.observableArrayList();

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				switch (newValue) {
				case "1D":
					Date day1 = DateUtil.getMidnightYesterday();
					Date day2 = DateUtil.getMidnightTonight();
					getMeaningfull(day1, day2);
					ObservableList<XYChart.Data<Date, Number>> seriesTodayAsset1 = getSeries(meaningfullRecordsAsset1,
							"1D");
					ObservableList<XYChart.Data<Date, Number>> seriesTodayAsset2 = getSeries(meaningfullRecordsAsset2,
							"1D");

					series.clear();
					series.add(new XYChart.Series<>("Quotings today(%): " + asset1.getName(), seriesTodayAsset1));
					series.add(new XYChart.Series<>("Quotings today(%): " + asset2.getName(), seriesTodayAsset2));
					lineChart.getData().clear();
					lineChart.getData().addAll(series);
					break;
				case "1W":
					Date week1 = DateUtil.getLastWeek();
					Date week2 = DateUtil.getNextWeek();
					getMeaningfull(week1, week2);
					ObservableList<XYChart.Data<Date, Number>> seriesThisWeekAsset1 = getSeries(
							meaningfullRecordsAsset1, "1W");
					ObservableList<XYChart.Data<Date, Number>> seriesThisWeekAsset2 = getSeries(
							meaningfullRecordsAsset2, "1W");

					series.clear();
					series.add(
							new XYChart.Series<>("Quotings this week(%): " + asset1.getName(), seriesThisWeekAsset1));
					series.add(
							new XYChart.Series<>("Quotings this week(%): " + asset2.getName(), seriesThisWeekAsset2));

					lineChart.getData().clear();
					lineChart.getData().addAll(series);
					break;
				case "1M":

					Date month1 = DateUtil.getLastMonth();
					Date month2 = DateUtil.getNextMonth();
					getMeaningfull(month1, month2);
					ObservableList<XYChart.Data<Date, Number>> seriesThisMonthAsset1 = getSeries(
							meaningfullRecordsAsset1, "1M");
					ObservableList<XYChart.Data<Date, Number>> seriesThisMonthAsset2 = getSeries(
							meaningfullRecordsAsset2, "1M");

					series.clear();
					series.add(
							new XYChart.Series<>("Quotings this month(%): " + asset1.getName(), seriesThisMonthAsset1));
					series.add(
							new XYChart.Series<>("Quotings this month(%): " + asset2.getName(), seriesThisMonthAsset2));

					lineChart.getData().clear();
					lineChart.getData().addAll(series);
					break;
				case "1Y":

					Date year1 = DateUtil.getLastYear();
					Date year2 = DateUtil.getNextYear();
					getMeaningfull(year1, year2);
					ObservableList<XYChart.Data<Date, Number>> seriesThisYearAsset1 = getSeries(
							meaningfullRecordsAsset1, "1Y");
					ObservableList<XYChart.Data<Date, Number>> seriesThisYearAsset2 = getSeries(
							meaningfullRecordsAsset2, "1Y");

					series.clear();
					series.add(
							new XYChart.Series<>("Quotings this year(%): " + asset1.getName(), seriesThisYearAsset1));
					series.add(
							new XYChart.Series<>("Quotings this year(%): " + asset2.getName(), seriesThisYearAsset2));

					lineChart.getData().clear();
					lineChart.getData().addAll(series);
					break;

				}
			}

			/**
			 * gets the rating records that correspond to the selected period of time
			 * @param date1
			 * @param date2
			 */
			private void getMeaningfull(Date date1, Date date2) {
				allRecordsAsset1 = asset1.getRatingRecords();
				allRecordsAsset2 = asset2.getRatingRecords();
				meaningfullRecordsAsset2.clear();
				meaningfullRecordsAsset1.clear();
				boolean first1 = true, first2 = true;
				BigDecimal init1 = BigDecimal.ONE;
				BigDecimal init2 = BigDecimal.ONE;
				for (RatingRecord r : allRecordsAsset1)
					if (DateUtil.isWithinRange(r.getTime(), date1, date2)) {
						if (first1) {
							init1 = r.getRate();
							first1 = false;
						}
						RatingRecord rr = new RatingRecord(((r.getRate().subtract(init1)).divide(init1,2)).multiply(new BigDecimal(100)), r.getTime(), r.getVolume());
						meaningfullRecordsAsset1.add(rr);
					}

				for (RatingRecord r : allRecordsAsset2)
					if (DateUtil.isWithinRange(r.getTime(), date1, date2)) {
						if (first2) {
							init2 = r.getRate();
							first2 = false;
						}
						RatingRecord rr = new RatingRecord(((r.getRate().subtract(init2)).divide(init2,2)).multiply(new BigDecimal(100)), r.getTime(), r.getVolume());
						meaningfullRecordsAsset2.add(rr);
					}

			}

		});

	}

	/**
	 * adds rating records to the series
	 * @param meaningfull list of rating records
	 * @param period specific period of time
	 * @return returns series of meaningful rating records
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
	 * closes the window
	 * @param event mouse event that triggers closing
	 */
	@FXML
	public void closePopUp(MouseEvent event) {
		Stage stage = (Stage) closePopUpIco.getScene().getWindow();
		stage.close();
	}

	/**
	 * initializes the pop with data about assets
	 * @param asset1 self-explainable
	 * @param asset2 
	 */
	public void initData(Asset asset1, Asset asset2) {
		this.asset1 = asset1;
		this.asset2 = asset2;

		Date day1 = DateUtil.getMidnightYesterday();
		Date day2 = DateUtil.getMidnightTonight();
		List<RatingRecord> all1 = asset1.getRatingRecords();
		List<RatingRecord> all2 = asset2.getRatingRecords();

		List<RatingRecord> meaningfull1 = new ArrayList<>();
		List<RatingRecord> meaningfull2 = new ArrayList<>();

		boolean first1 = true, first2 = true;
		BigDecimal init1 = BigDecimal.ONE;
		BigDecimal init2 = BigDecimal.ONE;

		for (RatingRecord r : all1)
			if (DateUtil.isWithinRange(r.getTime(), day1, day2)) {
				if (first1) {
					init1 = r.getRate();
					first1 = false;
				}
				RatingRecord rr = new RatingRecord(((r.getRate().subtract(init1)).divide(init1,2)).multiply(new BigDecimal(100)), r.getTime(), r.getVolume());
				meaningfull1.add(rr);
			}

		for (RatingRecord r : all2)
			if (DateUtil.isWithinRange(r.getTime(), day1, day2)) {
				if (first2) {
					init2 = r.getRate();
					first2 = false;
				}
				RatingRecord rr = new RatingRecord(((r.getRate().subtract(init2)).divide(init2,2)).multiply(new BigDecimal(100)), r.getTime(), r.getVolume());
				meaningfull2.add(rr);
			}

		// ------------------------------------------------------------------
		ObservableList<XYChart.Series<Date, Number>> series = FXCollections.observableArrayList();
		ObservableList<XYChart.Data<Date, Number>> series2Data1 = getSeries(meaningfull1, "1D");
		ObservableList<XYChart.Data<Date, Number>> series2Data2 = getSeries(meaningfull2, "1D");

		series.add(new XYChart.Series<>("Quotings today(%): " + asset1.getName(), series2Data1));
		series.add(new XYChart.Series<>("Quotings today(%): " + asset2.getName(), series2Data2));

		NumberAxis numberAxis = new NumberAxis();
		DateAxis dateAxis = new DateAxis();
		lineChart = new LineChart<>(dateAxis, numberAxis, series);
		lineChart.relocate(250, 56);
		Pane root = (Pane) closePopUpIco.getScene().getRoot();
		root.getChildren().add(lineChart);
		lineChart.setCreateSymbols(false);
	}

	/**
	 * refreshes the chart
	 */
	@FXML
	public void refreshChart() {

	}

}
