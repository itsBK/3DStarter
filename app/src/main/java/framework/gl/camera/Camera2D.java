package framework.gl.camera;

import javax.microedition.khronos.opengles.GL10;

import framework.gl.Graphics;
import framework.math.Vector2;

public class Camera2D {

    private final float frustumWidth;
    private final float frustumHeight;
    private final Graphics graphics;

    private final Vector2 position;
    public float zoom;


    public Camera2D(Graphics graphics, float frustumWidth, float frustumHeight) {
        this.graphics = graphics;
        this.frustumWidth = frustumWidth;
        this.frustumHeight = frustumHeight;
        this.position = new Vector2(frustumWidth / 2, frustumHeight / 2);
        this.zoom = 1.0f;
    }

    public void setMatrices() {
        GL10 gl = graphics.getGL();
        gl.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());

        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(position.x - frustumWidth * zoom / 2,
                    position.x + frustumWidth * zoom / 2,
                    position.y - frustumHeight * zoom / 2,
                    position.y + frustumHeight * zoom / 2,
                    1, -1);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void touchToWorld(Vector2 touch) {
        touch.x = (touch.x / (float) graphics.getWidth()) * frustumWidth * zoom;
        touch.y = (1 - touch.y / (float) graphics.getHeight()) * frustumHeight * zoom;
        touch.add(position).sub(frustumWidth * zoom / 2, frustumHeight * zoom / 2);
    }

    public Vector2 getPosition() {
        return position;
    }

}
