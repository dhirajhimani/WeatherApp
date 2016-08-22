package io.leftshift.weather.data.source;

import android.support.annotation.NonNull;

import java.io.IOException;

import io.leftshift.weather.data.WeatherDatasource;
import io.leftshift.weather.utils.WeatherUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by dhirajhimani on 8/17/2016.
 */
public class WeatherRemoteDataSource implements WeatherDatasource {

	private static WeatherRemoteDataSource INSTANCE;

	private OkHttpClient client;

	/**
	 * Gets instance.
	 *
	 * @return the instance
	 */
	public static WeatherRemoteDataSource getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new WeatherRemoteDataSource();
		}
		return INSTANCE;
	}

	/**
	 * Instantiates a new Weather remote data source.
	 */
// Prevent direct instantiation.
	public WeatherRemoteDataSource() {
		client = new OkHttpClient();
	}


	@Override
	public void getWeatherInfo(@NonNull LoadTasksCallback callback, String URL, String city) throws IOException {


			Request request = new Request.Builder()
					.url(URL)
					.build();

			Response response = client.newCall(request).execute();
		if (response.isSuccessful()) {
			// convert Data
			callback.onTasksLoaded(WeatherUtils.parseResponse(response.body().string()));
		} else {
			callback.onDataNotAvailable();
		}
	}
}
