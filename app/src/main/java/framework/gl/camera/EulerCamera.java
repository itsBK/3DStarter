package framework.gl.camera;

import android.opengl.GLU;
import android.opengl.Matrix;

import javax.microedition.khronos.opengles.GL10;

import framework.math.Vector3;

public class EulerCamera {

    private float yaw, pitch, pitchOffset;
    private float fieldOfView;
    private float aspectRatio;
    private float near, far;

    private final float[] matrix = new float[16];
    private final float[] inVec = {0, 0, -1, 1};
    private final float[] outVec = new float[4];

    private final Vector3 direction = new Vector3();
    private final Vector3 position = new Vector3();
    private final Vector3 up;


    public EulerCamera(float fieldOfView, float aspectRatio, float near, float far) {
        this.fieldOfView = fieldOfView;
        this.aspectRatio = aspectRatio;
        this.near = near;
        this.far = far;

        this.pitchOffset = 0;
        this.up = new Vector3(0, 1, 0);
    }

    public EulerCamera(float fieldOfView, float aspectRatio, float near, float far,
                       float pitchOffset, Vector3 up) {
        this.fieldOfView = fieldOfView;
        this.aspectRatio = aspectRatio;
        this.near = near;
        this.far = far;

        this.pitchOffset = pitchOffset;
        this.up = up;
    }

    public void setAngles(float yaw, float pitch) {
        if (pitch < -90 + pitchOffset) pitch = -90 + pitchOffset;
        if (pitch >  90 + pitchOffset) pitch =  90 + pitchOffset;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public void rotate(float yawInc, float pitchInc) {
        this.yaw += yawInc;
        this.pitch += pitchInc;
        if (pitch < -90 + pitchOffset) pitch = -90 + pitchOffset;
        if (pitch >  90 + pitchOffset) pitch =  90 + pitchOffset;
    }

    public void setMatrices(GL10 gl) {
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, fieldOfView, aspectRatio, near, far);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glRotatef(-pitch, 1, 0, 0);
        gl.glRotatef(-yaw, up.x, up.y, up.z);
        gl.glTranslatef(-position.x, -position.y, -position.z);
    }

    public Vector3 getDirection() {
        Matrix.setIdentityM(matrix, 0);
        Matrix.rotateM(matrix, 0, yaw, up.x, up.y, up.z);
        Matrix.rotateM(matrix, 0, pitch, 1, 0, 0);
        Matrix.multiplyMV(outVec, 0, matrix, 0, inVec, 0);
        direction.set(outVec[0], outVec[1], outVec[2]);
        return direction;
    }

    public Vector3 getPosition() {
        return position;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

}
