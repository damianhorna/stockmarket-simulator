package stockmarket.utils;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import stockmarket.model.Investor;

/**
 * pop up utils class
 * @author Damian Horna
 *
 */
public class PopUpUtils {

	/**
	 * y offset of a window
	 */
	private static double yOffset = 0;
	/**
	 * x offset of a window
	 */
	private static double xOffset = 0;
	
	/**
	 * shows pop up
	 * @param closeIcon close icon to get the root
	 * @param path path for pop up
	 */
	public static void showPopUp(MaterialDesignIconView closeIcon, String path) {
		Stage stage = new Stage(StageStyle.UNDECORATED);

		Parent root = closeIcon.getScene().getRoot();
		BlurUtils.blur(root);

		Pane pane = FXMLUtils.fxmlLoader(path);
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

		stage.setScene(new Scene(pane));
		stage.show();
	}

	/**
	 * shows info on selected item from the table
	 * @param closeIcon close icon to grab the root
	 * @param selectedItem seleted item
	 */
	public static void showInfoAbout(MaterialDesignIconView closeIcon, Investor selectedItem) {
		Stage stage = new Stage(StageStyle.UNDECORATED);

		Parent root = closeIcon.getScene().getRoot();
		BlurUtils.blur(root);

		Pane pane = FXMLUtils.fxmlLoader("/view/outline/ShowInvestor.fxml");
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

		stage.setScene(new Scene(pane));
		stage.show();
	}

}
