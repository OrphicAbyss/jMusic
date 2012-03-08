package img;

import javax.swing.ImageIcon;

/**
 * Loads an image from the same resource location as this class
 */
public class LoadImage {
	public static ImageIcon load(String image){
		return new ImageIcon(LoadImage.class.getResource(image));
	}
}
