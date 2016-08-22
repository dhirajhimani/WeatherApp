package io.leftshift.weather.weatherinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.leftshift.weather.R;
import io.leftshift.weather.weatherinfo.domain.model.WeatherInfo;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A placeholder fragment containing a simple view.
 */
public class WeatherFragment extends Fragment implements WeatherContract.View {

	private WeatherContract.Presenter mPresenter;

	private ListView mWeatherList;

	private TextView mEmptyView;

	private WeatherInfoAdapter mWeatherInfoAdapter;

	private ProgressBar mProgressBar;

	private TitleUpdate titleUpdate;

	private WeatherViewModel mWeatherViewModel;

	/**
	 * Instantiates a new Weather fragment.
	 */
	public WeatherFragment() {
	}

	public void setWeatherViewModel(WeatherViewModel weatherViewModel) {
		mWeatherViewModel = weatherViewModel;
	}

	/**
	 * The interface Title update.
	 */
	interface TitleUpdate {
		/**
		 * Udpate title.
		 *
		 * @param title the title
		 */
		void updateTitle(String title);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWeatherInfoAdapter = new WeatherInfoAdapter(new ArrayList<WeatherInfo>(0));
		titleUpdate = (TitleUpdate)getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View root =  inflater.inflate(R.layout.fragment_weather, container, false);
		//init
		mWeatherList = (ListView)root.findViewById(R.id.weather_list);
		mEmptyView = (TextView)root.findViewById(R.id.no_city_selected);
		mProgressBar = (ProgressBar)root.findViewById(R.id.progress_indicator);
		//
		mWeatherList.setAdapter(mWeatherInfoAdapter);
		mEmptyView.setVisibility(View.VISIBLE);
		mWeatherList.setVisibility(View.GONE);

		return root;
	}



	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		mPresenter.result(requestCode, resultCode, data);
	}

	public void currentLocationError(){
		mEmptyView.setVisibility(View.VISIBLE);
		mWeatherList.setVisibility(View.GONE);
		String errorMsg = getResources().getString(R.string.location_error);
		mEmptyView.setText(errorMsg);
		Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
	}


	@Override
	public void setPresenter(WeatherContract.Presenter presenter) {
		mPresenter = presenter;
	}

	@Override
	public void setLoadingIndicator(boolean active) {
		mProgressBar.setVisibility(active ? View.VISIBLE : View.GONE);
	}

	@Override
	public void showWeathers(List<WeatherInfo> weatherInfo) {
		mWeatherViewModel.setTaskListSize(weatherInfo.size());
		mWeatherInfoAdapter.replaceData(weatherInfo);

		mEmptyView.setVisibility(View.GONE);
		mWeatherList.setVisibility(View.VISIBLE);
	}

	@Override
	public void showWeatherCityName(String cityName) {
		titleUpdate.updateTitle(cityName + "'s " + "Weather");
	}

	@Override
	public void showError() {
		mEmptyView.setVisibility(View.VISIBLE);
		mWeatherList.setVisibility(View.GONE);
		mEmptyView.setText(getResources().getString(R.string.server_error));

	}

	@Override
	public boolean isActive() {
		return isAdded();
	}

	private static class WeatherInfoAdapter extends BaseAdapter {

		private List<WeatherInfo> mWeatherInfos;

		/**
		 * Instantiates a new Weather info adapter.
		 *
		 * @param weatherInfos the weather infos
		 */
		public WeatherInfoAdapter(List<WeatherInfo> weatherInfos) {
			setList(weatherInfos);
		}

		/**
		 * Replace data.
		 *
		 * @param weatherInfos the weather infos
		 */
		public void replaceData(List<WeatherInfo> weatherInfos) {
			setList(weatherInfos);
			notifyDataSetChanged();
		}

		private void setList(List<WeatherInfo> weatherInfos) {
			mWeatherInfos = checkNotNull(weatherInfos);
		}

		@Override
		public int getCount() {
			return mWeatherInfos.size();
		}

		@Override
		public WeatherInfo getItem(int i) {
			return mWeatherInfos.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			View rowView = view;
			if (rowView == null) {
				LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
				rowView = inflater.inflate(R.layout.weather_info_item, viewGroup, false);
			}

			final WeatherInfo weatherInfo = getItem(i);

			TextView titleTV = (TextView) rowView.findViewById(R.id.title);
			titleTV.setText(weatherInfo.getDateText());

			TextView statusTV = (TextView) rowView.findViewById(R.id.status);
			statusTV.setText(weatherInfo.getWeatherStatus());


			return rowView;
		}
	}


}
