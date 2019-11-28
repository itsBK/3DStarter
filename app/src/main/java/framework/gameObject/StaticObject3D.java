package framework.gameObject;

import framework.math.bounds.Bounds;
import framework.math.bounds.Sphere;
import framework.math.Vector3;

public class StaticObject3D extends Bounds {

    public final Vector3 position;
    public final Bounds bounds;


    public StaticObject3D(float x, float y, float z, float radius) {
        this.position = new Vector3(x, y, z);
        this.bounds = new Sphere(x, y, z, radius);
    }
}
