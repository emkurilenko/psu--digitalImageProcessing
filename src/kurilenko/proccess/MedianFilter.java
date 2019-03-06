package kurilenko.proccess;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MedianFilter {
    private int size; //size of the square filter

    public MedianFilter(int s) {
        if ((s%2 == 0)||(s < 3))
        {
            s = 3;
        }
        size = s;
    }

    public int getFilterSize() {
        return size;
    }

    public int median(int[] a) {
        List<Integer> list = Arrays.stream(a).boxed().collect(Collectors.toList());
        Collections.sort(list);

        if (list.size()%2 == 1)
            return list.get(list.size()/2);
        else
            return list.get(list.size()/2)+list.get((list.size()/2 -1))/2;
    }

    public int[] getArray(BufferedImage image, int x, int y){
        int[] n;
        int h = image.getHeight();
        int w = image.getWidth();
        int xmin, xmax, ymin, ymax;
        xmin = x - size/2;
        xmax = x + size/2;
        ymin = y - size/2;
        ymax = y + size/2;

        if (xmin < 0)
            xmin = 0;
        if (xmax > (w - 1))
            xmax = w - 1;
        if (ymin < 0)
            ymin = 0;
        if (ymax > (h - 1))
            ymax = h - 1;
        int nsize = (xmax-xmin+1)*(ymax-ymin+1);
        n = new int[nsize];
        int k = 0;
        for (int i = xmin; i <= xmax; i++)
            for (int j = ymin; j <= ymax; j++){
                n[k] = image.getRGB(i, j); //get pixel value
                k++;
            }
        return n;
    }

    public void filter(BufferedImage srcImage, BufferedImage dstImage) {
        int height = srcImage.getHeight();
        int width = srcImage.getWidth();

        int[] a;

        for (int k = 0; k < height; k++){
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

                int spixel = ImageToBinary.colorToRGB(0,R,G,B);
                dstImage.setRGB(j, k, spixel);
            }
        }
    }

}
