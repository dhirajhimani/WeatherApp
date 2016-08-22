package io.leftshift.weather.data;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.leftshift.weather.weatherinfo.domain.model.WeatherInfo;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by dhirajhimani on 8/17/2016.
 */
public class WeatherRepository implements WeatherDatasource {

	private static WeatherRepository INSTANCE = null;

	private final WeatherDatasource mWeatherRemoteDataSource;

	/**
	 * This variable has package local visibility so it can be accessed from tests.
	 */
	private Map<String, ArrayList<WeatherInfo>> mCachedWeatherList;

	/**
	 * Marks the cache as invalid, to force an update the next time data is requested.
	 */
	private boolean mCacheIsDirty = false;

	// Prevent direct instantiation.
	private WeatherRepository(@NonNull WeatherDatasource weatherRemoteDataSource) {
		mWeatherRemoteDataSource = checkNotNull(weatherRemoteDataSource);
	}

	/**
	 * Returns the single instance of this class, creating it if necessary.
	 *
	 * @param weatherRemoteDataSource the weather remote data source
	 * @return the instance
	 */
	public static WeatherRepository getInstance(WeatherDatasource weatherRemoteDataSource) {
		if (INSTANCE == null) {
			INSTANCE = new WeatherRepository(weatherRemoteDataSource);
		}
		return INSTANCE;
	}

	/**
	 * Destroy instance.
	 */
	public static void destroyInstance() {
		INSTANCE = null;
	}


	@Override
	public void getWeatherInfo(@NonNull LoadTasksCallback callback, String URL, String city) throws IOException {
		if (mCachedWeatherList != null && mCachedWeatherList.containsKey(city)) {
			callback.onTasksLoaded(mCachedWeatherList.get(city));
		}
		if (mCachedWeatherList == null) {
			mCachedWeatherList = new HashMap<>();
		}
		mWeatherRemoteDataSource.getWeatherInfo(callback, URL, city);
	}
}
