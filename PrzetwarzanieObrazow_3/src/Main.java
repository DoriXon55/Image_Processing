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


        try {
            BufferedImage bufferedImage = imageDownloader.downloadImage();
            String outputImageFile = "";

            int userOption;

            Mat image = imageDownloader.bufferedImageToMat(bufferedImage);

            if (image.empty()) {
                System.out.println("Error: Zdjecie nie zostalo zaladowane");
                return;
            } else {
                System.out.println("""
                        Wybierz opcje:\s
                        1. Zmien kontrast
                        2. Zmien jasnosc
                        3. Naloz dwa obrazy na siebie
                        4. Normalizacja
                        5. Rozloz obraz na 3 skladowe
                        6. Wczytaj przestrzen kolorow w HSV
                        7. Binaryzacja (wczytanie w skali szarosci)
                        8. Dzialania na obrazach
                        9. Histogram
                        """);
                userOption = scanner.nextInt();
                // PRZEROBIC NA ZAPISY W FUNKCJACH PRAWDOPOODBNIE
                switch (userOption) {
                    case 1:
                        outputImageFile = "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\contrast.jpg";
                        opencvFunctions.contrast(image, scanner);
                        opencvFunctions.saveImage(image, outputImageFile);
                        break;
                    case 2:
                        outputImageFile = "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\brightness.jpg";
                        opencvFunctions.brightness(image, scanner);
                        opencvFunctions.saveImage(image, outputImageFile);
                        break;
                    case 3:
                        outputImageFile = "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\imposition.jpg";
                        image = opencvFunctions.imposition(image);
                        opencvFunctions.saveImage(image, outputImageFile);
                        break;
                    case 4:
                        // tutaj wczytaj zdjęcie podane w sprawozdaniu
                        opencvFunctions.normalization();
                        break;
                    case 5:
                        // tutaj tak samo
                        image = Imgcodecs.imread("C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\task5Picture.jpg");
                        opencvFunctions.components(image);
                        break;
                    case 6:
                        opencvFunctions.saveImage(image, "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\orginalImageHSV.jpg");
                        outputImageFile = "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\HSV.jpg";
                        opencvFunctions.hsv(image);
                        opencvFunctions.saveImage(image, outputImageFile);
                        break;
                    case 7:
                        opencvFunctions.binarization(image);
                        break;
                    case 8:
                        opencvFunctions.actoins(image);
                        break;
                    case 9:
                        outputImageFile = "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_3\\src\\histogram.jpg";
                        opencvFunctions.histogram(image);
                        opencvFunctions.saveImage(image, outputImageFile);
                        break;
                }
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Error: Wystapil problem z pobraniem zdjecia" + e.getMessage());
        }


    }
}