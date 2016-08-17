package io.leftshift.weather.weatherinfo;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import io.leftshift.weather.R;
import io.leftshift.weather.UseCaseHandler;
import io.leftshift.weather.data.WeatherRepository;
import io.leftshift.weather.data.source.WeatherRemoteDataSource;
import io.leftshift.weather.selectcity.SelectCity;
import io.leftshift.weather.weatherinfo.domain.usecase.GetWeatherInfos;

public class WeatherActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks{

	private GoogleApiClient mGoogleApiClient;


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
				//get Current City
				WeatherFragment weatherFragment = (WeatherFragment)
						getSupportFragmentManager()
								.findFragmentById(R.id.fragment);
				weatherFragment.showCurrentLocationWeather();
			}
		});
		WeatherFragment weatherFragment = (WeatherFragment)
												getSupportFragmentManager()
														.findFragmentById(R.id.fragment);
		// Init Presenter
		new WeatherPresenter(weatherFragment,
							 UseCaseHandler.getInstance(),
								new GetWeatherInfos(WeatherRepository.
										getInstance(new WeatherRemoteDataSource())));

		if (mGoogleApiClient == null) {
			mGoogleApiClient = new GoogleApiClient.Builder(this)
					.addConnectionCallbacks(this)
					.addApi(LocationServices.API)
					.build();
		}
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
	protected void onStart() {
		mGoogleApiClient.connect();
		super.onStart();
	}

	@Override
	protected void onStop() {
		mGoogleApiClient.disconnect();
		super.onStop();
	}

	@Override
	@SuppressWarnings({"MissingPermission"})
	public void onConnected(@Nullable Bundle bundle) {
		Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
				mGoogleApiClient);
		if (mLastLocation != null) {

		}
	}

	@Override
	public void onConnectionSuspended(int i) {

	}
}
