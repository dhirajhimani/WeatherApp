package io.leftshift.weather.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.leftshift.weather.weatherinfo.domain.model.WeatherInfo;

/**
 * Created by dhirajhimani on 8/17/2016.
 */
public class WeatherUtils {

	private static final String TAG = "WeatherUtils";

	/**
	 * Parse response list.
	 *
	 * @param response the response
	 * @return the list
	 */
	public static List<WeatherInfo> parseResponse(String response) {
		List<WeatherInfo> weatherInfos = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONArray tag_list_array = jsonObject.getJSONArray("list");
			for (int i = 0 ; i < tag_list_array.length() ; i++) {
				JSONObject jsonObject1 = tag_list_array.getJSONObject(i);
				WeatherInfo weatherInfo = new WeatherInfo();
				weatherInfo.setTimestamp(jsonObject1.getLong("dt")  * 1000L);
				weatherInfo.setWeatherStatus(jsonObject1.getJSONArray("weather").getJSONObject(0).getString("main"));
				weatherInfos.add(weatherInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


		return weatherInfos;
	}

	/**
	 * Format date string.
	 *
	 * @param mTimestamp the m timestamp
	 * @return the string
	 */
	public static String formatDate(long mTimestamp) {
		String dateTime = new SimpleDateFormat("MMM d, yyyy").format(new Date(mTimestamp));
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(mTimestamp);
		Calendar today = Calendar.getInstance();
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.add(Calendar.DATE, +1);
		if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
			return "Today";
		} else if (calendar.get(Calendar.YEAR) == tomorrow.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == tomorrow.get(Calendar.DAY_OF_YEAR)) {
			return "Tomorrow";
		} else {
			return dateTime;
		}
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager
				= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	/**
	 * Function to show internet settings alert dialog
	 * On pressing Settings button will launch Settings Options
	 */
	public static void showInternetSettingsAlert(final Context context){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

		// Setting Dialog Title
		alertDialog.setTitle("Is Internet Available ?");

		// Setting Dialog Message
		alertDialog.setMessage("Internet is not enabled.Do you want to go to settings menu?");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
				context.startActivity(intent);
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
}
