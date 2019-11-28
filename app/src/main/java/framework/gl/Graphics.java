package framework.gl;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.opengles.GL10;

public class Graphics {

    private GLSurfaceView glView;
    private GL10 gl;


    public Graphics(GLSurfaceView glView) {
        this.glView = glView;
    }

    public GL10 getGL() {
        return gl;
    }

    public void setGL(GL10 gl) {
        this.gl = gl;
    }

    public int getWidth() {
        return glView.getWidth();
    }

    public int getHeight() {
        return glView.getHeight();
    }
}
