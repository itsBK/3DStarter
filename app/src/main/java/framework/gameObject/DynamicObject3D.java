package framework.gameObject;

import framework.math.Vector3;

public class DynamicObject3D extends StaticObject3D {

    public final Vector3 velocity;
    public final Vector3 accel;


    public DynamicObject3D(float x, float y, float z, float radius) {
        super(x, y, z, radius);
        velocity = new Vector3();
        accel = new Vector3();
    }
}
