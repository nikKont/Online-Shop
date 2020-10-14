package com.example.myonlineshop.Buyers;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myonlineshop.R;

public class SensorsActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG ="SensorsActivity";

    private SensorManager sensorManager;

    private Sensor mGyro, mMagno, mPressure, mHumidity, mLight, mTemperature;

    TextView xGyroValue, yGyroValue, zGyroValue;
    TextView xMagneValue, yMagneValue, zMagneValue;
    TextView light, pressure, temperature, humidity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        xGyroValue=(TextView)findViewById(R.id.xGyroValue);
        yGyroValue=(TextView)findViewById(R.id.yGyroValue);
        zGyroValue=(TextView)findViewById(R.id.zGyroValue);

        xMagneValue=(TextView)findViewById(R.id.xMagneValue);
        yMagneValue=(TextView)findViewById(R.id.yMagneValue);
        zMagneValue=(TextView)findViewById(R.id.zMagneValue);

        light=(TextView)findViewById(R.id.light);
        pressure=(TextView)findViewById(R.id.pressure);
        temperature=(TextView)findViewById(R.id.temperature);
        humidity=(TextView)findViewById(R.id.humidity);

        Log.d(TAG,"Initializing Sensor Services!!!");
        sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);

        //Gyroscope
        mGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (mGyro!= null){
            sensorManager.registerListener( SensorsActivity.this,mGyro,SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG,"Registered Gyroscope!!");
        }else{
            xGyroValue.setText("Gyroscope not supported!!");
            yGyroValue.setText("Gyroscope not supported!!");
            zGyroValue.setText("Gyroscope not supported!!");
        }

        //Magnetometer
        mMagno = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if(mMagno != null){
            sensorManager.registerListener( SensorsActivity.this,mMagno,SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG,"Registered Magnetometer!!");
        }else{
            xMagneValue.setText("Magnetometer not supported!!");
            yMagneValue.setText("Magnetometer not supported!!");
            zMagneValue.setText("Magnetometer not supported!!");
        }

        //light
        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(mLight != null){
            sensorManager.registerListener( SensorsActivity.this,mLight,SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG,"Registered Light Measurement!!");
        }else{
           light.setText("Light Measurement not supported!!");
        }

        //pressure
        mPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if(mPressure != null){
            sensorManager.registerListener( SensorsActivity.this,mPressure,SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG,"Registered Pressure Measurement!!");
        }else{
            pressure.setText("Pressure Measurement not supported!!");
        }

        //temperature
        mTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if(mTemperature != null){
            sensorManager.registerListener( SensorsActivity.this,mTemperature,SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG,"Registered Temperature!!");
        }else{
            temperature.setText("Temperature not supported!!");
        }

        //humidity
        mHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if(mHumidity != null){
            sensorManager.registerListener( SensorsActivity.this,mHumidity,SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG,"Registered Humidity!!");
        }else{
            humidity.setText("Humidity not supported!!");
        }



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor= event.sensor;
//        Log.d(TAG,"onSensorChanged: X: " + event.values[0] + "Y: " + event.values[1] + "Z: " + event.values[2]);

        if(sensor.getType()==Sensor.TYPE_GYROSCOPE){
            xGyroValue.setText("xGyroValue: " + event.values[0]);
            yGyroValue.setText("yGyroValue: " + event.values[1]);
            zGyroValue.setText("zGyroValue: " + event.values[2]);

        }else if(sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
            xMagneValue.setText("xMagneValue: " + event.values[0]);
            yMagneValue.setText("yMagneValue: " + event.values[1]);
            zMagneValue.setText("zMagneValue: " + event.values[2]);

        }else if(sensor.getType()==Sensor.TYPE_LIGHT){
            light.setText("Light: " + event.values[0]);

        }else if(sensor.getType()==Sensor.TYPE_PRESSURE){
            pressure.setText("Pressure: " + event.values[0]);

        }else if(sensor.getType()==Sensor.TYPE_AMBIENT_TEMPERATURE){
            temperature.setText("Temperature: " + event.values[0]);

        }else if(sensor.getType()==Sensor.TYPE_RELATIVE_HUMIDITY){
            humidity.setText("Humidity: " + event.values[0]);

        }
    }
}
