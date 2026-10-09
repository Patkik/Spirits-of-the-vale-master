package main;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class UtilityTool {
    public BufferedImage scaleImage(BufferedImage originalImage, int width, int height) {
        // Create a new BufferedImage with the desired dimensions
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Get the Graphics2D object
        Graphics2D g2 = scaledImage.createGraphics();

        // Enable sharp, high-quality scaling
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR); // For sharp scaling
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        // Draw the scaled image
        g2.drawImage(originalImage, 0, 0, width, height, null);

        // Dispose of the Graphics2D object to release resources
        g2.dispose();

        return scaledImage;
    }
}
