import java.awt.image.BufferedImage;


public class Utils {
    public static int GetArgmin(long[] arr) {
        long minVal = Long.MAX_VALUE;
        int minIdx = -1;
        for (int i = 0; i < arr.length; ++i) {
            if (arr[i] < minVal) {
                minVal = arr[i];
                minIdx = i;
            }
        }
        return minIdx;
    }

    public static BufferedImage RotateClockwise(BufferedImage img) {
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        BufferedImage ans = new BufferedImage(imgHeight, imgWidth, img.getType());
        for (int y = 0; y < imgWidth; ++y) {
            for (int x = 0; x < imgHeight; ++x) {
                int imgX = y;
                int imgY = imgHeight - 1 - x;
                ans.setRGB(x, y, img.getRGB(imgX, imgY));
            }
        }
        return ans;
    }

    public static BufferedImage RotateCounterclockwise(BufferedImage img) {
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        BufferedImage ans = new BufferedImage(imgHeight, imgWidth, img.getType());
        for (int y = 0; y < imgWidth; ++y) {
            for (int x = 0; x < imgHeight; ++x) {
                int imgX = imgWidth - 1 - y;
                int imgY = x;
                ans.setRGB(x, y, img.getRGB(imgX, imgY));
            }
        }
        return ans;
    }
}