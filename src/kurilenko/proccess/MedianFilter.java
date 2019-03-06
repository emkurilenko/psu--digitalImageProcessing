package kurilenko.proccess;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MedianFilter extends Filter{

    public MedianFilter(int s) {
        super(s);
    }

    public int median(int[] a) {
        List<Integer> list = Arrays.stream(a).boxed().collect(Collectors.toList());
        Collections.sort(list);

        if (list.size() % 2 == 1)
            return list.get(list.size() / 2);
        else
            return list.get(list.size() / 2) + list.get((list.size() / 2 - 1)) / 2;
    }


    public void filter(BufferedImage srcImage, BufferedImage dstImage) {
        int height = srcImage.getHeight();
        int width = srcImage.getWidth();

        int[] a;

        for (int k = 0; k < height; k++) {
            for (int j = 0; j < width; j++) {
                a = getArray(srcImage, j, k);
                int[] red, green, blue;
                red = new int[a.length];
                green = new int[a.length];
                blue = new int[a.length];

                for (int i = 0; i < a.length; i++) {
                    Color color = new Color(a[i]);
                    red[i] = color.getRed();
                    green[i] = color.getGreen();
                    blue[i] = color.getBlue();
                }

                int R = median(red);
                int G = median(green);
                int B = median(blue);

                int spixel = ImageToBinary.colorToRGB(0, R, G, B);
                dstImage.setRGB(j, k, spixel);
            }
        }
    }

    public static int[][] matrixFiltration(int width, int height, int[][] pixel, int N, double[][] matryx) {
        int k, m, gap = (int) (N / 2);
        int tmpH = height + 2 * gap, tmpW = width + 2 * gap;
        int[][] tmppixel = new int[tmpH][tmpW];
        int[][] newpixel = new int[height][width];

        for (int i = 0; i < gap; i++) {
            for (int j = 0; j < gap; j++) {
                tmppixel[i][tmpW - 1 - j] = pixel[0][0];
                tmppixel[i][tmpW - 1 - j] = pixel[0][width - 1];
                tmppixel[tmpH - 1 - i][tmpW - 1 - j] = pixel[height - 1][width - 1];
            }
        }

        for (int i = gap; i < tmpH - gap; i++) {
            for (int j = 0; j < gap; j++) {
                tmppixel[i][j] = pixel[i - gap][j];
                tmppixel[i][tmpW - 1 - j] = pixel[i - gap][width - 1 - j];
            }
        }

        for (int i = 0; i < gap; i++) {
            for (int j = gap; j < tmpW - gap; j++) {
                tmppixel[i][j] = pixel[i][j];
                tmppixel[tmpH - 1 - i][j] = pixel[height - 1 - i][j - gap];
            }
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tmppixel[i + gap][j + gap] = pixel[i][j];
            }
        }
        return  null;
    }

}
