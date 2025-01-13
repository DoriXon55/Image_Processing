import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.util.Scanner;

public class OpenCvFunctions {
    public static Mat filterResult = new Mat();
    public static Mat noisyImage = new Mat();
    public static Mat noisyImageTmp = new Mat();
    private final static String outputPath = "C:\\Users\\doria\\IdeaProjects\\PrzetwarzanieObrazow_5\\src\\";
    Scanner scanner = new Scanner(System.in);
    int userOption = 0;

    public static void displayOpenImage(String filePath) {
        ImageIcon openImage = new ImageIcon(filePath);
        JLabel openLabel = new JLabel(openImage);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(openLabel);
        frame.pack();
        frame.setVisible(true);
    }

    /*
    Ta metoda dodaje do obrazu szum. Używa tutaj orginalnego obrazu oraz prawdopodobieństwo pojawienia
    się szumu - prob. Metoda tworzy kopę obrazu wejściowego. Dla każdego piksela generuje wartość z przedziału [0,1].
    Następnie jeśli rand < prob to piksel ustawiany jest na czarny. Jeśli rand > 1 - prob to na biało.
    Na końcu zwraca zmodyfikowany obraz z dodanym szumem.
    Dlaczego jest tutaj [0,0,0] oraz [255,255,255]? Ponieważ obraz jest w RGB i musimy odpowiednio to zrobić także
    dla RGB lub dla skali szarości.
     */
    public static Mat addSaltAndPepperNoise(Mat image, double prob) {
        Mat noisyImage = image.clone();
        int channels = noisyImage.channels();

        for (int i = 0; i < noisyImage.rows(); i++) {
            for (int j = 0; j < noisyImage.cols(); j++) {
                double random = Math.random();
                if (random < prob) {
                    // Ustaw piksel na czarny dla wszystkich kanałów
                    if (channels == 1) {
                        noisyImage.put(i, j, 0); // Skala szarości
                    } else {
                        noisyImage.put(i, j, new double[] {0, 0, 0}); // Kolor
                    }
                } else if (random > 1 - prob) {
                    // Ustaw piksel na biały dla wszystkich kanałów
                    if (channels == 1) {
                        noisyImage.put(i, j, 255); // Skala szarości
                    } else {
                        noisyImage.put(i, j, new double[] {255, 255, 255}); // Kolor
                    }
                }
            }
        }
        return noisyImage;
    }

    /*
    Metoda dodaje do obrazu szum Gaussa. Czyli przpyadkowe wartości rozproszone na obrazie.
    zmienna sigmaBoy to odchylenie standardowe dla generowanego szumu
    Tworzy maciesz noise o tym samym rozmiarze co image. Następnie wypełnia noise losowymi
    wartościami z rozkładu normalnego o średniej 0 i sigmaBoy. Następnie dodaje macierz noise
    do obrazu wejściowego za pomocą Core.add i na koniec zwraca obraz z dodanym szumem.
     */
    public static Mat addGaussianNoise(Mat image, int sigmaBoy)
    {
        Mat noise = new Mat(image.size(), image.type());
        Core.randn(noise, 0, sigmaBoy);
        Core.add(image, noise, noisyImageTmp);
        return noisyImageTmp;
    }


 /*
 tu wiadomo co się dzieje. Jedyne co dodałem to finalPath.
  */
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
    metoda która stosuje filtr gaussa służącyy do rozmycia obrazu. Pyta użytkownika o moc filtra a następnie używa
    funkcji, która pozyskuje parametry w postaci zdjęcia, obrazu wyjściowego, rozamiru elementu (kernela) oraz wartość
    odchylenia standardowego
     */
    public void gaussFilter(Mat image)
    {
        System.out.println("Wybierz moc filtra: " +
                "1. Zwykła " +
                "2. Srednia " +
                "3. Mocna ");
        userOption = scanner.nextInt();
        switch (userOption)
        {
            case 1:
                Imgproc.GaussianBlur(image, filterResult, new Size(3, 3), 0);
                saveImage(filterResult, "GaussianBlur_1.jpg");
                break;
            case 2:
                Imgproc.GaussianBlur(image, filterResult, new Size(7, 7), 0);
                saveImage(filterResult, "GaussianBlur_2.jpg");
                break;
            case 3:
                Imgproc.GaussianBlur(image, filterResult, new Size(9, 9), 0);
                saveImage(filterResult, "GaussianBlur_3.jpg");
                break;
        }
    }


    /*
    Ten filtr także służy to rozmycia tak samo jak Gaussa. Tutaj jedynie zmienia się używana funkcja. Przyjmuje ona
    obraz wejściowy, wyjściowy oraz rozmiar kernela, który musi być liczbą nieparzystą. Wystarczy liczba i tworzy odpowiednio
    3x3, 7x7 lub 9x9.
     */
    public void medianFilter(Mat image)
    {
        System.out.println("Wybierz moc filtra: " +
                "1. Zwykła " +
                "2. Srednia " +
                "3. Mocna ");
        userOption = scanner.nextInt();
        switch (userOption)
        {
            case 1:
                Imgproc.medianBlur(image, filterResult, 3);
                saveImage(filterResult, "MedianBlur_1.jpg");
                break;
            case 2:
                Imgproc.medianBlur(image, filterResult, 7);
                saveImage(filterResult, "MedianBlur_2.jpg");
                break;
            case 3:
                Imgproc.medianBlur(image, filterResult, 9);
                saveImage(filterResult, "MedianBlur_3.jpg");
                break;
        }
    }


