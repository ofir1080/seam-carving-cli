import java.util.ArrayList;
import java.awt.image.BufferedImage;


public class SeamCarver extends FunctionalForLoops {

    @FunctionalInterface
    private interface Resizeable {
        BufferedImage resize();
    }

    private BufferedImage img;
    private int[][] imgMat;
    private ArrayList<Integer>[] idxMat;
    private long[][] costMat;
    private int[][] bestParentMat;
    private int inHeight;
    private int inWidth;
    private int outHeight;
    private int outWidth;
    private boolean isRotated;
    private Resizeable resizeOperation;

    public SeamCarver(BufferedImage img, int outWidth, boolean isRotated) throws TooManySeamsException {

        this.img = img;
        this.inHeight = img.getHeight();
        this.inWidth = img.getWidth();
        this.outHeight = this.inHeight;
        this.outWidth = outWidth;
        if (this.outWidth > this.inWidth * 2) {
            throw new TooManySeamsException();
        }
        this.isRotated = isRotated;
        this.costMat = new long[this.inHeight][this.inHeight];
        this.setForEachParameters(this.inHeight, this.inWidth);
        this.initImgMat();
        this.initIdxMat();
        if (this.outWidth == this.inWidth) this.resizeOperation = this::getImg;
        else this.resizeOperation = ((this.outWidth > this.inWidth) ? this::increaseImg : this::reduceImg);
    }

    public BufferedImage getImg() {
        return this.img;
    }

    public BufferedImage Resize() {
        int seamsToFind = Math.abs(this.inWidth - this.outWidth);
        System.out.println("\n" + (this.isRotated ? "Horizontal" : "Vertical") + " seam paths to find: " + seamsToFind);
        return this.ResizeImage(seamsToFind);
    }

    private void initIdxMat() {
        this.idxMat = (ArrayList<Integer>[]) new ArrayList[this.inHeight];
        this.forEachHeight(y -> {
            this.idxMat[y] = new ArrayList<Integer>();
            this.forEachWidth(x -> this.idxMat[y].add(x));
        });
    }

    private void initImgMat() {
        this.imgMat = new int[this.inHeight][this.inWidth];
        this.forEach((y, x) -> this.imgMat[y][x] = this.img.getRGB(x, y));
    }

    private void fillCostMat() {
        this.costMat = new long[this.inHeight][this.idxMat[0].size()];
        this.bestParentMat = new int[this.inHeight][this.idxMat[0].size()];
        for (int y = 0; y < this.costMat.length; ++y) {
            for (int x = 0; x < this.costMat[0].length; ++x) {
                int origX = this.idxMat[y].get(x);
                this.costMat[y][x] = this.getEnergyCost(y, origX);
                long[] array = this.costMat[y];
                int n = x;
                array[n] += this.getMinCost(y, x, origX);
            }
        }
    }

    private int getEnergyCost(int y, int x) {
        int neighborIdxX = x + ((x < this.inWidth - 1) ? 1 : -1);
        int neighborIdxY = y + ((y < this.inHeight - 1) ? 1 : -1);
        int currGrayValue = this.imgMat[y][x];
        int e1 = Math.abs(currGrayValue - this.imgMat[y][neighborIdxX]);
        int e2 = Math.abs(currGrayValue - this.imgMat[neighborIdxY][x]);

        return e1 + e2;
    }

    private long getMinCost(int y, int x, int actualX) {
        int minValIdx;
        int rightLeftGrad = 0;
        int leftGrad = 0;
        int rightGrad = 0;
        long cLeft = 0;
        long cRight = 0;
        long cMiddle = 0;
        int actualLeftNeighbor = -1;
        int actualRightNeighbor = -1;

//		if (origX == 0 || origX == this.inWidth - 1) {
//			neighborCostMiddle = 0;
//		}

        if (y == 0) return 0;

        if (x > 0) actualLeftNeighbor = (int) this.idxMat[y].get(x - 1);
        if (x < this.idxMat[0].size() - 1) actualRightNeighbor = (int) this.idxMat[y].get(x + 1);

        rightLeftGrad = (x > 0 && x < this.idxMat[0].size() - 1) ?
                Math.abs(this.imgMat[y][actualLeftNeighbor] - this.imgMat[y][actualRightNeighbor]) :
                255;

        cMiddle = this.costMat[y - 1][x];

        if (x > 0) {
            cLeft = this.costMat[y - 1][x - 1];
            leftGrad = Math.abs(this.imgMat[y][actualLeftNeighbor]
                    - this.imgMat[y - 1][actualX]);
        } else cLeft = Long.MAX_VALUE / 2;

        if (x < this.costMat[0].length - 1) {
            cRight = this.costMat[y - 1][x + 1];
            rightGrad = Math.abs(this.imgMat[y][actualRightNeighbor]
                    - this.imgMat[y - 1][actualX]);
        } else cRight = Long.MAX_VALUE / 2;

        long[] results = {cLeft + leftGrad + rightLeftGrad,
                cMiddle + rightLeftGrad,
                cRight + rightGrad + rightLeftGrad};

        minValIdx = Utils.GetArgmin(results);
        this.bestParentMat[y][x] = minValIdx - 1;

        return results[minValIdx];
    }

    private void removePath(int[] chosenPath) {
        this.forEachHeight(y -> this.idxMat[y].remove(chosenPath[y]));
    }

    private int[] findMinSeam() {
        int[] chosenPath = new int[this.inHeight];
        chosenPath[chosenPath.length - 1] = Utils.GetArgmin(this.costMat[this.inHeight - 1]);
        for (int i = chosenPath.length - 1; i > 0; --i) {
            chosenPath[i - 1] = this.bestParentMat[i][chosenPath[i]];
        }

        return chosenPath;
    }

    public BufferedImage ResizeImage(int seamsToFind) {
        BufferedImage reducedImg = null;
        for (int i = 0; i < seamsToFind; ++i) {
            this.fillCostMat();
            int[] chosenPath = this.findMinSeam();
            this.removePath(chosenPath);
        }
        reducedImg = this.resizeOperation.resize();

        return reducedImg;
    }

    private BufferedImage reduceImg() {
        BufferedImage reducedImg = new BufferedImage(this.outWidth, this.outHeight, 1);
        this.setForEachParameters(this.outHeight, this.outWidth);
        this.forEach((y, x) -> reducedImg.setRGB(x, y, this.img.getRGB(this.idxMat[y].get(x), y)));

        return reducedImg;
    }

    private BufferedImage increaseImg() {
        BufferedImage resizedImg = new BufferedImage(this.outWidth, this.outHeight, 1);
        for (int y = 0; y < this.idxMat.length; ++y) {
            int colIdx = 0;
            int i = 0;
            for (int x = 0; x < this.idxMat[0].size(); ++x) {
                int jActual = this.idxMat[y].get(x);
                resizedImg.setRGB(colIdx++, y, this.img.getRGB(i++, y));
                while (i < jActual || (x == this.idxMat[0].size() - 1 && i < this.inWidth)) {
                    resizedImg.setRGB(colIdx++, y, this.img.getRGB(i, y));
                    resizedImg.setRGB(colIdx++, y, this.img.getRGB(i++, y));
                }
            }
        }

        return resizedImg;
    }
}
