package com.simplecodec;

import com.simplecodec.utils.Assert;
import java.io.*;
import java.util.*;

public class PPMImageHandler {
    
    /**
     * Reads a PPM file and converts it to a 3D double array.
     * Supports P3 (ASCII) format only
     * 
     * @param filePath Path to the PPM file
     * @return 3D array with dimensions [height][width][3] with RGB values normalized to 0.0-1.0
     * @throws IOException If there's an error reading the file
     */
    public double[][][] readPPM(String filePath) throws IOException {
        File file = new File(filePath);
        try (FileInputStream fis = new FileInputStream(file)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            
            // Read the PPM header
            String magic = reader.readLine();
            Assert.isTrue(magic.equals("P3"));

            // Skip comments
            String line;
            do {
                line = reader.readLine();
            } while (line.startsWith("#"));
            
            // Parse width and height
            String[] dimensions = line.split("\\s+");
            int width = Integer.parseInt(dimensions[0]);
            int height = Integer.parseInt(dimensions[1]);
            
            // Read max value
            String maxValStr = reader.readLine();
            int maxVal = Integer.parseInt(maxValStr);
            
            double[][][] image = new double[height][width][3];
            
            // For P3 format (ASCII)
            Scanner scanner = new Scanner(reader);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    for (int c = 0; c < 3; c++) {
                        if (scanner.hasNextInt()) {
                            int value = scanner.nextInt();
                            image[y][x][c] = (double)value / maxVal;
                        }
                    }
                }
            }

            return image;
        }
    }
    
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
    
    /**
     * Writes a 3D double array to a PPM file in P6 (binary) format.
     * 
     * @param image 3D array with dimensions [height][width][3] with RGB values normalized to 0.0-1.0
     * @param filePath Path where the PPM file should be written
     * @throws IOException If there's an error writing the file
     */
    public void writeBinaryPPM(double[][][] image, String filePath) throws IOException {
        int height = image.length;
        int width = image[0].length;
        int maxVal = 255;
        
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            // Write the PPM header
            String header = "P6\n" + width + " " + height + "\n" + maxVal + "\n";
            fos.write(header.getBytes());
            
            // Write the binary pixel data
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    for (int c = 0; c < 3; c++) {
                        int value = (int)(image[y][x][c] * maxVal);
                        value = Math.max(0, Math.min(maxVal, value)); // Clamp value
                        fos.write(value);
                    }
                }
            }
        }
    }
}
