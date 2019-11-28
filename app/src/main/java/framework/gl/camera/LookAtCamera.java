package framework.gl.camera;

import android.opengl.GLU;

import javax.microedition.khronos.opengles.GL10;

import framework.math.Vector3;

public class LookAtCamera {

    private float fieldOfView;
    private float aspectRatio;
    private float near, far;

    private final Vector3 position;
    private final Vector3 up;
    private final Vector3 lookAt;


    public LookAtCamera(float fieldOfView, float aspectRatio, float near, float far) {
        this.fieldOfView = fieldOfView;
        this.aspectRatio = aspectRatio;
        this.near = near;
        this.far = far;

        position = new Vector3();
        lookAt = new Vector3();
        up = new Vector3(0, 1, 0);
    }

    public void setMatrices(GL10 gl) {
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, fieldOfView, aspectRatio, near, far);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        GLU.gluLookAt(gl, position.x, position.y, position.z,
                      lookAt.x, lookAt.y, lookAt.z,
                      up.x, up.y, up.z);
    }

    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getUp() {
        return up;
    }

    public Vector3 getLookAt() {
        return lookAt;
    }
}
