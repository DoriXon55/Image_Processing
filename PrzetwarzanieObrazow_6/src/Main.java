import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    public static void main(String[] args) {
        ImageDownloader imageDownloader = new ImageDownloader();
        OpenCvFunctions opencvFunctions = new OpenCvFunctions();
        Scanner scanner = new Scanner(System.in);
        int userOption;
        try {
            BufferedImage bufferedImage = imageDownloader.downloadImage();
            Mat image = imageDownloader.bufferedImageToMat(bufferedImage);
            if (image.empty()) {
                System.out.println("Error: Zdjecie nie zostalo zaladowane");
                return;
            } else {
                opencvFunctions.saveImage(image, "originalImage.jpg");
                System.out.println("""
                        Wybierz opcje:\s
                        1. Wykonaj wyrywanie krawedzi metoda Canny
                        2. Wykonaj wykrywanie krawedzi metoda Laplacian
                        3. Wykonaj wykrywanie krawedzi metoda Sobel
                        4. Zmniejsz sobie krawedzie 
                        5. Wykryj sobie kontury
                        """);
                userOption = scanner.nextInt();
                switch (userOption) {
                    case 1:
                        image = Imgcodecs.imread("C:\\Users\\doria\\IdeaProjectsp\\PrzetwarzanieObrazow_6\\src\\piesek.jpg");
                        opencvFunctions.edgeDetectionCanny(image);
                        break;
                    case 2:
                        opencvFunctions.edgeDetectionLaplacian(image);
                        break;
                    case 3:
                        opencvFunctions.edgeDetectionSobel(image);
                        break;
                    case 4:
                        image = Imgcodecs.imread("C:\\Users\\doria\\IdeaProjectsp\\PrzetwarzanieObrazow_6\\src\\piesek.jpg");
                        opencvFunctions.reduceEdges(image);
                        break;
                    case 5:
                        opencvFunctions.contourDetection(image);
                        break;

                }
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Error: Wystapil problem z pobraniem zdjecia" + e.getMessage());
        }
    }
}