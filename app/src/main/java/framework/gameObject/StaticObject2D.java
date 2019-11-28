package framework.gameObject;

import framework.math.bounds.Bounds;
import framework.math.bounds.Circle;
import framework.math.bounds.Rectangle;
import framework.math.Vector2;

public class StaticObject2D {

    public final Vector2 position;
    public final Bounds bounds;


    public StaticObject2D(float centerX, float centerY, float width, float height) {
        this.position = new Vector2(centerX, centerY);
        this.bounds = new Rectangle(centerX - width/2, centerY - height/2, width, height);
    }


    public StaticObject2D(float centerX, float centerY, float radius) {
        this.position = new Vector2(centerX, centerY);
        this.bounds = new Circle(centerX, centerY, radius);
    }
}
