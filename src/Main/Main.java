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

        LocalTime end = LocalTime.now();
        int startHour = start.getHour();
        int startMinute = start.getMinute();
        int startSecond = start.getSecond();
        int endHour = end.getHour();
        int endMinute = end.getMinute();
        int endSecond = end.getSecond();
        if (endSecond < startSecond) {
            endMinute = endMinute - 1;
            endSecond = endSecond + 60;
        }
        int h = endHour - startHour;
        int m = endMinute - startMinute;
        int s = endSecond - startSecond;
        if (s == 60) {
            s = 0;
            m = m + 1;
        }
        System.out.println("Start time: " + start);
        System.out.println("End time: " + end);
        System.out.println("Program running time: " + h +":"+ m +":" + s);
        System.out.println("DONE!");
    }
}
