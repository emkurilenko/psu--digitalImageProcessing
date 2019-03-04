package kurilenko.proccess;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Utills {
    public static BufferedImage changeBrightnessS(BufferedImage origin, double step) {
        BufferedImage changeImage = new BufferedImage(origin.getWidth(), origin.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < origin.getWidth(); i++) {
            for (int j = 0; j < origin.getHeight(); j++) {
                Color color = new Color(origin.getRGB(i, j));
                int red = (int) Math.round(color.getRed() * step);
                int green = (int) Math.round(color.getGreen() * step);
                int blue = (int) Math.round(color.getBlue() * step);
                if (red < 0) red = 0;
                if (red > 255) red = 255;
                if (green < 0) green = 0;
                if (green > 255) green = 255;
                if (blue < 0) blue = 0;
                if (blue > 255) blue = 255;

                changeImage.setRGB(i, j, new Color(red, green, blue).getRGB());
            }
        }
        return changeImage;
    }

    public static BufferedImage improveImage(BufferedImage originGrayImage) {
        int[] histogram = ImageToBinary.imageHistogram(originGrayImage);
        List<Integer> listHistogramItem = Arrays.stream(histogram).boxed().collect(Collectors.toList());

        BufferedImage imageImprove = new BufferedImage(originGrayImage.getWidth(), originGrayImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        int maxPos = listHistogramItem.indexOf(Collections.max(listHistogramItem));
        int minPos = listHistogramItem.indexOf(Collections.min(listHistogramItem));

        int max = Collections.max(listHistogramItem);
        int min = Collections.min(listHistogramItem);

        for (int i = 0; i < originGrayImage.getHeight(); i++) {
            for (int j = 0; j < originGrayImage.getWidth(); j++) {
                int oldPixel = new Color(originGrayImage.getRGB(j, i)).getRed();
                int newPixel = ((oldPixel - min) / (max - min) * 255);
                if (newPixel < 0) newPixel = 0;
                if (newPixel > 255) newPixel = 255;
                imageImprove.setRGB(j, i, new Color(newPixel, newPixel, newPixel).getRGB());
            }
        }

        return imageImprove;
    }
}
