package kurilenko.proccess;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageToGray {
    public static BufferedImage convertToGrayImage(BufferedImage oldImage) {
        BufferedImage convertImage = new BufferedImage(oldImage.getWidth(), oldImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < oldImage.getHeight(); i++) {
            for (int j = 0; j < oldImage.getWidth(); j++) {
                Color c = new Color(oldImage.getRGB(j, i));
                int red = (int) (c.getRed() * 0.3);
                int green = (int) (c.getGreen() * 0.59);
                int blue = (int) (c.getBlue() * 0.11);

                int sum = red + green + blue;

                Color newColor = new Color(sum, sum, sum);

                convertImage.setRGB(j,i,newColor.getRGB());
            }
        }
        return convertImage;
    }
}
