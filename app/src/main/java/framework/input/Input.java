package framework.input;

import android.content.Context;
import android.view.View;

import java.util.List;

public class Input {

    public static class KeyEvent {
        public static final int KEY_DOWN = 0;
        public static final int KEY_UP = 1;

        public int type;
        public int keyCode;
        public char keyChar;
    }

    public static class TouchEvent {
        public static final int TOUCH_DOWN = 0;
        public static final int TOUCH_UP = 1;
        public static final int TOUCH_DRAGGED = 2;

        public int pointer;
        public int type;
        public int x, y;
    }

    private SensorHandler sensorHandler;
    private KeyboardHandler keyHandler;
    private TouchHandler touchHandler;


    public Input(Context context, View view, float scaleX, float scaleY, int maxPoolSize, int sensorType) {
        sensorHandler = new SensorHandler(context, sensorType);
        keyHandler = new KeyboardHandler(view, maxPoolSize);
        touchHandler = new TouchHandler(view, scaleX, scaleY, maxPoolSize);
    }

    public boolean isKeyPressed(int keyCode) {
        return keyHandler.isKeyPressed(keyCode);
    }

    public boolean isTouchDown(int pointer) {
        return touchHandler.isTouchDown(pointer);
    }

    public int getTouchX(int pointer) {
        return touchHandler.getTouchX(pointer);
    }

    public int getTouchY(int pointer) {
        return touchHandler.getTouchY(pointer);
    }

    public float getSensorX() {
        return sensorHandler.getSensorX();
    }

    public float getSensorY() {
        return sensorHandler.getSensorY();
    }

    public float getSensorZ() {
        return sensorHandler.getSensorZ();
    }

    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }

    public List<KeyEvent> getKeyEvents() {
        return keyHandler.getKeyEvents();
    }
}
