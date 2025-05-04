package com.simplecodec;

public class ColorSpaceConverter {
    
    /**
     * Converts RGB color space to YUV color space.
     * 
     * @param rgb 3D array with dimensions [height][width][3] representing RGB values (0.0-1.0)
     * @return 3D array with dimensions [height][width][3] representing YUV values
     */
    public double[][][] rgbToYuv(double[][][] rgb) {
        int height = rgb.length;
        int width = rgb[0].length;
        
        double[][][] yuv = new double[height][width][3];
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double r = rgb[y][x][0];
                double g = rgb[y][x][1];
                double b = rgb[y][x][2];
                
                // RGB to YUV conversion formula
                yuv[y][x][0] = 0.299 * r + 0.587 * g + 0.114 * b;                  // Y
                yuv[y][x][1] = -0.14713 * r - 0.28886 * g + 0.436 * b;             // U
                yuv[y][x][2] = 0.615 * r - 0.51499 * g - 0.10001 * b;              // V
            }
        }
        
        return yuv;
    }
    
    /**
     * Converts YUV color space to RGB color space.
     * 
     * @param yuv 3D array with dimensions [height][width][3] representing YUV values
     * @return 3D array with dimensions [height][width][3] representing RGB values (0.0-1.0)
     */
    public double[][][] yuvToRgb(double[][][] yuv) {
        int height = yuv.length;
        int width = yuv[0].length;
        
        double[][][] rgb = new double[height][width][3];
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double yVal = yuv[y][x][0];
                double u = yuv[y][x][1];
                double v = yuv[y][x][2];
                
                // YUV to RGB conversion formula
                rgb[y][x][0] = yVal + 1.13983 * v;                                // R
                rgb[y][x][1] = yVal - 0.39465 * u - 0.58060 * v;                  // G
                rgb[y][x][2] = yVal + 2.03211 * u;                                // B
                
                // Ensure RGB values stay in the 0.0-1.0 range
                rgb[y][x][0] = clamp(rgb[y][x][0], 0.0, 1.0);
                rgb[y][x][1] = clamp(rgb[y][x][1], 0.0, 1.0);
                rgb[y][x][2] = clamp(rgb[y][x][2], 0.0, 1.0);
            }
        }
        
        return rgb;
    }
    
    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
