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
	interface View extends BaseView<Presenter> {

		void setLoadingIndicator(boolean active);

		void showWeathers(List<WeatherInfo> weatherInfo);

		void showWeather(String cityName);

		void showError();

		boolean isActive();

		void currentLocationError();
	}

	interface Presenter extends BasePresenter {

		void result(int requestCode, int resultCode, Intent data);

		void loadWeatherInfo(boolean forceUpdate);

		void selectCity();

		void openCityWeatherDetails(@NonNull String cityName);
	}

}
