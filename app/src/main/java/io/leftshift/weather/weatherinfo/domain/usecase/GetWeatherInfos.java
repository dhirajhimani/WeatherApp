package io.leftshift.weather.weatherinfo.domain.usecase;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.List;

import io.leftshift.weather.UseCase;
import io.leftshift.weather.data.WeatherDatasource;
import io.leftshift.weather.data.WeatherRepository;
import io.leftshift.weather.weatherinfo.domain.model.WeatherInfo;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by dhirajhimani on 8/17/2016.
 */
public class GetWeatherInfos extends UseCase<GetWeatherInfos.RequestValues, GetWeatherInfos.ResponseValue> {

	private final WeatherRepository mWeatherRepository;

	public static String URL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=%s&mode=json&cnt=14&APPID=f6bddf40a2298dc31e2c905776412034&units=metric";

	public GetWeatherInfos(@NonNull WeatherRepository weatherRepository) {
		mWeatherRepository = checkNotNull(weatherRepository, "WeatherRepository cannot be null!");
	}

	@Override
	protected void executeUseCase(final RequestValues values) {
		try {
			mWeatherRepository.getWeatherInfo(new WeatherDatasource.LoadTasksCallback() {
				@Override
				public void onTasksLoaded(List<WeatherInfo> weatherInfos) {
					ResponseValue responseValue = new ResponseValue(weatherInfos);
					getUseCaseCallback().onSuccess(responseValue);
				}

				@Override
				public void onDataNotAvailable() {
					getUseCaseCallback().onError();
				}
			}, values.url, values.city);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static final class RequestValues implements UseCase.RequestValues {

		private final String url, city;

		public RequestValues(String url, String city) {
			this.url = url;
			this.city = city;
		}
	}

	public static final class ResponseValue implements UseCase.ResponseValue {

		private final List<WeatherInfo> mWeatherInfos;

		public ResponseValue(@NonNull List<WeatherInfo> weatherInfos) {
			mWeatherInfos = checkNotNull(weatherInfos, "weatherInfos cannot be null!");
		}

		public List<WeatherInfo> getInfos() {
			return mWeatherInfos;
		}
	}
}