package Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;

/**
 * @author QuariX
 * @version 1.0
 * @since 17.0.1
 * @date 25.11.2021
 */

public class Main {
    public static void main(String[] args) {

        LocalTime start = LocalTime.now();
        int rad = 25;
        double[][] weights = GaussianBlur.getInstance().generateWeightMatrix(rad, Math.sqrt(rad));
        GaussianBlur.getInstance().printWeightedMatrixToFile(weights);

        BufferedImage answer = null;
        try {
            BufferedImage source_img = ImageIO.read(new File("src/ImgInput/21.png"));
            answer = GaussianBlur.getInstance().createGaussianImage(source_img, weights, rad);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assert answer != null;
            ImageIO.write(answer, "PNG", new File("src/ImgOutput/22.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        LocalTime finish = LocalTime.now();
        int startHour = start.getHour();
        int startMinute = start.getMinute();
        int startSecond = start.getSecond();
        int finishHour = finish.getHour();
        int finishMinute = finish.getMinute();
        int finishSecond = finish.getSecond();
        if (finishSecond < startSecond) {
            --finishMinute;
            finishSecond += 60;
        }
        if (finishMinute < startMinute) {
            --finishHour;
            finishMinute += 60;
        }

        int totalHour = finishHour - startHour;
        int totalMinute = finishMinute - startMinute;
        int totalSecond = finishSecond - startSecond;
        if (totalSecond == 60) {
            totalSecond = 0;
            ++totalMinute;
        }
        if (totalMinute == 60) {
            totalMinute = 0;
            ++totalHour;
        }

        System.out.println("Start time: " + start);
        System.out.println("End time: " + finish);
        System.out.println("Program running time: " + totalHour + ":" + totalMinute + ":" + totalSecond);
        System.out.println("DONE!");
    }
}
