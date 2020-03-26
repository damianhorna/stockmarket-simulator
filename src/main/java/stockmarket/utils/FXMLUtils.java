package stockmarket.utils;

import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

/**
 * fxml utils class
 * @author Damian Horna
 *
 */
public class FXMLUtils {

	/**
	 * loads the screen to the right hand side of the window
	 * @param path path to load the view from
	 * @return returns the pane or null
	 */
	public static Pane fxmlLoader(String path) {
		FXMLLoader loader = new FXMLLoader(FXMLUtils.class.getResource(path));
		loader.setResources(getResourceBundle());
		try {
			return loader.load();
		} catch (Exception e) {
			DialogUtils.errorDialog(e.getMessage());
		}
		return null; 
	}
	
	
	/**
	 * @return returns rsource bundle
	 */
	public static ResourceBundle getResourceBundle() {
		return ResourceBundle.getBundle("bundles.messages");
	}
}
