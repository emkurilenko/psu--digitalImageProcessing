package kurilenko.proccess;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Noise {
    public static BufferedImage saltPepperNoise(BufferedImage img, double procent) {
        BufferedImage noise_img = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        Random random = new Random();
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                double rand = random.nextDouble();
                if(rand <= procent) {
                    if (random.nextBoolean()) {
                        noise_img.setRGB(i, j, Color.BLACK.getRGB());
                        continue;
                    }else{
                        noise_img.setRGB(i, j, Color.WHITE.getRGB());
                        continue;
                    }
                }
                noise_img.setRGB(i, j, img.getRGB(i, j));
            }
        }
        return noise_img;
    }

    public static BufferedImage gaussianNoise(BufferedImage img, double mean, double std_variance) {
        BufferedImage noise_img = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        Random random = new Random();
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                int gray = (int)(random.nextGaussian() * std_variance + mean);
                gray += img.getRGB(i, j) & 0xff;
                if (gray < 0) gray = 0;
                if (gray > 255) gray = 255;
                int rgb = ((gray * 256) + gray) * 256 + gray;

                noise_img.setRGB(i, j, rgb);
            }
        }
        return noise_img;
    }
}
