package com.simplecodec;

import com.simplecodec.ColorSpaceConverter;
import com.simplecodec.PPMImageHandler;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Main {

    public static void main(String[] args) {
        try {
            // Example file path - replace with an actual PPM file path
            String filePath = "images/red_half.ppm.txt";
            
            // Read the PPM file
            PPMImageHandler handler = new PPMImageHandler();
            double[][][] rgbImage = handler.readPPM(filePath);
            
            System.out.println("Image loaded: " + rgbImage[0].length + "x" + rgbImage.length);
            
            // Convert RGB to YUV
            ColorSpaceConverter converter = new ColorSpaceConverter();
            double[][][] yuvImage = converter.rgbToYuv(rgbImage);
            System.out.println("Converted to YUV");
            
            // Convert back to RGB
            double[][][] convertedRgbImage = converter.yuvToRgb(yuvImage);
            System.out.println("Converted back to RGB");
            
            // Display the original and converted images
            displayImages(rgbImage, convertedRgbImage);
            
            // Optionally save the converted image
            handler.writePPM(convertedRgbImage, "converted.ppm");
            System.out.println("Converted image saved to converted.ppm");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * twiceConverted is original converted to YUV and back to RGB
     * @param original
     * @param twiceConverted
     */
    private static void displayImages(double[][][] original, double[][][] twiceConverted) {
        int height = original.length;
        int width = original[0].length;
        
        BufferedImage origImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        BufferedImage convImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r1 = clip((int)(original[y][x][0] * 255));
                int g1 = clip((int)(original[y][x][1] * 255));
                int b1 = clip((int)(original[y][x][2] * 255));
                origImg.setRGB(x, y, (r1 << 16) | (g1 << 8) | b1);
                
                int r2 = clip((int)(twiceConverted[y][x][0] * 255));
                int g2 = clip((int)(twiceConverted[y][x][1] * 255));
                int b2 = clip((int)(twiceConverted[y][x][2] * 255));
                convImg.setRGB(x, y, (r2 << 16) | (g2 << 8) | b2);
            }
        }
        
        JFrame frame = new JFrame("RGB-YUV-RGB Conversion");
        frame.setLayout(new GridLayout(1, 2));
        frame.add(new JLabel(new ImageIcon(origImg)));
        frame.add(new JLabel(new ImageIcon(convImg)));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
    private static int clip(int value) {
        return Math.max(0, Math.min(255, value));
    }
}
