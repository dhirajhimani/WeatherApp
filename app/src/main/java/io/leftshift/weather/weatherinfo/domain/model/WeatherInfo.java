package io.leftshift.weather.weatherinfo.domain.model;

import com.google.common.base.Objects;

import io.leftshift.weather.utils.WeatherUtils;

/**
 * Created by dhirajhimani on 8/17/2016.
 */
public class WeatherInfo {

	private long mTimestamp;

	private String mWeatherStatus;

	public String getDateText() {
		return dateText;
	}

	public void setDateText(String dateText) {
		this.dateText = dateText;
	}

	private String dateText;

	public long getTimestamp() {
		return mTimestamp;
	}

	public void setTimestamp(long mTimestamp) {
		this.mTimestamp = mTimestamp;
		dateText = WeatherUtils.formatDate(mTimestamp);
	}

	public String getWeatherStatus() {
		return mWeatherStatus;
	}

	public void setWeatherStatus(String mWeatherStatus) {
		this.mWeatherStatus = mWeatherStatus;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WeatherInfo weatherInfo = (WeatherInfo) o;
		return Objects.equal(mTimestamp, weatherInfo.mTimestamp) &&
				Objects.equal(mWeatherStatus, weatherInfo.mWeatherStatus) ;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(mTimestamp, mWeatherStatus);
	}

	@Override
	public String toString() {
		return "WeatherInfo on  " + mTimestamp;
	}

}
