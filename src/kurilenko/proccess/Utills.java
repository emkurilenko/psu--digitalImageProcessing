package kurilenko.proccess;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Utills {
    public static BufferedImage changeBrightnessS(BufferedImage origin, double step) {

        BufferedImage changeImage = new BufferedImage(origin.getWidth(), origin.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < origin.getWidth(); i++) {
            for (int j = 0; j < origin.getHeight(); j++) {
                Color color = new Color(origin.getRGB(i, j));
                int red = (int) (color.getRed() * step);
                int green = (int) (color.getGreen() * step);
                int blue = (int) (color.getBlue() * step);
                int alpha = (int) (color.getAlpha() * step);
                if (red < 0) red = 0;
                if (red > 255) red = 255;
                if (green < 0) green = 0;
                if (green > 255) green = 255;
                if (blue < 0) blue = 0;
                if (blue > 255) blue = 255;
                if (alpha < 0) alpha = 0;
                if (alpha > 255) alpha = 255;


                changeImage.setRGB(i, j, new Color(red, green, blue, alpha).getRGB());
            }
        }
        return changeImage;
    }

    public static BufferedImage improveCorrectionImage(BufferedImage originGrayImage) {

        int source[][] = new int[originGrayImage.getWidth()][originGrayImage.getHeight()];

        for (int i = 0; i < originGrayImage.getWidth(); i++) {
            for (int j = 0; j < originGrayImage.getHeight(); j++) {
                source[i][j] = new Color(originGrayImage.getRGB(i,j)).getRed();
            }
        }

        Histogram histogram = new Histogram(source);
        int min = histogram.min();
        int max = histogram.max();

        BufferedImage imageImprove = new BufferedImage(originGrayImage.getWidth(), originGrayImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        double coefficient = 255.0 / (max - min);

        for (int i = 0; i < originGrayImage.getWidth(); i++) {
            for (int j = 0; j < originGrayImage.getHeight(); j++) {
                int oldPixel = new Color(originGrayImage.getRGB(i, j)).getRed();
                int newPixel = (int) ((oldPixel - min) * coefficient);
                imageImprove.setRGB(i, j, new Color(newPixel, newPixel, newPixel).getRGB());
            }
        }
        return imageImprove;
    }

}
