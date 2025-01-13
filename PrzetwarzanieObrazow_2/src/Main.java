import org.opencv.core.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    // inicjalizacja zmiennej result, aby nie trzeba było jej tworzyć za każdym razem


    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }


    public static void main(String[] args) throws IOException {

        //inicjalizacja klasy z funkcjami do pobierania losowych zdjęć z internetu
        ImageDownloader imageDownloader = new ImageDownloader();
        OpenCvFunctions openCvFunctions = new OpenCvFunctions();
        Scanner scanner = new Scanner(System.in);
        Mat result = new Mat();

        try {
            BufferedImage bufferedImage = imageDownloader.downloadImage();
            String outputImageFile = "";
            int userOption;

            //bufforowanie zdjęcia do macierzy
            Mat image = imageDownloader.bufferedImageToMat(bufferedImage);

            if (image.empty()) {
                System.out.println("Error: Zdjecie nie jest zaladowane");
                return;
            } else {
                System.out.print("""
                        Prosze wybrac opcje:
                        1. Przesun obraz
                        2. Odbij obraz w pionie
                        3. Odbij obraz w poziomie
                        4. Obroc obraz o kat
                        5. Wytnij z obrazu prostokat
                        6. Powieksz obraz 2-krotnie
                        7. Powieksz obraz 4-krotnie
                        8. Pomniejsz obraz 2-krotnie
                        9. Pomniejsz obraz 4-krotnie
                        10. Powieksz obraz 1.5 razy
                        """);
                userOption = scanner.nextInt();
                // switch służacy do wybierania funkcji
                switch (userOption) {
                    case 1 -> {
                        outputImageFile = "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_2\\src\\moved_image.jpg";
                        openCvFunctions.moveTheImage(image, scanner, result);
                    }
                    case 2 -> {
                        outputImageFile = "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_2\\src\\vertical_image.jpg";
                        openCvFunctions.verticalImage(image, result);
                    }
                    case 3 -> {
                        outputImageFile = "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_2\\src\\horizon_image.jpg";
                        openCvFunctions.horizonImage(image, result);
                    }
                    case 4 -> {
                        outputImageFile = "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_2\\src\\rectangle_image.jpg";
                        openCvFunctions.rotateImage(image, scanner, result);
                    }
                    case 5 -> {
                        // tutaj raczej rozbić
                        outputImageFile = "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_2\\src\\rectangleImage.jpg";
                        openCvFunctions.cutOutRectangle(image, scanner, result);
                    }
                    case 6 -> {
                        // tu rozbić
                        outputImageFile = "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_2\\src\\zoomTwice.jpg";
                        openCvFunctions.zoomTwice(image, scanner, result);
                    }
                    case 7 -> {
                        outputImageFile = "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_2\\src\\zoomQuadruple.jpg";
                        openCvFunctions.zoomQuadruple(image, scanner, result);
                    }
                    case 8 -> {
                        outputImageFile = "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_2\\src\\zoomOutTwice.jpg";
                        openCvFunctions.zoomOutTwice(image, scanner, result);
                    }
                    case 9 -> {
                        outputImageFile = "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_2\\src\\zoomOutQuadruple.jpg";
                        openCvFunctions.zoomOutQuadruple(image, scanner, result);
                    }
                    case 10 -> {
                        outputImageFile = "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_2\\src\\one_and_half_image.jpg";
                        openCvFunctions.zoomOneAndHalf(image, result);
                    }
                    default -> System.out.println("Error: Zla opcja wybierz jeszcze raz");
                }
            }
            //zapisywanie zdjęcia, to jest niezmienne


            openCvFunctions.saveImage(result, outputImageFile);
            scanner.close();
        } catch (IOException e) {
            System.out.println("Error: Wystapil problem z pobraniem zdjecia" + e.getMessage());
        }
    }
}