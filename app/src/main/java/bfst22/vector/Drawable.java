package bfst22.vector;

import javafx.scene.canvas.GraphicsContext;

public interface Drawable {
    static float minX = 0;
    static float minY = 0;

    default void draw(GraphicsContext gc) {
        gc.beginPath();
        trace(gc);
        gc.stroke();
    }

    default void fill(GraphicsContext gc) {
        gc.beginPath();
        trace(gc);
        gc.fill();
    }

    void trace(GraphicsContext gc);
    void resize(double zoomlevel);

    default BoundingBox getBoundingBox() {
        return null;
    }
}
