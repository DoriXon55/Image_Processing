import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

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
        boolean option;
        int userOption;
        int contourOption;

        try {
            BufferedImage bufferedImage = imageDownloader.downloadImage();
            Mat image = imageDownloader.bufferedImageToMat(bufferedImage);
            if (image.empty()) {
                System.out.println("Error: Zdjecie nie zostalo zaladowane");
                return;
            } else {
                System.out.println("""
                        Wybierz opcje:\s
                        1. Wykonaj erozje
                        2. Wykonaj erozje kilkukrotnie
                        3. Wykonaj dylatację
                        4. Wykonaj dylatację kilkukrotnie
                        5. Wykonaj opeacje otwarcia i domknięcia
                        6. Wykonaj ekstrację konturów
                        7. Wykonaj szkielet literki
                        """);
                userOption = scanner.nextInt();

                switch (userOption) {
                    case 1:
                        image = Imgcodecs.imread("C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_4\\src\\tmp.jpg");
                        option = false;
                        opencvFunctions.erosion(image, option);
                        break;
                    case 2:
                        option = true;
                        opencvFunctions.erosion(image, option);
                        break;
                    case 3:
                        image = Imgcodecs.imread("C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_4\\src\\tmp.jpg");
                        option = false;
                        opencvFunctions.dilation(image, option);
                        break;
                    case 4:
                        option = true;
                        opencvFunctions.dilation(image, option);
                        break;
                    case 5:
                        System.out.println("Chcesz otworzyć czy zamknąć zdjęcie? " +
                                "1. Otworzyć!" +
                                "2. Zamknąć!");
                        userOption = scanner.nextInt();
                        option = userOption == 1; //  <- = option = (userOption == 1) ? true : false;
                        opencvFunctions.openedAndClosuredImage(image, option);
                        break;
                    case 6:

                        System.out.println("Jaką metodą chcesz wykonać ekstrakcję konturów? " +
                                "1. Pierwszą!" +
                                "2. Drugą!" +
                                "3. Wybierz trójkę Panie!");
                        contourOption = scanner.nextInt();
                        opencvFunctions.contourExtraction(image, contourOption);
                        break;
                    case 7:
                        opencvFunctions.skeletonization(image);
                        break;
                }
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Error: Wystapil problem z pobraniem zdjecia" + e.getMessage());
        }
    }
}