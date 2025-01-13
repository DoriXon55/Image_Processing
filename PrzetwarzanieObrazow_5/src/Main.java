import org.opencv.core.Core;
import org.opencv.core.Mat;

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
                        1. Wykonaj rozmycie filtrem Gaussa
                        2. Wykonaj rozmycie filtrem Medianowym
                        3. Wykonaj rozmycie filtrem Bilateralnym
                        4. Wykonaj rozmycie autorskim filtrem :)
                        5. Wykonaj wyostrzenie obrazu (3 rozwiązania)
                        6. Wykonaj szum "salt and pepper" i przetestuj sobie filtry
                        7. Wykonaj szum Gaussa i sobie sprawdź filtry po raz kolejny
                        """);
                userOption = scanner.nextInt();
                switch (userOption) {
                    case 1:
                        opencvFunctions.gaussFilter(image);
                        break;
                    case 2:
                        opencvFunctions.medianFilter(image);
                        break;
                    case 3:
                        opencvFunctions.bilateralFilter(image);
                        break;
                    case 4:
                        opencvFunctions.originalFilter(image);
                        break;
                    case 5:
                        opencvFunctions.sharpening(image);
                        break;
                    case 6:
                        opencvFunctions.saltNoise(image);
                        break;
                    case 7:
                        opencvFunctions.gaussNoise(image);
                        break;
                }
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Error: Wystapil problem z pobraniem zdjecia" + e.getMessage());
        }
    }
}