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
import stockmarket.model.Gambler;
import stockmarket.model.RatingRecord;
import stockmarket.model.RawMaterial;
import stockmarket.model.RawMaterialsMarket;
import stockmarket.utils.BlurUtils;
import stockmarket.utils.DateAxis;
import stockmarket.utils.DateUtil;
import stockmarket.utils.PseudoDB;

/**
 * controller responsible for pop up with information about raw material
 * @author Damian Horna
 *
 */
public class ShowRawMaterialController {

	/**
	 * used for closing the window
	 */
	@FXML
	private MaterialDesignIconView closePopUpIco;

	/**
	 * name of raw material
	 */
	@FXML
	private Label materialName;
	/**
	 * current rate of raw material
	 */
	@FXML
	private Label materialCurrentRate;
	/**
	 * initial rate of raw material
	 */
	@FXML
	private Label materialInitialRate;
	/**
	 * maximal rate of raw material
	 */
	@FXML
	private Label materialMaximalRate;
	/**
	 * minimal rate of raw material
	 */
	@FXML
	private Label materialMinimalRate;
	/**
	 * the unit of raw material
	 */
	@FXML
	private Label materialUnit;
	/**
	 * currency of raw material
	 */
	@FXML
	private Label materialCurrency;
	/**
	 * stack pane for displaying error messages
	 */
	@FXML
	private StackPane stackPane;
	/**
	 * informs whether raw material is quoted or not
	 */
	@FXML
	private Label materialQuoted;
	/**
	 * used for refreshing the chart
	 */
	@FXML
	private JFXButton refreshButton;
	
	/**
	 * icon for deleting raw material
	 */
	@FXML
	private MaterialDesignIconView deleteIcon;

	/**
	 * period selection
	 */
	@FXML
	private JFXComboBox<String> periodComboBox;

	/**
	 * raw material to show info about
	 */
	private RawMaterial material;
	
	/**
	 * table of raw materials
	 */
	TableView<RawMaterial> rawMaterialsTable;
	
	/**
	 * line chart with material ratings
	 */
	private LineChart<Date, Number> lineChart;

	/**
	 * initializes the pop up with data
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
					allRecords = material.getRatingRecords();
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
					allRecords = material.getRatingRecords();
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
					allRecords = material.getRatingRecords();
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
					allRecords = material.getRatingRecords();
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
	 * gets all raw materials available in the simulation
	 * @param rmm raw materials market
	 * @return returns list of raw materials
	 */
	private ObservableList<RawMaterial> getMaterials(RawMaterialsMarket rmm) {
		ObservableList<RawMaterial> materials = FXCollections.observableArrayList(
				rmm.getQuotedRawMaterials().stream().filter(c -> !c.getName().equals("USD")).collect(Collectors.toList()));
		System.out.println(materials);
		return materials;
	}
	
	
	/**
	 * gets the series from rating records
	 * @param meaningfull
	 * @param period
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
	 * @param theMaterial
	 * @param materials
	 */
	public void initData(RawMaterial theMaterial, TableView<RawMaterial> materials) {
		this.material = theMaterial;
		this.rawMaterialsTable = materials;	
		materialName.setText(material.getName());
		if (material.isQuoted()) {
			materialQuoted.setText("");
		}
		materialCurrentRate.setText(material.getCurrentRate().toString());
		materialInitialRate.setText(material.getInitialRate().toString());
		materialMaximalRate.setText(material.getMaximalRate().toString());
		materialMinimalRate.setText(material.getMinimalRate().toString());
		materialUnit.setText(material.getUnit().toString());
		materialCurrency.setText(material.getCurrency().getName());
		

		Date day1 = DateUtil.getMidnightYesterday();
		Date day2 = DateUtil.getMidnightTonight();
		List<RatingRecord> all = material.getRatingRecords();
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
	 * deletes raw material
	 * @param event
	 */
	@FXML
	void deleteRawMaterial(MouseEvent event) {
		synchronized(PseudoDB.rawMaterialsLock) {
			Stage stage = (Stage) deleteIcon.getScene().getWindow();
			BlurUtils.unblur();
			// delete gamblers' assets
			for (Gambler g : material.getOwners()) {
				g.setPossessions(g.getPossessions().stream().filter(a -> !a.getAssetType().getName().equals(material.getName()))
						.collect(Collectors.toList()));
			}
			// delete rmaterial
			PseudoDB.getRawMaterials().remove(material);
			RawMaterialsMarket rmm = PseudoDB.getRawMaterialsMarkets().stream().filter(e->e.getQuotedRawMaterials().contains(material)).collect(Collectors.toList()).get(0);
			rmm.getQuotedRawMaterials().remove(material);
			
			
			PseudoDB.setOrders(PseudoDB.getOrders().stream().filter(o->o.getAsset().getName()!=material.getName()).collect(Collectors.toList()));
			try {
				rawMaterialsTable.setItems(null);
				rawMaterialsTable.setItems(getMaterials(rmm));
			}catch(Exception e) {
				//access from assets tab
			}
			stage.close();
		}
		
	}
	
	/**
	 * closes the pop up window
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
