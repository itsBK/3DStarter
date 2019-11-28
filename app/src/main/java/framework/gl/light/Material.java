package framework.gl.light;

import javax.microedition.khronos.opengles.GL10;

public class Material {

    private float[] ambient = {0.2f, 0.2f, 0.2f, 1.0f};
    private float[] diffuse = {1.0f, 1.0f, 1.0f, 1.0f};
    private float[] specular = {0.0f, 0.0f, 0.0f, 1.0f};


    public void setAmbient(float r, float g, float b, float a) {
        ambient = new float[] {r, g, b, a};
    }

    public void setDiffuse(float r, float g, float b, float a) {
        diffuse = new float[] {r, g, b, a};
    }

    public void setSpecular(float r, float g, float b, float a) {
        specular = new float[] {r, g, b, a};
    }


    public void enable(GL10 gl) {
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, ambient, 0);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, diffuse, 0);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, specular, 0);
    }

}
