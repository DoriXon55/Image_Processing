import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class OpenCvFunctions {

    public static double userInput;
    ImageDownloader imageDownloader = new ImageDownloader();

    public static void displayOpenImage(String filePath) {
        ImageIcon openImage = new ImageIcon(filePath);
        JLabel openLabel = new JLabel(openImage);
        JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(openLabel);
        frame.pack();
        frame.setVisible(true);
    }

    public void components(Mat image) {

        List<Mat> channelsBGR = new ArrayList<Mat>();
        Core.split(image, channelsBGR);
        saveImage(channelsBGR.get(0), "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\blueImage.jpg");
        saveImage(channelsBGR.get(1), "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\greenImage.jpg");
        saveImage(channelsBGR.get(2), "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\redImage.jpg");
    }


    //sprawdzic czy zadziała.
    public Mat imposition(Mat image) {
        Mat result = new Mat();
        try {
            BufferedImage bufferedImage2 = imageDownloader.downloadImage();
            Mat image2 = imageDownloader.bufferedImageToMat(bufferedImage2);
            Core.addWeighted(image, 0.5, image2, 0.5, 0.0, result);
        } catch (IOException e) {
            System.out.println("Error: Problem w nakladaniu zdjec" + e.getMessage());
        }
        return result;
    }

    public void hsv(Mat image) {
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2HSV);
    }

    public void actoins(Mat image) {
        try {
            Mat addedImage = new Mat();
            Mat subtractedImageAB = new Mat();
            Mat subtractedImageBA = new Mat();
            Mat multiplyImage = new Mat();
            Mat divideImageAB = new Mat();
            Mat divideImageBA = new Mat();

            BufferedImage bufferedImage2 = imageDownloader.downloadImage();
            Mat image2 = imageDownloader.bufferedImageToMat(bufferedImage2);

            Core.add(image, image2, addedImage);
            Core.subtract(image, image2, subtractedImageAB);
            Core.subtract(image2, image, subtractedImageBA);
            Core.multiply(image, image2, multiplyImage);
            Core.divide(image, image2, divideImageAB);
            Core.divide(image2, image, divideImageBA);

            saveImage(addedImage, "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\addedImage.jpg");
            saveImage(subtractedImageAB, "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\subtractedImageAB.jpg");
            saveImage(subtractedImageBA, "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\subtractedImageBA.jpg");
            saveImage(multiplyImage, "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\multiplyImage.jpg");
            saveImage(divideImageAB, "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\divideImageAB.jpg");
            saveImage(divideImageBA, "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\divideImageBA.jpg");
        } catch (IOException e) {
            System.out.println("Error: Problem w nakladaniu zdjec" + e.getMessage());
        }
    }

    public void binarization(Mat image) {
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
        Mat binary = new Mat();
        Imgproc.threshold(image, binary, 128, 255, Imgproc.THRESH_BINARY);
        saveImage(binary, "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\'binary.jpg");
    }

    public void normalization() {
        Mat image = Imgcodecs.imread("C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\task4Picture.jpg");

        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);

        Core.MinMaxLocResult minMaxImageValue = Core.minMaxLoc(image);
        double minVal = minMaxImageValue.minVal;
        double maxVal = minMaxImageValue.maxVal;
        System.out.printf("Wartosci przed normalizacja -> min: %.2f, max: %.2f \n", minVal, maxVal);

        Mat normalizedImage = new Mat();
        image.convertTo(normalizedImage, CvType.CV_8U, 255.0 / (maxVal - minVal), -255.0 * minVal / (maxVal - minVal));
        Core.MinMaxLocResult minMaxResultImage = Core.minMaxLoc(normalizedImage);
        minVal = minMaxResultImage.minVal;
        maxVal = minMaxResultImage.maxVal;
        System.out.printf("Wartosci po normalizacji -> min: %.2f, max: %.2f \n", minVal, maxVal);
        saveImage(normalizedImage, "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\normalizedImage.jpg");
    }

    public void contrast(Mat image, Scanner scanner) {
        System.out.println("O ile chcesz zmienic kontrast?");
        userInput = scanner.nextDouble();
        Core.multiply(image, new Scalar(userInput, userInput, userInput), image);
    }

    public void brightness(Mat image, Scanner scanner) {
        System.out.println("O ile chcesz rozjasnic zdjecie?");
        userInput = scanner.nextDouble();
        Core.add(image, new Scalar(userInput, userInput, userInput), image);
    }

    public void histogram(Mat image) {
        List<Mat> bgrPlanes = new ArrayList<>();
        Core.split(image, bgrPlanes);

        int histSize = 256;
        float[] range = {0, 256};
        MatOfFloat histRange = new MatOfFloat(range);

        Mat bHist = new Mat(), gHist = new Mat(), rHist = new Mat();

        Imgproc.calcHist(bgrPlanes.subList(0, 1), new MatOfInt(0), new Mat(), bHist, new MatOfInt(histSize), histRange, false);
        Imgproc.calcHist(bgrPlanes.subList(1, 2), new MatOfInt(0), new Mat(), gHist, new MatOfInt(histSize), histRange, false);
        Imgproc.calcHist(bgrPlanes.subList(2, 3), new MatOfInt(0), new Mat(), rHist, new MatOfInt(histSize), histRange, false);

        Mat combinedHist = new Mat();
        Core.add(bHist, gHist, combinedHist);
        Core.add(combinedHist, rHist, combinedHist);

        Core.normalize(combinedHist, combinedHist, 0, 256, Core.NORM_MINMAX);

        int histW = 500, histH = 500;
        int binW = Math.round((float) histW / histSize);

        Mat histImage = new Mat(histH, histW, CvType.CV_8UC3, new Scalar(255, 255, 255));
        for (int i = 1; i < histSize; i++) {
            Imgproc.line(histImage,
                    new Point(binW * (i - 1), histH - Math.round(combinedHist.get(i - 1, 0)[0])),
                    new Point(binW * i, histH - Math.round(combinedHist.get(i, 0)[0])),
                    new Scalar(0, 0, 0), 2);  // rysujemy w kolorze czarnym
        }
        saveImage(histImage, "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\histImage.jpg");
    }

    public void saveImage(Mat image, String outputFilePath) {
        boolean result = Imgcodecs.imwrite(outputFilePath, image);
        if (result) {
            System.out.println("Poprawnie zapisano");
            displayOpenImage(outputFilePath);
        } else {
            System.out.println("Nie zapisano pliku");
        }
    }

}
