import org.opencv.core.CvType;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageDownloader {

    public BufferedImage downloadImage() throws IOException {
        String urlString = "https://picsum.photos/500/500";
        URL url = new URL(urlString);
        return ImageIO.read(url);
    }

    public Mat bufferedImageToMat(BufferedImage bi) {
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        byte[] data = ((java.awt.image.DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }

    public BufferedImage matToBufferedImage(Mat mat) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (mat.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage img = new BufferedImage(mat.width(), mat.height(), type);
        byte[] data = new byte[mat.rows() * mat.cols() * (int) mat.elemSize()];
        mat.get(0, 0, data);
        img.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);
        return img;
    }
}
