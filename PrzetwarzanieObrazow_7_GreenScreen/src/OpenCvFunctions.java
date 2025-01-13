import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import javax.swing.*;

public class OpenCvFunctions {
    private final static String outputPath = "C:\\Users\\doria\\IdeaProjectsp\\PrzetwarzanieObrazow_7_GreenScreen\\src\\";

    public void displayOpenImage(String filePath) {
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
    Metoda służąca do nałożenia ramki, która nie działa.
     */

    public void applyFrame(String resultImagePath, String framePath) {
        // Załaduj zdjęcia
        Mat resultImage = Imgcodecs.imread(resultImagePath);
        Mat frame = Imgcodecs.imread(framePath);

        if (resultImage.empty() || frame.empty()) {
            System.out.println("Error loading images");
            return;
        }

        Mat grayFrame = new Mat();
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);

        Mat mask = new Mat();
        Core.inRange(grayFrame, new Scalar(235), new Scalar(245), mask);


        Mat nonZero = new Mat();
        Core.findNonZero(mask, nonZero);
        if (nonZero.total() == 0) {
            System.out.println("No placeholder area found in frame");
            return;
        }

        Rect imageArea = Imgproc.boundingRect(nonZero);
        double srcAspectRatio = (double) resultImage.width() / resultImage.height();
        double dstAspectRatio = (double) imageArea.width / imageArea.height;

        Mat resizedResult = new Mat();
        if (srcAspectRatio > dstAspectRatio) {

            int newHeight = imageArea.height;
            int newWidth = (int) (newHeight * srcAspectRatio);
            Imgproc.resize(resultImage, resizedResult, new Size(newWidth, newHeight));
            int startX = (newWidth - imageArea.width) / 2;
            Rect cropRect = new Rect(startX, 0, imageArea.width, imageArea.height);
            resizedResult = new Mat(resizedResult, cropRect);
        } else {

            int newWidth = imageArea.width;
            int newHeight = (int) (newWidth / srcAspectRatio);
            Imgproc.resize(resultImage, resizedResult, new Size(newWidth, newHeight));
            int startY = (newHeight - imageArea.height) / 2;
            Rect cropRect = new Rect(0, startY, imageArea.width, imageArea.height);
            resizedResult = new Mat(resizedResult, cropRect);
        }
        Mat finalImage = frame.clone();
        resizedResult.copyTo(finalImage.submat(imageArea));
        saveImage(finalImage, "finalImageFrame.jpg");
    }


    /*
    Metoda służąca do dodania pięknego tła i użycia filtrów oraz otwarcia i domknięcia. Wszystko co zostało tutaj użyte było używane już w laboratoriach wcześniej,
    więc pozwolę sobie nie omawiać tego tak bardzo szczegółowo jak wcześniej.
     */

    void algorithm(int userOption)
    {

        // wczytywanie obrazów
        Mat backgorund = Imgcodecs.imread("C:\\Users\\doria\\IdeaProjectsp\\PrzetwarzanieObrazow_7_GreenScreen\\src\\background.jpg");
        Mat selfie = Imgcodecs.imread("C:\\Users\\doria\\IdeaProjectsp\\PrzetwarzanieObrazow_7_GreenScreen\\src\\selfie.jpg");

        // dopasowanie rozmiarów
        Imgproc.resize(backgorund, backgorund, new Size(selfie.cols(), selfie.rows()));

        // konwersja selfie do HSV
        Mat selfieHSV = new Mat();
        Imgproc.cvtColor(selfie, selfieHSV, Imgproc.COLOR_RGB2HSV);


        // tworzenie maski
        Scalar lowerGreen = new Scalar(35, 55, 55);
        Scalar upperGreen = new Scalar(85, 255, 255);
        Mat mask = new Mat();
        Core.inRange(selfieHSV, lowerGreen, upperGreen, mask);

        // zastosowanie wybranego filtra

        switch (userOption) {
            case 1:
                Imgproc.GaussianBlur(mask, mask, new Size(5,5), 0);
                break;
            case 2:
                Imgproc.medianBlur(mask,mask,3);
                break;
            case 3:
                Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
                Imgproc.morphologyEx(mask, mask, Imgproc.MORPH_OPEN, kernel);
                Imgproc.morphologyEx(mask, mask, Imgproc.MORPH_CLOSE, kernel);
                break;
            default:
                System.out.println("Nieprawidlowy wybor filtra!");
                break;
        }


        Mat maskInv = new Mat();
        Core.bitwise_not(mask, maskInv);

        Mat selfieFG = new Mat();
        Core.bitwise_and(selfie, selfie, selfieFG, maskInv);

        Mat backgroundBG = new Mat();
        Core.bitwise_and(backgorund, backgorund, backgroundBG, mask);

        Mat result = new Mat();
        Core.add(selfieFG, backgroundBG, result);
        saveImage(result, "algorithmResult.png");
    }


