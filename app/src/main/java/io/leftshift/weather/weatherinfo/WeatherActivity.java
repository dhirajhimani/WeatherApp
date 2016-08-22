package io.leftshift.weather.weatherinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import io.leftshift.weather.R;
import io.leftshift.weather.UseCaseHandler;
import io.leftshift.weather.data.WeatherRepository;
import io.leftshift.weather.data.source.WeatherRemoteDataSource;
import io.leftshift.weather.selectcity.SelectCity;
import io.leftshift.weather.weatherinfo.domain.usecase.GetWeatherInfos;

/**
 * The type Weather activity.
 */
public class WeatherActivity extends AppCompatActivity implements  WeatherFragment.TitleUpdate {

	private static final String CURRENT_CITY_KEY = "current_city";
	private WeatherPresenter mWeatherPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fabSelectLocation = (FloatingActionButton) findViewById(R.id.select_location);
		fabSelectLocation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivityForResult(new Intent(WeatherActivity.this, SelectCity.class), SelectCity.REQUEST_CITY);
			}
		});
		FloatingActionButton fabShowCurrentLocationWeather = (FloatingActionButton) findViewById(R.id.show_current_location);
		fabShowCurrentLocationWeather.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mWeatherPresenter.getLocationByGpsOrNetwork();
			}
		});
		WeatherFragment weatherFragment = (WeatherFragment)
												getSupportFragmentManager()
														.findFragmentById(R.id.fragment);


		// Init Presenter
		mWeatherPresenter = new WeatherPresenter(weatherFragment,
							 UseCaseHandler.getInstance(),
							 new GetWeatherInfos(WeatherRepository.
										getInstance(new WeatherRemoteDataSource())),
							 this);

		WeatherViewModel weatherViewModel =
				new WeatherViewModel(getApplicationContext(), mWeatherPresenter);

		weatherFragment.setWeatherViewModel(weatherViewModel);

		// Load previously saved state, if available.
		if (savedInstanceState != null) {
			String selectedCity =
					(String) savedInstanceState.getSerializable(CURRENT_CITY_KEY);
			if (!TextUtils.isEmpty(selectedCity)) {
				mWeatherPresenter.openCityWeatherDetails(selectedCity);
			}
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(CURRENT_CITY_KEY, mWeatherPresenter.getCurrentSelectedCity());

		super.onSaveInstanceState(outState);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		WeatherFragment weatherFragment = (WeatherFragment)
				getSupportFragmentManager()
						.findFragmentById(R.id.fragment);
		if (weatherFragment != null) {
			weatherFragment.onActivityResult(requestCode, resultCode, data);
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}


	@Override
	public void updateTitle(String title) {
		getSupportActionBar().setTitle(title);
	}
}
