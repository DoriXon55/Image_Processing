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


    /*
    Metoda od wykrywania krawędzi metodą Canny, jest tutaj używana funkcja Imgproc.Canny(), która przyjmuje
    obraz wejściowy, wyjściowy oraz progi wykrywania krawędzi. Tutaj zbyt dużo się nie dzieje
    */
    public void edgeDetectionCanny(Mat image)
    {
        Mat edges = new Mat();
        double tresh1 = 50, tresh2 = 100;
        Imgproc.Canny(image, edges, tresh1, tresh2);
        saveImage(edges, "cannyEdges.jpg");
    }

    /*
    Metoda do wykrywania krawędzi metodą Laplacian, która używa funkcję Imgproc.Laplacian. Przyjmuje ona
    obraz wejściowy, obraz wyjściowy, typ danych wejsciowych CvType.CV_64F (64 bity) i rozmiar naszeo jądra
     */
    public void edgeDetectionLaplacian(Mat image)
    {
        Mat laplacian = new Mat();
        Imgproc.Laplacian(image, laplacian, CvType.CV_64F, 9);

        Mat ret = new Mat();
        Core.convertScaleAbs(laplacian, ret);
        saveImage(ret, "laplacianEdges.jpg");
    }

    /*
    Metoda do wykrywania krawędzi metodą Sobel. Jest tutaj używana funkcja Imgpro.Sobel() która to jest używana dwukrotnie
    dla kierunku X oraz Y a na końcu łączona w całość i konwertowana na wynk 8-bitowy.
     */
    public void edgeDetectionSobel(Mat image)
    {
        Mat sobel_x = new Mat();
        Mat sobel_y = new Mat();
        Imgproc.Sobel(image, sobel_x, CvType.CV_64F, 1, 0 ,3);
        Imgproc.Sobel(image, sobel_y, CvType.CV_64F, 0, 1, 3);


        Mat sumOfSobel = new Mat();
        Core.magnitude(sobel_x, sobel_y, sumOfSobel);
        Core.convertScaleAbs(sumOfSobel, sumOfSobel);
        saveImage(sumOfSobel, "sobelEdges.jpg");
    }

    /*
    Metoda do redukowania krawędzi, która korzysta z rozmycia Gaussa i wytłumaczonej wcześniej funkcji
    do wykrywania krawędzi metodą Canny, lecz mogłaby tutaj być prawie każda.
     */
    public void reduceEdges(Mat image)
    {
        Mat blurred = new Mat(), edges = new Mat();
        Imgproc.GaussianBlur(image, blurred, new Size(3, 3), 0);
        Imgproc.Canny(blurred, edges, 50, 150);
        saveImage(edges, "reduceEdges.jpg");
    }

    /*
    Metoda, która wykrywa krawędzie na obrazie. Zamieniamy go na skalę szarości, wykonujemy binaryzację,
    wykrywamy kontury oraz kolorujemy sobie kredkami konruty na obrazie i łączymy.
     */
    public void contourDetection(Mat image) {
        Mat grayImage = new Mat();
        if (image.channels() > 1) { // Sprawdzenie, czy obraz jest kolorowy
            Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
        } else {
            grayImage = image.clone(); // Kopiowanie, jeśli już jest w szarości
        }

        Mat binary = new Mat();
        Imgproc.threshold(grayImage, binary, 128, 255, Imgproc.THRESH_BINARY);

        List<MatOfPoint> contoursList = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(binary, contoursList, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        Mat result = new Mat();
        Imgproc.cvtColor(grayImage, result, Imgproc.COLOR_GRAY2BGR);

        // Rysowanie konturów na obrazie
        Imgproc.drawContours(result, contoursList, -1, new Scalar(0, 255, 0), 2);
        saveImage(result, "contoursDetection.jpg");
    }

}
