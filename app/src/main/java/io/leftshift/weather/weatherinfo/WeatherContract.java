package io.leftshift.weather.weatherinfo;

import android.content.Intent;
import android.support.annotation.NonNull;

import java.util.List;

import io.leftshift.weather.BasePresenter;
import io.leftshift.weather.BaseView;
import io.leftshift.weather.weatherinfo.domain.model.WeatherInfo;

/**
 * Created by dhirajhimani on 8/17/2016.
 */
public class WeatherContract {
	/**
	 * The interface View.
	 */
	interface View extends BaseView<Presenter> {

		/**
		 * Sets loading indicator.
		 *
		 * @param active the active
		 */
		void setLoadingIndicator(boolean active);

		/**
		 * Show weathers.
		 *
		 * @param weatherInfo the weather info
		 */
		void showWeathers(List<WeatherInfo> weatherInfo);

		/**
		 * Show weather city name.
		 *
		 * @param cityName the city name
		 */
		void showWeatherCityName(String cityName);

		/**
		 * Show error.
		 */
		void showError();

		/**
		 * Is active boolean.
		 *
		 * @return the boolean
		 */
		boolean isActive();

		/**
		 * Current location error.
		 */
		void currentLocationError();

		/**
		 * Show current location weather.
		 */
		void showCurrentLocationWeather(); }

	/**
	 * The interface Presenter.
	 */
	interface Presenter extends BasePresenter {

		/**
		 * Result.
		 *
		 * @param requestCode the request code
		 * @param resultCode  the result code
		 * @param data        the data
		 */
		void result(int requestCode, int resultCode, Intent data);

		/**
		 * Open city weather details.
		 *
		 * @param cityName the city name
		 */
		void openCityWeatherDetails(@NonNull String cityName);

		/**
		 * Gets location by gps or network.
		 */
		void getLocationByGpsOrNetwork();

	}


}
