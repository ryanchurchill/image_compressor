package com.simplecodec;

import com.simplecodec.utils.Assert;
import java.io.*;
import java.util.*;

public class PPMImageHandler {
    
    /**
     * Writes a 3D double array to a PPM file in P3 (ASCII) format.
     * 
     * @param image 3D array with dimensions [height][width][3] with RGB values normalized to 0.0-1.0
     * @param filePath Path where the PPM file should be written
     * @throws IOException If there's an error writing the file
     */
    public void writePPM(double[][][] image, String filePath) throws IOException {
        int height = image.length;
        int width = image[0].length;
        int maxVal = 255;
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // Write the PPM header
            writer.println("P3");
            writer.println("# Created by ImageCompressor");
            writer.println(width + " " + height);
            writer.println(maxVal);
            
            // Write the pixel data
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    for (int c = 0; c < 3; c++) {
                        int value = (int)(image[y][x][c] * maxVal);
                        value = Math.max(0, Math.min(maxVal, value)); // Clamp value
                        writer.print(value + " ");
                    }
                    writer.print("  ");
                }
                writer.println();
            }
        }
    }
}
