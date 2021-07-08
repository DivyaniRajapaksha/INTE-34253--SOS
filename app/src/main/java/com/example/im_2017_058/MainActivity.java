package com.example.im_2017_058;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity {
    private LocationListener locationListener;
    private LocationManager locationManager;
    private final long MIN_TIME=1000; //minimum time interval between location updates, in milliseconds
    private final long MIN_DISTANCE=5; //minimum distance between location updates, in meters
    private LatLng latLng;
    private String lat;
    private String lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                PackageManager.PERMISSION_GRANTED);
        getLocation();
    }
    public void sendAlert(View view) {
        EditText edit = (EditText) findViewById(R.id.contactNo);
        String name = edit.getText().toString();
        Log.d("data", name);
        getLocation();
        String message ="Divyani-058 http://maps.google.com/?q=" + lat + "," + lng;
        try {
            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage(name,null,message,null,null);
            Toast.makeText(getApplicationContext(), "SMS Sent to " + name, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS Sending Failed", Toast.LENGTH_LONG).show();
        }
    }

    public void sendSMS(String contactNo){

        try{
            if(!contactNo.equals("") && (!lat.isEmpty() && !lng.isEmpty())){
                SmsManager smgr = SmsManager.getDefault();
                smgr.sendTextMessage(contactNo,null,"Hello",null,null);
                Toast.makeText(getApplicationContext(),"SMS Sent to "+contactNo,Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"SMS Sending Failed",Toast.LENGTH_LONG).show();
        }
    }
    public void getLocation() {

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());

                    Log.d("lat", Double.toString(location.getLatitude()));
                    Log.d("lng", Double.toString(location.getLongitude()));

                    lat = Double.toString(location.getLatitude());
                    lng = Double.toString(location.getLongitude());
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
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


        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}