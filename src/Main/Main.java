package Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author QuariX
 * @version 1.0
 * @since 17.0.1
 * @date 25.11.2021
 */

public class Main {
    public static void main(String[] args) {

        int rad = 25;
        double[][] weights = GaussianBlur.getInstance().generateWeightMatrix(rad, Math.sqrt(rad));
        GaussianBlur.getInstance().printWeightedMatrixToFile(weights);

        BufferedImage answer = null;
        try {
            BufferedImage source_img = ImageIO.read(new File("src/ImgInput/11.jpg"));
            answer = GaussianBlur.getInstance().createGaussianImage(source_img, weights, rad);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assert answer != null;
            ImageIO.write(answer, "PNG", new File("src/ImgOutput/12.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("DONE!");
    }
}
