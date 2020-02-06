package symphome.de.locationtracker;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class LocationService extends Service implements LocationListener {

    private static final long MIN_TIME_UPDATES = 1000 * 6;
    private static final long MIN_DISTANCE_UPDATES = 10;
    public static final String ACTION_LOCATION_BC = "location_bc";
    private Location location;

    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        boolean isGPSEnabled =
                locationManager.isProviderEnabled(
                        LocationManager.GPS_PROVIDER);

        if (isGPSEnabled) {
            try{
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, MIN_TIME_UPDATES,
                        MIN_DISTANCE_UPDATES, this);

                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } catch (SecurityException e) {}
        }

        boolean isNetworkEnabled =
                locationManager.isProviderEnabled(
                        LocationManager.NETWORK_PROVIDER);

        if (isNetworkEnabled) {
            try{
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, MIN_TIME_UPDATES,
                        MIN_DISTANCE_UPDATES, this);

                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            } catch (SecurityException e) {

            }
        }

        if(location != null) {
            Intent i = new Intent(ACTION_LOCATION_BC);
            i.putExtra("long", location.getLongitude());
            i.putExtra("lat", location.getLatitude());
            this.sendBC(i);
        }

        return Service.START_STICKY;
    }

    private void sendBC(Intent intent) {
        sendBroadcast(intent);
    }

    @Override
    public void onLocationChanged(Location location) {
        // Broadcast senden
        Intent i = new Intent(ACTION_LOCATION_BC);
        i.putExtra("long", location.getLongitude());
        i.putExtra("lat", location.getLatitude());
        this.sendBC(i);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

//    public class MyBinder extends Binder{
//        public LocationService getBinder() {
//            return LocationService.this;
//        }
//
//        private final IBinder myBinder = new MyBinder();
//        public IBinder onBind(Intent intent) {
//            return myBinder;
//        }
//    }
}



