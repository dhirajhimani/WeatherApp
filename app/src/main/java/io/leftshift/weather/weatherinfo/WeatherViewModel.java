package io.leftshift.weather.weatherinfo;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import io.leftshift.weather.BR;

/**
 * Created by dhirajhimani on 8/22/2016.
 */
public class WeatherViewModel extends BaseObservable {

	int mWeatherInfoSize = 0;

	private final WeatherContract.Presenter mPresenter;

	private Context mContext;

	public WeatherViewModel(Context context, WeatherContract.Presenter presenter) {
		mContext = context;
		mPresenter = presenter;
	}

	@Bindable
	public boolean isNotEmpty() {
		return mWeatherInfoSize > 0;
	}

	public void setTaskListSize(int weatherinfoSize) {
		mWeatherInfoSize = weatherinfoSize;
		notifyPropertyChanged(BR.notEmpty);
	}
}
