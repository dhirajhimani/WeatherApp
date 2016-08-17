package io.leftshift.weather.utils;

import android.util.Log;

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

	public static List<WeatherInfo> parseresponse(String response) {
		Log.d(TAG, response);
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
}
