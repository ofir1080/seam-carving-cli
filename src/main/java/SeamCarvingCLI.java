import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Scanner;

public class SeamCarvingCLI {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BufferedImage srcImg = null;
        try {
            srcImg = ImageIO.read(new File(args[0]));
            System.out.println("Image read\n");
            System.out.print("new height (original is " + srcImg.getHeight() + "):\t");
            int outHeight = Integer.parseInt(sc.nextLine());
            System.out.print("new width (original is " + srcImg.getWidth() + "):\t");
            int outWidth = Integer.parseInt(sc.nextLine());
            runSeamCurving(srcImg, outHeight, outWidth);
            System.exit(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private static void runSeamCurving(BufferedImage img, int outHeight, int outWidth) throws TooManySeamsException {
        BufferedImage gsImg = toGrayscale(img);
        SeamCarver sc = new SeamCarver(img, outWidth, false);
        img = sc.Resize();
        sc = new SeamCarver(Utils.RotateClockwise(img), outHeight, true);
        img = sc.Resize();
        img = Utils.RotateCounterclockwise(img);
        try {
            ImageIO.write(img, "png", new File("./resized_image.png"));
            System.out.println("\nImaged saved in working directory");
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    // TODO: implement
    private static BufferedImage toGrayscale(BufferedImage img) {
        return null;
    }
}