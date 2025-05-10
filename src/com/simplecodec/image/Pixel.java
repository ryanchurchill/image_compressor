package com.simplecodec.image;

import com.simplecodec.utils.Assert;

/**
 * Unused.
 * Each pixel of an image. Could be RGB or YUV
 */
public class Pixel {
    ColorSpace colorSpace;
    double val1; // R or Y
    double val2; // G or U
    double val3; // B or V

    private Pixel(ColorSpace colorSpace, double val1, double val2, double val3)
    {
        this.colorSpace = colorSpace;
        this.val1 = val1;
        this.val2 = val2;
        this.val3 = val3;
    }

    // RGB getters
    public double r()
    {
        assertColorSpace(ColorSpace.RGB);
        return val1;
    }
    public double g()
    {
        assertColorSpace(ColorSpace.RGB);
        return val2;
    }
    public double b()
    {
        assertColorSpace(ColorSpace.RGB);
        return val3;
    }

    // YUV getters
    public double y()
    {
        assertColorSpace(ColorSpace.YUV);
        return val1;
    }
    public double u()
    {
        assertColorSpace(ColorSpace.YUV);
        return val2;
    }
    public double v()
    {
        assertColorSpace(ColorSpace.YUV);
        return val3;
    }

    private void assertColorSpace(ColorSpace colorSpace)
    {
        Assert.isTrue(this.colorSpace == colorSpace, "Unexpected color space");
    }

    public static Pixel createRgbPixel(double r, double g, double b)
    {
        return new Pixel(ColorSpace.RGB, r, g, b);
    }

    public static Pixel createYuvPixel(double y, double u, double v)
    {
        return new Pixel(ColorSpace.YUV, y, u, v);
    }
}
