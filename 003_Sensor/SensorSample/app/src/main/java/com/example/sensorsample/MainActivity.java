package com.example.sensorsample;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import static android.hardware.Sensor.TYPE_LIGHT;
import static android.hardware.Sensor.TYPE_ORIENTATION;
import static android.hardware.Sensor.TYPE_PROXIMITY;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor mLightSensor;
    private Sensor mProximitySensor;
    private Sensor mOrientationSensor;
    private TextView mLightTextView;
    private TextView mProximityTextView;
    private TextView mOrientationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView sensorTextView = findViewById(R.id.sensor_textview);
        mLightTextView = findViewById(R.id.light_textview);
        mProximityTextView = findViewById(R.id.proximity_textview);
        mOrientationTextView = findViewById(R.id.orientation_textview);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        StringBuilder sensorText = new StringBuilder();
        for (Sensor sensor : sensorList) {
            sensorText.append(sensor.getName() + "\n");
        }

        sensorTextView.setText(sensorText);
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mProximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mOrientationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        if(mLightSensor == null){
            mLightTextView.setText("Light Sensor Not Exist");
        }

        if(mProximitySensor == null){
            mProximityTextView.setText("Proximity Sensor Not Exist");
        }

        if(mOrientationSensor == null){
            mOrientationTextView.setText("Orientation Sensor Not Exist");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mLightSensor != null){
            mSensorManager.registerListener(this, mLightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if(mProximitySensor!=null){
            mSensorManager.registerListener(this, mProximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if(mOrientationSensor!=null){
            mSensorManager.registerListener(this, mOrientationSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        int sensorType = event.sensor.getType();
        float currentValue = event.values[0];
        switch (sensorType){
            case TYPE_LIGHT:
                mLightTextView.setText(getResources().getString(R.string.label_light_sensor, currentValue));
                break;
            case TYPE_PROXIMITY:
                mProximityTextView.setText(getResources().getString(R.string.label_proximity_sensor, currentValue));
                break;
            case TYPE_ORIENTATION:
                mOrientationTextView.setText(getResources().getString(R.string.label_orientation_sensor, event.values[0], event.values[1], event.values[2]));
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    private void getOrientation(float[] accelerometerReading, float[] magnetometerReading){

        // Rotation matrix based on current readings from accelerometer and magnetometer.
        final float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading);
        // Express the updated rotation matrix as three orientation angles.
        final float[] orientationAngles = new float[3];
        SensorManager.getOrientation(rotationMatrix, orientationAngles);
    }

}
