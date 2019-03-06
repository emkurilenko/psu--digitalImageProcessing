package kurilenko.proccess;

import java.awt.image.BufferedImage;

public abstract class Filter {
    private int size;

    public Filter(int size) {
        if ((size % 2 == 0) || (size < 3)) {
            size = 3;
        }
        this.size = size;
    }

    public int getFilterSize() {
        return size;
    }


    public int[] getArray(BufferedImage image, int x, int y) {
        int[] n;
        int h = image.getHeight();
        int w = image.getWidth();
        int xmin, xmax, ymin, ymax;
        xmin = x - size / 2;
        xmax = x + size / 2;
        ymin = y - size / 2;
        ymax = y + size / 2;

        if (xmin < 0)
            xmin = 0;
        if (xmax > (w - 1))
            xmax = w - 1;
        if (ymin < 0)
            ymin = 0;
        if (ymax > (h - 1))
            ymax = h - 1;
        int nsize = (xmax - xmin + 1) * (ymax - ymin + 1);
        n = new int[nsize];
        int k = 0;
        for (int i = xmin; i <= xmax; i++)
            for (int j = ymin; j <= ymax; j++) {
                n[k] = image.getRGB(i, j);
                k++;
            }
        return n;
    }

}
