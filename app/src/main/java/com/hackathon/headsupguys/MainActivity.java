package com.hackathon.headsupguys;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    // 亮度传感器
    private SensorManager mSensorManager;
    private Sensor mLight;

    // GPS信息
    private LocationManager mLocationManager;
    private LocationListener locationListener;
    private Location lastLocationCheckPoint = null;
    float distanceInMeters;
    int speedInMetersPerSecond;

    // UI控件
    private TextView mTextMessage;
    private TextView mTextMessage2;
    private TextView mTextMessage3;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        mTextMessage2 = (TextView) findViewById(R.id.message2);
        mTextMessage3 = (TextView) findViewById(R.id.message3);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // 亮度传感器
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // GPS信息
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // 距离
                Location schoolLocation = new Location("school");
                schoolLocation.setLatitude(10.0d);
                schoolLocation.setLongitude(10.0d);
                distanceInMeters = Math.round(schoolLocation.distanceTo(location));

                if (distanceInMeters > 200)
                    mTextMessage3.setText("不在学校：" + distanceInMeters + "米");
                else
                    mTextMessage3.setText("在学校");

                // 速度
                if (lastLocationCheckPoint == null)
                    lastLocationCheckPoint = location;
                else if (location.getTime() - lastLocationCheckPoint.getTime() >= 5000) {
                    distanceInMeters = lastLocationCheckPoint.distanceTo(location);
                    speedInMetersPerSecond = Math.round(distanceInMeters / ((location.getTime() - lastLocationCheckPoint.getTime()) / 1000));
                    lastLocationCheckPoint = location;
                }

                if (speedInMetersPerSecond >= 0 && speedInMetersPerSecond < 1)
                    mTextMessage2.setText("静止: " + speedInMetersPerSecond + "米/秒");
                else if (speedInMetersPerSecond >= 1 && speedInMetersPerSecond < 4)
                    mTextMessage2.setText("步行: " + speedInMetersPerSecond + "米/秒");
                else if (speedInMetersPerSecond >= 4 && speedInMetersPerSecond < 200)
                    mTextMessage2.setText("行车: " + speedInMetersPerSecond + "米/秒");
                else
                    mTextMessage2.setText("错误: " + speedInMetersPerSecond + "米/秒");

            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider) {
            }
            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    0);
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

    }


    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float sensorValueLight = event.values[0];
        mTextMessage.setText("亮度：" + String.valueOf(sensorValueLight));
    }

    @Override
    protected void onResume() {
        // 亮度传感器
        super.onResume();
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
