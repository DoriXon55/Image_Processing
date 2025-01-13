import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.util.Scanner;

public class OpenCvFunctions {
    public void moveTheImage(Mat image, Scanner scanner, Mat result) {
        System.out.println("Podaj przesuniecie w osi X: ");
        int xMove = scanner.nextInt();
        System.out.println("Podaj przesuniecie w osi Y: ");
        int yMove = scanner.nextInt();

        Mat move = Mat.eye(2, 3, CvType.CV_32F);
        move.put(0, 2, xMove);
        move.put(1, 2, yMove);
        Imgproc.warpAffine(image, result, move, new Size(image.cols(), image.rows()));
    }

    public void verticalImage(Mat image, Mat result) {
        Core.flip(image, result, 0);
    }

    public void horizonImage(Mat image, Mat result) {
        Core.flip(image, result, 1);
    }

    public void rotateImage(Mat image, Scanner scanner, Mat result) {
        System.out.println("Podaj o ile chcesz obrocic obraz: ");
        double angle = scanner.nextDouble();
        Point center = new Point(((double) image.cols() / 2), ((double) image.rows() / 2));
        Mat rotationMatrix = Imgproc.getRotationMatrix2D(center, angle, 1);
        Imgproc.warpAffine(image, result, rotationMatrix, new Size(image.cols(), image.rows()));
    }

    public void cutOutRectangle(Mat image, Scanner scanner, Mat result) {
        int x, y, width, height;
        System.out.println("Podaj x: ");
        x = scanner.nextInt();
        System.out.println("Podaj y: ");
        y = scanner.nextInt();
        System.out.println("Podaj szerokosc: ");
        width = scanner.nextInt();
        System.out.println("Podaj wysokosc: ");
        height = scanner.nextInt();
        if (x + width <= image.cols() && y + height <= image.rows()) {
            Mat tempResult = image.submat(new Rect(x, y, width, height));
            tempResult.copyTo(result);
        } else {
            System.out.println("Podany prostokat wykracza poza zdjecie!");
        }
    }

    public void zoomTwice(Mat image, Scanner scanner, Mat result) {
        System.out.println("""
                Wybierz, ktora funkcje chcesz wykorzystac\
                1. Resize\
                2. pyrUp""");
        int userZoomOption = scanner.nextInt();
        switch (userZoomOption) {
            case 1 ->
                    Imgproc.resize(image, result, new Size((double) image.cols() * 2, (double) image.rows() * 2), 0, 0, Imgproc.INTER_LINEAR);
            case 2 -> Imgproc.pyrUp(image, result);
        }
    }

    public void zoomQuadruple(Mat image, Scanner scanner, Mat result) {
        System.out.println("""
                Wybierz, ktora funkcje chcesz wykorzystac\
                1. Resize\
                2. pyrUp""");
        int userZoomOption = scanner.nextInt();
        switch (userZoomOption) {
            case 1 ->
                    Imgproc.resize(image, result, new Size((double) image.cols() * 4, (double) image.rows() * 4), 0, 0, Imgproc.INTER_LINEAR);
            case 2 -> {
                Mat temp = new Mat();
                Imgproc.pyrUp(image, temp);
                Imgproc.pyrUp(temp, result);
            }
        }
    }

    public void zoomOutQuadruple(Mat image, Scanner scanner, Mat result) {
        System.out.println("Wybierz, ktora funkcje chcesz wykorzystac" +
                "1. Resize" +
                "2. pyrDown");
        int userZoomOption = scanner.nextInt();
        switch (userZoomOption) {
            case 1 ->
                    Imgproc.resize(image, result, new Size((double) image.cols() / 4, (double) image.rows() / 4), 0, 0, Imgproc.INTER_LINEAR);
            case 2 -> {
                Mat temp = new Mat();
                Imgproc.pyrDown(image, temp);
                Imgproc.pyrDown(temp, result);
            }
        }
    }

    public void zoomOutTwice(Mat image, Scanner scanner, Mat result) {
        System.out.println("Wybierz, ktora funkcje chcesz wykorzystac" +
                "1. Resize" +
                "2. pyrDown");
        int userZoomOption = scanner.nextInt();
        switch (userZoomOption) {
            case 1 ->
                    Imgproc.resize(image, result, new Size((double) image.cols() / 2, (double) image.rows() / 2), 0, 0, Imgproc.INTER_LINEAR);
            // poprawić, tak ma być 
            case 2 -> Imgproc.pyrDown(image, result);
        }
    }

    public void zoomOneAndHalf(Mat image, Mat result) {
        Imgproc.resize(image, result, new Size(image.cols() * 1.5, image.rows() * 1.5), 0, 0, Imgproc.INTER_LINEAR);
    }

    public void displayOpenImage(String filePath) {
        ImageIcon openImage = new ImageIcon(filePath);
        JLabel openLabel = new JLabel(openImage);
        JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(openLabel);
        frame.pack();
        frame.setVisible(true);
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
