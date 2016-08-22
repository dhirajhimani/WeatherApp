package io.leftshift.weather.weatherinfo;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import io.leftshift.weather.UseCase;
import io.leftshift.weather.UseCaseHandler;
import io.leftshift.weather.selectcity.SelectCity;
import io.leftshift.weather.weatherinfo.domain.model.WeatherInfo;
import io.leftshift.weather.weatherinfo.domain.usecase.GetWeatherInfos;

/**
 * Created by dhirajhimani on 8/17/2016.
 */
public class WeatherPresenter implements  WeatherContract.Presenter {

	private static final String TAG = "WeatherPresenter";

	private final WeatherContract.View mWeatherView;

	private final UseCaseHandler mUseCaseHandler;

	private final GetWeatherInfos mGetWeatherInfos;

	/**
	 * Instantiates a new Weather presenter.
	 *
	 * @param weatherView     the weather view
	 * @param useCaseHandler  the use case handler
	 * @param getWeatherInfos the get weather infos
	 */
	WeatherPresenter( WeatherContract.View weatherView , UseCaseHandler useCaseHandler
														, GetWeatherInfos getWeatherInfos) {
		mWeatherView = weatherView;
		mWeatherView.setPresenter(this);
		mUseCaseHandler = useCaseHandler;
		mGetWeatherInfos = getWeatherInfos;
	}

	@Override
	public void result(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK){
			String city = data.getStringExtra(SelectCity.CITY_SELECTED);
			Log.d(TAG, city);
			// Make a REST call
			openCityWeatherDetails(city);
		}
	}

	@Override
	public void openCityWeatherDetails(@NonNull String cityName) {


		if (cityName == null || cityName.isEmpty()) {
			//show current Location
			if (!mWeatherView.isActive()) {
				return;
			}
			mWeatherView.currentLocationError();
		} else {
			mWeatherView.setLoadingIndicator(true);
			mWeatherView.showWeatherCityName(cityName);
			// show weather by city
			String url = String.format(GetWeatherInfos.URL, cityName);
			GetWeatherInfos.RequestValues requestValues = new GetWeatherInfos.RequestValues(url, cityName);
			mUseCaseHandler.execute(mGetWeatherInfos, requestValues,
					new UseCase.UseCaseCallback<GetWeatherInfos.ResponseValue>() {
						@Override
						public void onSuccess(GetWeatherInfos.ResponseValue response) {
							if (!mWeatherView.isActive()) {
								return;
							}
							mWeatherView.setLoadingIndicator(false);

							processInfo(response.getInfos());
						}

						@Override
						public void onError() {
							if (!mWeatherView.isActive()) {
								return;
							}
							mWeatherView.showError();
						}
					});
		}
	}

	private void processInfo(List<WeatherInfo> weatherInfos) {
		if (weatherInfos.isEmpty()) {
			mWeatherView.showError();
		}else {
			mWeatherView.showWeathers(weatherInfos);
		}
	}

	@Override
	public void start() {
		// Get current Location and show weather
		mWeatherView.showCurrentLocationWeather();
	}

	public void getLocationByGpsOrNetwork(){
		mWeatherView.showCurrentLocationWeather();
	}

}
