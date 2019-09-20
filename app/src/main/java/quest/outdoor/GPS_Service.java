package quest.outdoor;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

import androidx.annotation.Nullable;

public class GPS_Service extends Service {

    private LocationListener listener;
    private LocationManager locationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                ////when there is a location update method onLocationChanged called
                //we need to transfer the data from this method to the corresponding activity

                //we'll use broadcast receiver
                //we create the custom intent, attach some data to it
                //and than broadcast that intent

                //we need to provide an intent filter
                Intent i = new Intent("location_update"); //to recognize recieved in a corresponding activity intent
                String coord = "lat:" + location.getLatitude() + "\nlon:" + location.getLongitude();
                i.putExtra("coordinates", coord);
                //to broadcast the intent
                sendBroadcast(i);

                //next register the receiver in the corresponding activity

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                //if the location services are disabled on the phone
                //we should point user to the settings panel to enable them
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        //initialise location manager with the location getSystem manager
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,listener);
    }

    //when service is destroyed (stopped)
    //we should destroy listener to avoid memory (and battery?) leaks
    @Override
    public void onDestroy() {
        super.onDestroy();
        //unregister the listener
        if(locationManager != null)
        {
            locationManager.removeUpdates(listener);
        }
    }
}
