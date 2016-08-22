package io.leftshift.weather.weatherinfo.domain.usecase;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import java.util.List;

/**
 * The type Gps tracker.
 */
@SuppressWarnings({"MissingPermission"})
public class GPSTracker extends Service implements LocationListener {

	private static final String TAG = "GPSTracker";
	private final Context mContext;

	/**
	 * The Is gps enabled.
	 */
// flag for GPS status
	boolean isGPSEnabled = false;

	/**
	 * The Is network enabled.
	 */
// flag for network status
	boolean isNetworkEnabled = false;

	/**
	 * The Can get location.
	 */
// flag for GPS status
	boolean canGetLocation = false;

	/**
	 * The Location.
	 */
	Location location; // location

	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

	/**
	 * The Location manager.
	 */
// Declaring a Location Manager
	protected LocationManager locationManager;

	private List<Address> addresses;

	/**
	 * Instantiates a new Gps tracker.
	 *
	 * @param context the context
	 */
	public GPSTracker(Context context) {
		this.mContext = context;
		getLocation();
	}

	/**
	 * Gets location.
	 *
	 * @return the location
	 */
	public Location getLocation() {
		try {
			locationManager = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
			} else {
				this.canGetLocation = true;
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d(TAG, "Network");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							double latitude = location.getLatitude();
							double longitude = location.getLongitude();
							Geocoder geocoder = new Geocoder(mContext);
							addresses = geocoder.getFromLocation(latitude, longitude, 1);
							locationManager.removeUpdates(this);
						}
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d(TAG, "GPS Enabled");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								double latitude = location.getLatitude();
								double longitude = location.getLongitude();
								Geocoder geocoder = new Geocoder(mContext);
								addresses = geocoder.getFromLocation(latitude, longitude, 1);
								locationManager.removeUpdates(this);
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}

	/**
	 * Stop using GPS listener
	 * Calling this function will stop using GPS in your app
	 */
	public void stopUsingGPS(){
		if(locationManager != null){
			locationManager.removeUpdates(GPSTracker.this);
		}		
	}


	/**
	 * Gets city name.
	 *
	 * @return the city name
	 */
	public String getCityName() {
		getLocation();
		if (addresses != null && addresses.size() > 0) {
			return addresses.get(0).getLocality();
		} else {
			return null;
		}
	}

	/**
	 * Function to check GPS/wifi enabled
	 *
	 * @return boolean boolean
	 */
	public boolean canGetLocation() {
		return this.canGetLocation;
	}

	/**
	 * Function to show settings alert dialog
	 * On pressing Settings button will lauch Settings Options
	 */
	public void showSettingsAlert(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
   	 
        // Setting Dialog Title
        alertDialog.setTitle("Is GPS Enabled ?");
 
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
 
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            	mContext.startActivity(intent);
            }
        });
 
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
 
        // Showing Alert Message
        alertDialog.show();
	}

	/**
	 * Update current location.
	 */
	public void updateCurrentLocation(){
		getLocation();
	}

	@Override
	public void onLocationChanged(Location location) {
		getLocation();
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
