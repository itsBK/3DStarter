package framework.gameObject;

import framework.math.Vector2;

public class DynamicObject2D extends StaticObject2D {

    public final Vector2 velocity;
    public final Vector2 accel;


    public DynamicObject2D(float centerX, float centerY, float width, float height) {
        super(centerX, centerY, width, height);
        velocity = new Vector2();
        accel = new Vector2();
    }


    public DynamicObject2D(float centerX, float centerY, float radius) {
        super(centerX, centerY, radius);
        velocity = new Vector2();
        accel = new Vector2();
    }
}
