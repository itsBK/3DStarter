package framework.UI;

public class Slider {

    public final static int VERTICAL = 0;
    public final static int HORIZONTAL = 1;


    private float x, y;
    private float startX, startY;
    private float width, height;
    private int rotation;


    public Slider(float startX, float startY, float width, float height, int rotation) {
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }

    public void onTouch(int x, int y) {
        this.x = (x - startX) / width;
        this.y = (y - startY) / height;
    }

    public float getValue() {
        if (rotation == VERTICAL)
            return y;
        else
            return x;
    }
}
