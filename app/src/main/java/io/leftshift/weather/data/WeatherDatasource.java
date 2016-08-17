package io.leftshift.weather.data;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.List;

import io.leftshift.weather.weatherinfo.domain.model.WeatherInfo;

/**
 * Created by dhirajhimani on 8/17/2016.
 */
public interface WeatherDatasource {

	interface LoadTasksCallback {

		void onTasksLoaded(List<WeatherInfo> weatherInfoList);

		void onDataNotAvailable();
	}

	void getWeatherInfo(@NonNull LoadTasksCallback callback, String URL, String city) throws IOException;
}
