package kurilenko.proccess;

import java.awt.image.BufferedImage;

public class LowFilter extends Filter {
    private int lowThreeOnThree[][] = {{3,3,3}, {3,3,3},{3,3,3}};

    public LowFilter(int size) {
        super(size);
    }

    public void filter(BufferedImage origin, BufferedImage newImage) {

    }
}
