package kurilenko.proccess;

public class Histogram {
    // The range of brightness values in the pixel array passed to the constructor
    private int max, min;

    // The array of count values for different brightness values
    private int [] hist;

    // The total number of values in the pixel array passed to the constructor
    private int tot;

    // Create a histogram from a given pixel array
    public Histogram( int[][] source ) {
        // Save the number of elements in the pixel array
        tot = source.length*source[0].length;

        // Determine the minimum and maximum brightness values in the array;
        max = min = source[0][0];
        for ( int x = 0; x < source.length; x++ ) {
            for ( int y = 0; y < source[0].length; y++ ) {
                min = Math.min(source[x][y],min);
                max = Math.max(source[x][y],max);
            }
        }

        // Create an array with one entry for each value between min and max
        // equal to the number of pixels in the image with the corresponding
        // brightness value
        hist = new int[max - min + 1];
        for ( int x = 0; x < source.length; x++ ) {
            for ( int y = 0; y < source[0].length; y++ ) {
                hist[ source[x][y] - min ] ++;
            }
        }
    }

    // Get the size of the range of values found in the pixel array
    public int range() {
        return hist.length;
    }

    // Get the sum of all of the counts in the histogram array
    public int total() {
        return tot;
    }

    // Get the count of the number of times a particular shade of brightness
    // occurs in the pixel array use to build the histogram.
    public int frequency( int shade ) {
        return hist[ shade - min ];
    }

    // Get the count of the number of times the most frequently found shade of
    // brightness occurs in the pixel array used to build the histogram
    public int maxFreq() {
        int max = hist[0];
        for ( int i = 1; i < hist.length; i++ ) {
            max = Math.max( max, hist[i] );
        }
        return max;
    }

    // Get the largest brightness value found in the source pixel array
    public int max() {
        return max;
    }

    // Get the smallest brightness value found in the source pixel array
    public int min() {
        return min;
    }

    // Eventually, this method will.........

    // Get the number of bits that would be required per bix to encode this
    // image if it were encoded using a Huffman code designed for this
    // particular image.
    public double huffmanSize( ) {
        return 8.0*tot/tot;
    }

    // change me!
    public int findMin(int[] weights, int distinct) {
        return 0;
    }

    // change me!
    public int extractMin(int[] weights, int distinct) {
        return 0;
    }

}