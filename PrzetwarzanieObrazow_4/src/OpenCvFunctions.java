import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.util.Scanner;

public class OpenCvFunctions {
    Scanner scanner = new Scanner(System.in);
    static Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
    static Mat binaryImage = new Mat();
    static Mat dilatedImage = new Mat();
    static Mat erodedImage = new Mat();

    public static void displayOpenImage(String filePath) {
        ImageIcon openImage = new ImageIcon(filePath);
        JLabel openLabel = new JLabel(openImage);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(openLabel);
        frame.pack();
        frame.setVisible(true);
    }
    public static void saveImage(Mat image, String outputFilePath) {
        boolean result = Imgcodecs.imwrite(outputFilePath, image);
        if (result) {
            System.out.println("Poprawnie zapisano");
            displayOpenImage(outputFilePath);
        } else {
            System.out.println("Nie zapisano pliku");
        }
    }
    public void binarization(Mat image)
    {
        if(image.channels() > 1)
            Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);

        Imgproc.threshold(image, binaryImage, 128, 255, Imgproc.THRESH_BINARY);
    }
    public void erosion(Mat image, boolean option)
    {
        binarization(image);
        if (option)
        {
            System.out.println("Wybierz ile razy chcesz wykonać erozje: \n");
            int userLoop = scanner.nextInt();
            for (int i = 0; i < userLoop; i++)
            {
                Imgproc.erode(binaryImage, erodedImage, element);
            }
            saveImage(erodedImage, "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_4\\src\\erodedMulti.jpg");
        } else {
            Imgproc.erode(binaryImage, erodedImage, element);
            saveImage(erodedImage, "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_4\\src\\erodedOnce.jpg");
        }
    }
    public void dilation(Mat image, boolean option)
    {
        binarization(image);
        if (option)
        {
            System.out.println("Wybierz ile razy chcesz wykonać dylację: \n");
            int userLoop = scanner.nextInt();
            for (int i = 0; i < userLoop; i++) {
                Imgproc.dilate(binaryImage, dilatedImage, element);
            }
            saveImage(dilatedImage, "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_4\\src\\dilatedMulti.jpg");

        } else {
            Imgproc.dilate(binaryImage, dilatedImage, element);
            saveImage(dilatedImage, "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_4\\src\\dilatedOnce.jpg");
        }
    }
    public void openedAndClosuredImage(Mat image, boolean option)
    {
        binarization(image);
        if (option)
        {
            Mat openedImage = new Mat();
            Imgproc.morphologyEx(binaryImage, openedImage, Imgproc.MORPH_OPEN, element);
            saveImage(openedImage, "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_4\\src\\openedImage.jpg");
        } else
        {
            Mat closedImage = new Mat();
            Imgproc.morphologyEx(binaryImage, closedImage, Imgproc.MORPH_CLOSE, element);
            saveImage(closedImage, "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_4\\src\\closedImage.jpg");
        }
    }
    public void contourExtraction(Mat image, int option)
    {
        dilation(image, false);
        erosion(image, false);
        Mat tmp = new Mat(binaryImage.size(), CvType.CV_8U);
        Mat contour = new Mat(binaryImage.size(), CvType.CV_8U);
        switch(option)
        {
            case 1:
                Core.add(binaryImage, dilatedImage, tmp);
                Core.subtract(tmp, image, contour);
                saveImage(contour, "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_4\\src\\contour1.jpg");
                break;
            case 2:
                Core.add(binaryImage, erodedImage, tmp);
                Core.subtract(binaryImage, tmp, contour);
                saveImage(contour, "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_4\\src\\contour2.jpg");
                break;
            case 3:
                Mat combined = new Mat(binaryImage.size(), CvType.CV_8U);
                Core.add(binaryImage, dilatedImage, tmp);
                Core.add(binaryImage, erodedImage, combined);
                Core.add(tmp, combined, contour);
                saveImage(contour, "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_4\\src\\contour3.jpg");
                break;
        }
    }
    public void skeletonization(Mat image)
    {
        binarization(image);
        Mat skeleton = Mat.zeros(binaryImage.size(), CvType.CV_8UC1);
        Mat eroded = new Mat();
        Mat temp = new Mat();
        while (Core.countNonZero(binaryImage) > 0) {
            Imgproc.erode(binaryImage, eroded, element);
            Imgproc.dilate(eroded, temp, element);
            Core.subtract(binaryImage, temp, temp);
            Core.bitwise_or(skeleton, temp, skeleton);
            binaryImage = eroded.clone();
        }
        saveImage(skeleton, "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_4\\src\\skeletonImage.jpg");
    }
}
