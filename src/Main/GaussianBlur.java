package Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 @author QuariX
  * @version 1.0
 * @since 17.0.1
 * @date 25.11.2021
 */

public class GaussianBlur {
    private GaussianBlur() {}

    private static GaussianBlur blur;
    public static GaussianBlur getInstance() {
        if (blur == null) {
            blur = new GaussianBlur();
        }
        return blur;
    }

    public double[][] generateWeightMatrix(int radius, double variance) {
        double[][] weights = new double[radius][radius];
        double summation = 0;
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = gaussianModel(i - radius / 2.0, j - radius / 2.0, variance);
                summation += weights[i][j];

                System.out.print(weights[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("-----------------------------------");

        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights.length; j++) {
                weights[i][j] /= summation;
            }
        }

        return weights;
    }

    public  void printWeightedMatrixToFile(double[][] weightMatrix) {
        BufferedImage img = new BufferedImage(weightMatrix.length, weightMatrix.length, BufferedImage.TYPE_INT_RGB);

        double max = 0;
        for (int i = 0; i < weightMatrix.length; i++) {
            for (int j = 0; j < weightMatrix.length; j++) {
                max = Math.max(max, weightMatrix[i][j]);
            }
        }

        for (int i = 0; i < weightMatrix.length; i++) {
            for (int j = 0; j < weightMatrix.length; j++) {
                int grayScaleValue = (int) ((weightMatrix[i][j] / max) * 255d);
                img.setRGB(i, j, new Color(grayScaleValue,grayScaleValue, grayScaleValue).getRGB());
            }
        }

        try {
            ImageIO.write(img, "PNG", new File("src/GaussGraph/gaussian-weights-graphed.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage createGaussianImage(BufferedImage source_image, double[][] weights, int radius) {
        System.out.println("Working...");
        BufferedImage answer = new BufferedImage(source_image.getWidth(), source_image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < source_image.getWidth(); x++) {
            for (int y = 0; y < source_image.getHeight(); y++) {

                double[][] distributedColorRed = new double[radius][radius];
                double[][] distributedColorGreen = new double[radius][radius];
                double[][] distributedColorBlue = new double[radius][radius];

                for (int weightX = 0; weightX < weights.length; weightX++) {
                    for (int weightY = 0; weightY < weights[weightX].length; weightY++) {

                        int sampleX = x + weightX - (weights.length / 2);
                        int sampleY = y + weightY - (weights.length / 2);

                        if (sampleX > source_image.getWidth() - 1) {
                            int error_offset = sampleX - source_image.getWidth() - 1;
                            sampleX = source_image.getWidth() - radius - error_offset;
                        }

                        if (sampleY > source_image.getHeight() - 1) {
                            int error_offset = sampleY - source_image.getHeight() - 1;
                            sampleY = source_image.getHeight() - radius - error_offset;
                        }

                        if (sampleX < 0) {
                            sampleX = Math.abs(sampleX);
                        }
                        if (sampleY < 0) {
                            sampleY = Math.abs(sampleY);
                        }

                        double currentWeight = weights[weightX][weightY];

                        Color sampledColor = new Color(source_image.getRGB(sampleX, sampleY));

                        distributedColorRed[weightX][weightY] = currentWeight * sampledColor.getRed();
                        distributedColorGreen[weightX][weightY] = currentWeight * sampledColor.getGreen();
                        distributedColorBlue[weightX][weightY] = currentWeight * sampledColor.getBlue();
                    }
                }
                answer.setRGB(x, y, new Color(getWeightedColorValue(distributedColorRed),
                        getWeightedColorValue(distributedColorGreen),
                        getWeightedColorValue(distributedColorBlue)).getRGB());
            }
        }
        return answer;
    }

    private int getWeightedColorValue(double[][] weightedColor) {
        double summation = 0;

        for (int i = 0; i < weightedColor.length; i++) {
            for (int j = 0; j < weightedColor[i].length; j++) {
                summation += weightedColor[i][j];
            }
        }
        return (int) summation;
    }

    public double gaussianModel(double x, double y, double variance) {
        return (1 / (2 * Math.PI * Math.pow(variance, 2))
                * Math.exp(-(Math.pow(x, 2) + Math.pow(y, 2)) / (2 * Math.pow(variance, 2))));
    }
}