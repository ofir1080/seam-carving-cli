import java.util.function.Consumer;
import java.util.function.BiConsumer;


public abstract class FunctionalForLoops {
    private int height;
    private int width;

    public void setForEachParameters(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public void forEach(BiConsumer<Integer, Integer> action) {
        this.forEachHeight(y -> this.forEachWidth(x -> action.accept(y, x)));
    }

    public void forEachWidth(Consumer<Integer> action) {
        for (int x = 0; x < this.width; ++x) {
            action.accept(x);
        }
    }

    public void forEachHeight(Consumer<Integer> action) {
        for (int y = 0; y < this.height; ++y) {
            action.accept(y);
        }
    }
}