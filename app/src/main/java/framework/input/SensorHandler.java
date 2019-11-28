package framework.input;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorHandler implements SensorEventListener {

    public static final int TYPE_ACCELEROMETER = Sensor.TYPE_ACCELEROMETER;
    public static final int TYPE_GYROSCOPE = Sensor.TYPE_GYROSCOPE;
    public static final int TYPE_ORIENTATION = Sensor.TYPE_ORIENTATION;

    private float sensorX;
    private float sensorY;
    private float sensorZ;


    public SensorHandler(Context context, int sensorType) {
        SensorManager manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if(manager.getSensorList(sensorType).size() != 0) {
            Sensor sensor = manager.getSensorList(sensorType).get(0);
            manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        sensorX = event.values[0];
        sensorY = event.values[1];
        sensorZ = event.values[2];
    }

    public float getSensorX() {
        return sensorX;
    }

    public float getSensorY() {
        return sensorY;
    }

    public float getSensorZ() {
        return sensorZ;
    }
}