    /*
    Tutaj metoda odpala naszą kamerkę i nas podgląda.
     */
    public void openCamera() {
        // Otwórz kamerę
        VideoCapture camera = new VideoCapture(0);

        if (!camera.isOpened()) {
            System.err.println("Nie udało się otworzyć kamery.");
            return;
        }

        // Wczytaj obraz nowego tła
        Mat backgroundImage = Imgcodecs.imread("C:\\Users\\doria\\IdeaProjectsp\\PrzetwarzanieObrazow_7_GreenScreen\\src\\background.jpg");

        if (backgroundImage.empty()) {
            System.err.println("Nie udało się wczytać obrazu tła: background.jpg");
            return;
        }

        // Pobierz rozmiar tła
        Size frameSize = new Size(camera.get(Videoio.CAP_PROP_FRAME_WIDTH),
                camera.get(Videoio.CAP_PROP_FRAME_HEIGHT));

        // Dopasuj tło do rozmiaru wideo
        Mat resizedBackground = new Mat();
        Imgproc.resize(backgroundImage, resizedBackground, frameSize);

        Mat frame = new Mat(); // Bieżąca klatka
        Mat mask = new Mat();  // Maska do detekcji tła
        Mat maskInv = new Mat();
        Mat foreground = new Mat();
        Mat background = new Mat();

        // Załaduj klasyfikator Haar do wykrywania twarzy
        CascadeClassifier faceCascade = new CascadeClassifier("C:\\Users\\doria\\IdeaProjectsp\\PrzetwarzanieObrazow_7_GreenScreen\\src\\haarcascade_frontalface_default.xml");

        // Sprawdź, czy udało się załadować klasyfikator
        if (faceCascade.empty()) {
            System.err.println("Nie udało się załadować klasyfikatora Haar.");
            return;
        }

        while (true) {
            // Pobierz klatkę z kamery
            camera.read(frame);
            if (frame.empty()) {
                System.err.println("Nie udało się odczytać klatki.");
                break;
            }

            // Wykrywanie tła (np. zielonego ekranu)
            Scalar lowerBound = new Scalar(35, 55, 55);  // Dolny zakres zielonego (HSV)
            Scalar upperBound = new Scalar(90, 255, 255); // Górny zakres zielonego (HSV)

            Mat hsv = new Mat();
            Imgproc.cvtColor(frame, hsv, Imgproc.COLOR_BGR2HSV); // Przekształć do HSV
            Core.inRange(hsv, lowerBound, upperBound, mask); // Stwórz maskę

            // Sprawdzenie, czy jest jakikolwiek zielony kolor (tło)
            int nonZeroCount = Core.countNonZero(mask);
            boolean isGreenscreenPresent = nonZeroCount > 0;

            // Odwróć maskę
            Core.bitwise_not(mask, maskInv);

            // Wydziel pierwszy plan
            Core.bitwise_and(frame, frame, foreground, maskInv);

            // Wydziel tło
            Core.bitwise_and(resizedBackground, resizedBackground, background, mask);

            // Połącz tło z pierwszym planem
            Mat result = new Mat();
            Core.add(foreground, background, result);

            // Detekcja twarzy
            Mat gray = new Mat();
            Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY); // Przekształć do skali szarości
            Imgproc.equalizeHist(gray, gray); // Wyrównaj histogram

            // Wykrywanie twarzy
            MatOfRect faces = new MatOfRect();
            faceCascade.detectMultiScale(gray, faces);

            // Rysowanie prostokątów wokół wykrytych twarzy
            Rect[] facesArray = faces.toArray();  // Konwersja do tablicy Rect
            for (Rect face : facesArray) {
                Imgproc.rectangle(result, new Point(face.x, face.y),
                        new Point(face.x + face.width, face.y + face.height),
                        new Scalar(0, 255, 0), 2);
            }

            // Sprawdzenie i dodanie komunikatu na ekranie
            if (!isGreenscreenPresent) {
                Imgproc.putText(result, "No greenscreen", new Point(10, 30),
                        Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 0, 255), 2);
            }

            // Wyświetl wynik
            HighGui.imshow("Face Detection and Background Replacement", result);

            // Zatrzymaj program po naciśnięciu klawisza 'q'
            if (HighGui.waitKey(1) == 'q') {
                break;
            }
        }

        // Zwolnij zasoby
        camera.release();
        HighGui.destroyAllWindows();
    }



    void detectFace()
    {
        Mat image = Imgcodecs.imread("C:\\Users\\doria\\IdeaProjectsp\\PrzetwarzanieObrazow_7_GreenScreen\\src\\the-leon-dicaprio-produced-captain-planet-should-leave-out-gaia-20190906005544.jpg"); // Ścieżka do obrazu

        if (image.empty()) {
            System.err.println("Nie udało się wczytać obrazu.");
            return;
        }

        // Przekształć obraz do skali szarości
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        // Wyrównaj histogram obrazu
        Imgproc.equalizeHist(grayImage, grayImage);

        // Załaduj klasyfikator Haar Cascade (ścieżka do pliku haarcascade_frontalface_default.xml)
        CascadeClassifier faceCascade = new CascadeClassifier("C:\\Users\\doria\\IdeaProjectsp\\PrzetwarzanieObrazow_7_GreenScreen\\src\\haarcascade_frontalface_default.xml");

        if (faceCascade.empty()) {
            System.err.println("Nie udało się załadować klasyfikatora.");
            return;
        }

        // Wykrywanie twarzy
        MatOfRect faces = new MatOfRect();
        faceCascade.detectMultiScale(grayImage, faces, 1.1, 2, 0, new Size(30, 30), new Size());

        // Rysowanie prostokątów wokół wykrytych twarzy
        for (Rect face : faces.toArray()) {
            Imgproc.rectangle(image, face.tl(), face.br(), new Scalar(0, 255, 0), 2);
        }

        // Wyświetl wynik
        HighGui.imshow("Wykryte twarze", image);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }
}