import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


public class OpenCvFunctions {
    private final static String outputPath = "C:\\Users\\doria\\IdeaProjectsp\\PrzetwarzanieObrazow_6\\src";

    public static void displayOpenImage(String filePath) {
        ImageIcon openImage = new ImageIcon(filePath);
        JLabel openLabel = new JLabel(openImage);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(openLabel);
        frame.pack();
        frame.setVisible(true);
    }
    public void saveImage(Mat image, String outputFileName) {
        String finalPath = outputPath + outputFileName;
        boolean result = Imgcodecs.imwrite(finalPath, image);
        if (result) {
            System.out.println("Poprawnie zapisano");
            displayOpenImage(finalPath);
        } else {
            System.out.println("Nie zapisano pliku");
        }
    }

}
