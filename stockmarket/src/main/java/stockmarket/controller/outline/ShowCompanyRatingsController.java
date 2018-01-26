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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import stockmarket.model.RatingRecord;
import stockmarket.model.Share;
import stockmarket.utils.BlurUtils;
import stockmarket.utils.DateAxis;
import stockmarket.utils.DateUtil;

/**
 * controller responsible for pop up that shows rating records of a given company
 * @author Damian Horna
 *
 */
public class ShowCompanyRatingsController {

	/**
	 * open rate of share (session)
	 */
	@FXML
	private Label ratOpen;

	/**
	 * name of share
	 */
	@FXML
	private Label ratName;

	/**
	 * the highest rate in session
	 */
	@FXML
	private Label ratHigh;

	/**
	 * the lowest rate in session
	 */
	@FXML
	private Label ratLow;

	/**
	 * last rate of company
	 */
	@FXML
	private Label ratLast;

	/**
	 * the change within session
	 */
	@FXML
	private Label ratChange;

	/**
	 * the percentage change
	 */
	@FXML
	private Label ratChangePct;

	/**
	 * all time low rate of share
	 */
	@FXML
	private Label ratAllTimeLow;

	/**
	 * all time high of share
	 */
	@FXML
	private Label ratAllTimeHigh;

	/**
	 * refresh button
	 */
	@FXML
	private JFXButton refreshButton;

	/**
	 * period selection
	 */
	@FXML
	private JFXComboBox<String> periodComboBox;

	/**
	 * icon that enables closing the application
	 */
	@FXML
	private MaterialDesignIconView closePopUpIco;

	/**
	 * line chart
	 */
	private LineChart<Date, Number> lineChart;

	/**
	 * share
	 */
	private Share share;

	/**
	 * the source string: assetsTab - coming from assets
	 */
	private String source;

	/**
	 * initializes the pop up with data
	 * @param share
	 * @param src
	 */
	public void initData(Share share, String src) {
		this.source = src;
		this.share = share;
		ratName.setText(share.getName());
		ratOpen.setText(share.getInitialRate().toString());
		ratHigh.setText(share.getMaximalRate().toString());
		ratLow.setText(share.getMinimalRate().toString());
		ratLast.setText(share.getCurrentRate().toString());
		ratChange.setText(share.getChange().toString());
		ratChangePct.setText(share.getChangePct().multiply(new BigDecimal("100")).toString() + "%");
		ratAllTimeHigh.setText(share.getAllTimeHigh().toString());
		ratAllTimeLow.setText(share.getAllTimeLow().toString());

		Date day1 = DateUtil.getMidnightYesterday();
		Date day2 = DateUtil.getMidnightTonight();
		List<RatingRecord> all = share.getRatingRecords();
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
	 * gets meaningful series (within given period of time)
	 * @param meaningfull meaningful rating records
	 * @param period given period of time
	 * @return returns series
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
	 * initializes the pop up with data
	 */
	@FXML
	public void initialize() {

		periodComboBox.getItems().setAll("1D", "1W", "1M", "1Y");
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
					allRecords = share.getRatingRecords();
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
					allRecords = share.getRatingRecords();
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

					Date month1 = DateUtil.getLastMonth();
					Date month2 = DateUtil.getNextMonth();
					allRecords = share.getRatingRecords();
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
					allRecords = share.getRatingRecords();
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
	 * closes the window
	 * @param event 
	 */
	@FXML
	public void closePopUp(MouseEvent event) {
		Stage stage = (Stage) closePopUpIco.getScene().getWindow();
		if(this.source.equals("assetsTab")) {
			BlurUtils.unblur();
		}
		stage.close();
	}

	/**
	 * refreshes the chart, loads data again
	 * @param event
	 */
	@FXML
	public void refreshChart(ActionEvent event) {
		share.getRatingRecords();
		System.out.println(share.getRatingRecords());
	}

}
