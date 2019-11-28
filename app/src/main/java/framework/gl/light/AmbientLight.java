package framework.gl.light;

import javax.microedition.khronos.opengles.GL10;

public class AmbientLight {

    private float[] color = {0.2f, 0.2f, 0.2f, 1};


    public void setColor(float r, float g, float b, float a) {
        color = new float[] {r, g, b, a};
    }

    public void enable(GL10 gl) {
        gl.glLightModelfv(GL10.GL_LIGHT_MODEL_AMBIENT, color, 0);
    }
}