    /*
    Kolejny filtr tym razem bilateralny. On wygładza obraz oraz zachowuje krawędzie. Tutaj również zmienia się tylko
    funkcja. Przyjmuje ona tak samo jak poprzednie obraz wejściowy oraz wyjściowy. Rozmiar filtra w pikselach oraz dwa ostatnie
    parametry to intensywność wygładzenia przestrzennego oraz intensywność wygładzenia kolorów.
     */
    public void bilateralFilter(Mat image)
    {
        System.out.println("Wybierz moc filtra: " +
                "1. Zwykła " +
                "2. Srednia " +
                "3. Mocna ");
        userOption = scanner.nextInt();
        switch (userOption)
        {
            case 1:
                Imgproc.bilateralFilter(image, filterResult, 9, 75, 75);
                saveImage(filterResult, "BilateralBlur_1.jpg");
                break;
            case 2:
                Imgproc.bilateralFilter(image, filterResult, 9, 150, 150);
                saveImage(filterResult, "BilateralBlur_2.jpg");
                break;
            case 3:
                Imgproc.bilateralFilter(image, filterResult, 9, 150, 150);
                saveImage(filterResult, "BilateralBlur_3.jpg");
                break;
        }
    }
    // zobaczyć jak to się robi
    /*
    Tworzymy macierz o rozmiarze 3x3, wypełniamy ją wartościami np. 1/9 co spowoduje uśrednienie piksela a następnie
    poprzez funckję filter2D filtrujemy obraz przy użyciu naszego stworzonego kernela. Funkcja przyjmuje obraz wejściowy, wyjściowy
    głębia obrazu wyjściowego (bez zmian) oraz filtr użyty do przetwarzania a raczej nasz kernel.
     */
    public void originalFilter(Mat image)
    {
        Mat originalBlur = new Mat();
        Mat kernel = Mat.ones(3, 3, CvType.CV_32F);
        Core.multiply(kernel, new Scalar(1.0 / 9.0), kernel);
        Imgproc.filter2D(image, originalBlur, -1, kernel);

        saveImage(originalBlur,"CustomFilter_Blur.jpg");

    }

    /*
    Trzy metody wyostrzania obrazu. Na początku tworzymy kernel wyostrzający i używamy tak jak poprzednio funkcję
    filter2D. Kelner ten jest wielkości 3x3 z wartością środkową 5 oraz wartościami -1. Zwiększa on kontrast na krawędziach.
    Następnie mamy wyostrzenie przy użyciu filtru Laplaciana. Oblicza on drugą pochodną intensywności obrazu, co pozwala na wykrycie i
    wzmocnienie krawędzi. Jest to druga technika wyostrzania. Ostatnia to jest unsharp, może to mylić lecz jest to
    technika wyostrzająca z użyciem rozmycia Gaussa. Działa to na zasadzie porównania orginalnego obrazu z jego rozmytą
    wersją i wzmocnienia różnic między nimi. Jest to powszechna technika.
     */
    public void sharpening(Mat image)
    {
        Mat sharpened = new Mat();
        Mat laplacian = new Mat();
        Mat unsharp = new Mat();

        Mat kernel = new Mat(3,3, CvType.CV_32F);
        kernel.put(0, 0, 0, -1, 0, -1, 5, -1, 0, -1, 0);
        Imgproc.filter2D(image, sharpened, -1, kernel);

        // Laplacian
        Imgproc.Laplacian(image, laplacian, CvType.CV_64F);

        // Unsharp Mask
        Mat gaussianBlurImage = new Mat();
        Imgproc.GaussianBlur(image, gaussianBlurImage, new Size(9, 9), 10);
        Core.addWeighted(image, 1.5,gaussianBlurImage, -0.5, 0, unsharp);

       saveImage(sharpened, "sharpened.jpg");
       saveImage(laplacian, "laplacian.jpg");
       saveImage(unsharp, "unsharp.jpg");

    }

    /*
    ta metoda generuje szum za pomocą addSaltAndPepperNoise. Następnie pyta użytkownika o filtr i zapisuje zdjęcia.
     */
    public void saltNoise(Mat image)
    {
        noisyImage = addSaltAndPepperNoise(image, 0.05);
        saveImage(noisyImage, "saltandpepper.jpg");
        System.out.println("Wybierz jaki chcesz użyć filtr: " +
                "1. Gaussa" +
                "2. Median" +
                "3. Bilateral");
        userOption = scanner.nextInt();

        // tutaj wszystkie filtry się automatycznie zapiszą w swojej metodzie
        switch (userOption){
            case 1:
                gaussFilter(noisyImage);
                break;
            case 2:
                medianFilter(noisyImage);
                break;
            case 3:
                bilateralFilter(noisyImage);
                break;
            default:
                System.out.println("Zla opcja!");
        }
    }

    /*
    metoda, która wywołuje funkcję addGaussianNoise. Ona jest odpowiedzialna za nałożenie szumu. Później już tylko mamy
    wybór użytkownika jakiego chce użyć filtru i odpowiednie formy zapisu.
     */
    public void gaussNoise(Mat image)
    {
        noisyImage = addGaussianNoise(image, 25);
        saveImage(noisyImage, "gaussNoise.jpg");
        System.out.println("Wybierz jaki chcesz użyć filtr: " +
                "1. Gaussa" +
                "2. Median" +
                "3. Bilateral");
        userOption = scanner.nextInt();

        // tutaj wszystkie filtry się automatycznie zapiszą w swojej metodzie
        switch (userOption){
            case 1:
                gaussFilter(image);
                break;
            case 2:
                medianFilter(image);
                break;
            case 3:
                bilateralFilter(image);
                break;
            default:
                System.out.println("Zla opcja!");
        }
    }

}
