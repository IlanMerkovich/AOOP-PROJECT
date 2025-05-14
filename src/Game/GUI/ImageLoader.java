package Game.GUI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ImageLoader {
    private static final String IMAGE_FOLDER = "../Resources/Images/";

    public static ImageIcon load(String fileName) {
        URL imageUrl = ImageLoader.class.getResource(IMAGE_FOLDER + fileName);
        if (imageUrl != null) {
            return new ImageIcon(imageUrl);
        }
        else {
            System.err.println("Image not found: " + IMAGE_FOLDER + fileName);
            return null;
        }
    }

    public static ImageIcon load(String fileName, int width, int height) {
        ImageIcon original = load(fileName);
        if (original == null)
            return null;
        Image scaledImage = original.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

}
