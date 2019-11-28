package framework.gl.light;

import javax.microedition.khronos.opengles.GL10;

public class DirectionalLight {

    private float[] ambient = {0.2f, 0.2f, 0.2f, 1.0f};
    private float[] diffuse = {1.0f, 1.0f, 1.0f, 1.0f};
    private float[] specular = {0.0f, 0.0f, 0.0f, 1.0f};
    private float[] direction = {0, 0, -1, 0};
    private int lastLightId = 0;


    public void setAmbient(float r, float g, float b, float a) {
        ambient = new float[] {r, g, b, a};
    }

    public void setDiffuse(float r, float g, float b, float a) {
        diffuse = new float[] {r, g, b, a};
    }

    public void setSpecular(float r, float g, float b, float a) {
        specular = new float[] {r, g, b, a};
    }

    public void setDirection(float x, float y, float z) {
        direction = new float[] {-x, -y, -z, 0};
    }


    public void enable(GL10 gl, int lightId) {
        gl.glEnable(lightId);
        gl.glLightfv(lightId, GL10.GL_AMBIENT, ambient, 0);
        gl.glLightfv(lightId, GL10.GL_DIFFUSE, diffuse, 0);
        gl.glLightfv(lightId, GL10.GL_SPECULAR, specular, 0);
        gl.glLightfv(lightId, GL10.GL_POSITION, direction, 0);
        lastLightId = lightId;
    }

    public void disable(GL10 gl) {
        gl.glDisable(lastLightId);
    }
}
