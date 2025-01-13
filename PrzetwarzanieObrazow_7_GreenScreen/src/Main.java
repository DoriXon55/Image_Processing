import org.opencv.core.Core;

import java.util.Scanner;

public class Main{
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    }
    public static void main(String[] args) {
        OpenCvFunctions openCvFunctions = new OpenCvFunctions();
        Scanner scanner = new Scanner(System.in);
        System.out.println("""
                Wybierz filtr:
                1. Użyj filtru Gaussa
                2. Użyj filtru Medianowego
                3. Użyj morfologicznego otwarcia i domknięcia
                """);
        int userOption = scanner.nextInt();
        openCvFunctions.algorithm(userOption);
        // nie dziala
        openCvFunctions.applyFrame("C:\\Users\\doria\\IdeaProjectsp\\PrzetwarzanieObrazow_7_GreenScreen\\src\\the-leon-dicaprio-produced-captain-planet-should-leave-out-gaia-20190906005544.jpg", "C:\\Users\\doria\\IdeaProjectsp\\PrzetwarzanieObrazow_7_GreenScreen\\src\\Pngtreeframe_border_rectangle_golden_6538238.png");
        openCvFunctions.openCamera();
        openCvFunctions.detectFace();


    }

}
