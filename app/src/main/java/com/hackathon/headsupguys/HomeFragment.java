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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.content.Context.LOCATION_SERVICE;

public class HomeFragment extends Fragment implements SensorEventListener {

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
    private TextView textLight;
    private TextView textSpeed;
    private TextView textDistance;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        textLight = (TextView) view.findViewById(R.id.textLight);
        textSpeed = (TextView) view.findViewById(R.id.textSpeed);
        textDistance = (TextView) view.findViewById(R.id.textDistance);

        // 亮度传感器
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // GPS信息
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    0);
        }
        mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // 距离
                Location schoolLocation = new Location("school");
                schoolLocation.setLatitude(10.0d);
                schoolLocation.setLongitude(10.0d);
                distanceInMeters = Math.round(schoolLocation.distanceTo(location));

                if (distanceInMeters < 200)
                    textDistance.setText("Distance: Near (" + distanceInMeters + " m)");
                else
                    textDistance.setText("Distance: Away (" + distanceInMeters + " m)");

                // 速度
                if (lastLocationCheckPoint == null)
                    lastLocationCheckPoint = location;
                else if (location.getTime() - lastLocationCheckPoint.getTime() >= 5000) {
                    distanceInMeters = lastLocationCheckPoint.distanceTo(location);
                    speedInMetersPerSecond = Math.round(distanceInMeters / ((location.getTime() - lastLocationCheckPoint.getTime()) / 1000));
                    lastLocationCheckPoint = location;
                }

                if (speedInMetersPerSecond >= 0 && speedInMetersPerSecond < 1)
                    textSpeed.setText("Speed: Still (" + speedInMetersPerSecond + " m/s)");
                else if (speedInMetersPerSecond >= 1 && speedInMetersPerSecond < 4)
                    textSpeed.setText("Speed: Walking (" + speedInMetersPerSecond + " m/s)");
                else if (speedInMetersPerSecond >= 4 && speedInMetersPerSecond < 200)
                    textSpeed.setText("Speed: Driving (" + speedInMetersPerSecond + " m/s)");
                else
                    textSpeed.setText("Speed: Other (" + speedInMetersPerSecond + " m/s)");

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
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        return view;
    }

    @Override
    public void onResume() {
        // 亮度传感器
        super.onResume();
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // 亮度传感器
        int sensorValueLight = Math.round(event.values[0]);
        if (sensorValueLight < 200)
            textLight.setText("Light: Very Dark (" + String.valueOf(sensorValueLight) + " lux)");
        else if (sensorValueLight < 500)
            textLight.setText("Light: Dim (" + String.valueOf(sensorValueLight) + " lux)");
        else
            textLight.setText("Light: Bright (" + String.valueOf(sensorValueLight) + " lux)");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
