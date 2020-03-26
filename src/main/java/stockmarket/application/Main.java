package stockmarket.application;

import java.util.concurrent.ThreadLocalRandom;

//import java.util.Locale;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import stockmarket.simulation.Simulation;
import stockmarket.utils.FXMLUtils;
import stockmarket.utils.RandomDataGenerator;




/**
 * Date: Jan 2-2018
 * An application that simulates stock markets.
 * @author Damian Horna, 132240
 * @version 1.0
 */
public class Main extends Application {

	
	
	/**
	 * x position of a window
	 */
	private double xOffset = 0;
	/**
	 * y position of a window
	 */
	private double yOffset = 0;

	/**
	 * first screen to load
	 */
	private static final String MAIN_SCREEN_FXML = "/view/MainScreen.fxml";

	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(ThreadLocalRandom.current().nextInt(0, 1));

		launch(args);
	}

	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		/**
		 * an instance of random data generator
		 */
		RandomDataGenerator dataGenerator = new RandomDataGenerator();
		dataGenerator.initialize();
		
		/**
		 * simulation thread
		 */
		Thread t = new Thread(new Simulation());
		t.start();

		primaryStage.initStyle(StageStyle.UNDECORATED);
		
		/**
		 * used to load the main screen
		 */
		Pane root = FXMLUtils.fxmlLoader(MAIN_SCREEN_FXML);
		root.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
		root.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				primaryStage.setX(event.getScreenX() - xOffset);
				primaryStage.setY(event.getScreenY() - yOffset);
			}
		});

		/**
		 * setting root on a scene
		 */
		Scene scene = new Scene(root);

		primaryStage.setTitle("stockMarket Simulator");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

}