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
                int red = (int) Math.round(color.getRed() + step);
                int green = (int) Math.round(color.getGreen() + step);
                int blue = (int) Math.round(color.getBlue() + step);
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

    public static BufferedImage improveCorrectionImage(BufferedImage originGrayImage) {
        int[] histogram = ImageToBinary.imageHistogram(originGrayImage);
        List<Integer> listHistogramItem = Arrays.stream(histogram).boxed().collect(Collectors.toList());

        BufferedImage imageImprove = new BufferedImage(originGrayImage.getWidth(), originGrayImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        int maxPos = listHistogramItem.indexOf(Collections.max(listHistogramItem));
        int minPos = listHistogramItem.indexOf(Collections.min(listHistogramItem));

        int max = Collections.max(listHistogramItem);
        int min = Collections.min(listHistogramItem);

        double coefficient = 256.0 / (maxPos - minPos);

        for (int i = 0; i < originGrayImage.getWidth(); i++) {
            for (int j = 0; j < originGrayImage.getHeight(); j++) {
                int oldPixel = new Color(originGrayImage.getRGB(i, j)).getRed();
                int newPixel = (int) ((oldPixel - minPos) * coefficient);
                if (newPixel < 0) newPixel = 0;
                if (newPixel > 255) newPixel = 255;
                imageImprove.setRGB(i, j, new Color(newPixel, newPixel, newPixel).getRGB());
            }
        }

        return imageImprove;
    }

    public static BufferedImage addContrastFilter(BufferedImage grayImage, int correction) {
        BufferedImage contrastImage = new BufferedImage(grayImage.getWidth(),grayImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        int imageRows = grayImage.getHeight();
        int imageCols = grayImage.getWidth();

        int lAB = 0;

        int b[] = new int[256];

        for (int i = 0; i < imageRows; i++) {
            for (int j = 0; j < imageCols; j++) {
                lAB += new Color(grayImage.getRGB(j, i)).getRed();
            }
        }

        lAB /= imageRows * imageCols;

        double k = 1.0 + correction / 100.0;

        for (int i = 0; i < 256; i++) {
            int delta = (int) i - lAB;
            int temp = (int) (lAB + k * delta);

            if (temp < 0)
                temp = 0;
            if (temp >= 255)
                temp = 255;
            b[i] = temp;
        }

        for (int i = 0; i < imageRows; i++) {
            for (int j = 0; j < imageCols; j++) {
                Color c = new Color(grayImage.getRGB(j, i));
                contrastImage.setRGB(j, i, b[c.getRed()]);
            }
        }
        return contrastImage;
    }
}
