package stockmarket.utils;

import javafx.scene.Parent;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorAdjust;

/**
 * class that enables blurring the window
 * @author Damian Horna
 *
 */
public class BlurUtils {
	/**
	 * root of the window to blur
	 */
	private static Parent root;

	public static Parent getRoot() {
		return root;
	}

	public static void setRoot(Parent root) {
		BlurUtils.root = root;
	}
	/**
	 * blurs the screen
	 * @param root
	 */
	public static void blur(Parent root) {
		setRoot(root);
		getRoot().setEffect(new BoxBlur(2, 7, 7));
		ColorAdjust adj = new ColorAdjust(0, -0.9, -0.5, 0);
	    BoxBlur blur = new BoxBlur(2,7,7); 
	    adj.setInput(blur);
	    getRoot().setEffect(adj);
	}
	
	/**
	 * unblurs the screen
	 */
	public static void unblur() {
		getRoot().setEffect(new BoxBlur(0, 0, 0));
	}
}
