package framework.UI;

import javax.microedition.khronos.opengles.GL10;

import framework.gl.vertices.Vertices;

public class Button {

    private Vertices vertices;
    private float startX, startY;
    private float width, height;
    private boolean value = false;


    public Button(float startX, float startY, float width, float height, boolean value) {
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
        this.value = value;
    }

    public void onTouch() {
        value = !value;
    }

    public boolean getValue() {
        return value;
    }

    public void draw(GL10 gl) {
    }
}
