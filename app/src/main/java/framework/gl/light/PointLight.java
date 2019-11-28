package framework.gl.light;

import javax.microedition.khronos.opengles.GL10;

import framework.math.Vector3;

public class PointLight {

    private float[] ambient = {0.2f, 0.2f, 0.2f, 1.0f};
    private float[] diffuse = {1.0f, 1.0f, 1.0f, 1.0f};
    private float[] specular = {0.0f, 0.0f, 0.0f, 1.0f};
    private float[] position = {0, 0, 0, 1};
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

    public void setPosition(float x, float y, float z) {
        position = new float[] {x, y, z, 1};
    }

    public void setPosition(Vector3 position) {
        this.position = new float[] {position.x, position.y, position.z, 1};
    }


    public void enable(GL10 gl, int lightId) {
        gl.glEnable(lightId);
        gl.glLightfv(lightId, GL10.GL_AMBIENT, ambient, 0);
        gl.glLightfv(lightId, GL10.GL_DIFFUSE, diffuse, 0);
        gl.glLightfv(lightId, GL10.GL_SPECULAR, specular, 0);
        gl.glLightfv(lightId, GL10.GL_POSITION, position, 0);
        lastLightId = lightId;
    }

    public void disable(GL10 gl) {
        gl.glDisable(lastLightId);
    }
}
