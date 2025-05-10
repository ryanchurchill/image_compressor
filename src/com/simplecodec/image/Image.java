package com.simplecodec.image;

import com.simplecodec.utils.Assert;

import java.io.*;
import java.util.Scanner;

/**
 * Using normalized values in both color spaces:
 *   RGB: all channels 0.0-1.0
 *   YUV: Y in 0.0-1.0, U/V in -0.5 to 0.5
 */
public class Image {
    ColorSpace colorSpace;
    double[][] val1; // R or Y
    double[][] val2; // G or U
    double[][] val3; // B or V

    public int getHeight()
    {
        return val1.length;
    }

    public int getWidth()
    {
        return val1[0].length;
    }

    public double getR(int x, int y)
    {
        assertColorSpace(ColorSpace.RGB);
        return val1[y][x];
    }
    public double getG(int x, int y)
    {
        assertColorSpace(ColorSpace.RGB);
        return val2[y][x];
    }
    public double getB(int x, int y)
    {
        assertColorSpace(ColorSpace.RGB);
        return val3[y][x];
    }

    public void convertToYuv()
    {
        assertColorSpace(ColorSpace.RGB);

        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                double r = val1[y][x];
                double g = val2[y][x];
                double b = val3[y][x];

                val1[y][x] = 0.299 * r + 0.587 * g + 0.114 * b;      // Y
                val2[y][x] = -0.14713 * r - 0.28886 * g + 0.436 * b; // U
                val3[y][x] = 0.615 * r - 0.51499 * g - 0.10001 * b;  // V
            }
        }
        colorSpace = ColorSpace.YUV;
    }

    public void convertToRgb()
    {
        assertColorSpace(ColorSpace.YUV);

        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                double yVal = val1[y][x];
                double uVal = val2[y][x];
                double vVal = val3[y][x];

                val1[y][x] = clamp((yVal + 1.13983 * vVal), 0.0, 1.0);                  // R
                val2[y][x] = clamp((yVal - 0.39465 * uVal - 0.58060 * vVal), 0.0, 1.0); // G
                val3[y][x] = clamp((yVal + 2.03211 * uVal), 0.0, 1.0);                  // B
            }
        }

        colorSpace = ColorSpace.RGB;
    }

    public static Image createRgbFromPpm(String filePath) throws IOException
    {
        Image image = new Image();
        image.colorSpace = ColorSpace.RGB;

        File file = new File(filePath);
        try (FileInputStream fis = new FileInputStream(file)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            // Read the PPM header (first line)
            String magic = reader.readLine();
            Assert.isTrue(magic.equals("P3"));

            // Skip comments
            String line;
            do {
                line = reader.readLine();
            } while (line.startsWith("#"));

            // Parse width and height (written on second line)
            String[] dimensions = line.split("\\s+");
            int width = Integer.parseInt(dimensions[0]);
            int height = Integer.parseInt(dimensions[1]);

            // Parse max value (used later for normalization)
            String maxValStr = reader.readLine();
            int maxVal = Integer.parseInt(maxValStr);

            // parse the rest of the ASCII to get the normalized values and build the image arrays
            image.val1 = new double[height][width];
            image.val2 = new double[height][width];
            image.val3 = new double[height][width];

            Scanner scanner = new Scanner(reader);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    for (int c = 0; c < 3; c++) {
                        if (scanner.hasNextInt()) {
                            int value = scanner.nextInt();
                            double normalizedValue = (double)value / maxVal;

                            switch (c) {
                                case 0:
                                    image.val1[y][x] = normalizedValue;
                                    break;
                                case 1:
                                    image.val2[y][x] = normalizedValue;
                                    break;
                                case 2:
                                    image.val3[y][x] = normalizedValue;
                                    break;
                            }
                        }
                    }
                }
            }

            return image;
        }
    }

    public static Image copyImage(Image source) {
        if (source == null) return null;

        Image copy = new Image();
        copy.colorSpace = source.colorSpace;

        copy.val1 = copyArray(source.val1);
        copy.val2 = copyArray(source.val2);
        copy.val3 = copyArray(source.val3);

        return copy;
    }

    private static double[][] copyArray(double[][] source) {
        if (source == null) return null;

        double[][] copy = new double[source.length][];
        for (int i = 0; i < source.length; i++) {
            copy[i] = source[i].clone();
        }
        return copy;
    }

    private void assertColorSpace(ColorSpace colorSpace)
    {
        Assert.isTrue(colorSpace == this.colorSpace, "Unexpected color space");
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
