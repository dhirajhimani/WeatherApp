package io.leftshift.weather.data;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.List;

import io.leftshift.weather.weatherinfo.domain.model.WeatherInfo;

/**
 * Created by dhirajhimani on 8/17/2016.
 */
public interface WeatherDatasource {

	/**
	 * The interface Load tasks callback.
	 */
	interface LoadTasksCallback {

		/**
		 * On tasks loaded.
		 *
		 * @param weatherInfoList the weather info list
		 */
		void onTasksLoaded(List<WeatherInfo> weatherInfoList);

		/**
		 * On data not available.
		 */
		void onDataNotAvailable();
	}

	/**
	 * Gets weather info.
	 *
	 * @param callback the callback
	 * @param URL      the url
	 * @param city     the city
	 * @throws IOException the io exception
	 */
	void getWeatherInfo(@NonNull LoadTasksCallback callback, String URL, String city) throws IOException;
}
