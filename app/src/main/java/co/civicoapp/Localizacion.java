package co.civicoapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class Localizacion {

    private Context mContext;
    public LocationManager locManager;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    public LocationListener locListenerNETWORK = null;
    public LocationListener locListenerGPS = null;
    public Location loc;

    public Localizacion(Context m) {
        this.mContext = m;
        locManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        loc = null;
    }

    public Boolean tieneGPS(){
        isGPSEnabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Boolean retorn = false;

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(mContext, "No tiene habilitado el GPS o no esta instalado.", Toast.LENGTH_LONG).show();
            return false;
        }else if (!isGPSEnabled) {
            Toast.makeText(mContext, "No tiene habilitado el GPS o no esta instalado.", Toast.LENGTH_LONG).show();
            return false;
        }
        if(isGPSEnabled || isNetworkEnabled) retorn = true;
        if(retorn) comenzarLocalizacion();
        return retorn;
    }

    public void comenzarLocalizacion() {
        locManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        loc = null;
        if (!isGPSEnabled && !isNetworkEnabled) {
            Toast.makeText(mContext, "No tiene habilitado el GPS o no esta instalado.", Toast.LENGTH_LONG).show();
            return;
        }
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(mContext, "No tiene habilitado el GPS o no esta instalado.", Toast.LENGTH_LONG).show();
            return;
        }
        locListenerNETWORK = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(isBetterLocation(location,loc)){
                    loc = location;
                }
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
        };
        locListenerGPS = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(isBetterLocation(location,loc)){
                    loc = location;
                }
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
        };
        if (isNetworkEnabled) {
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locListenerNETWORK);
            loc = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }// if GPS Enabled get lat/long using GPS Services
        if (isGPSEnabled) {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locListenerGPS);
            loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
    }
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        int TWO_MINUTES = 100 * 60 * 2;
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }
        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;
        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }
        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;
        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),currentBestLocation.getProvider());
        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }
}