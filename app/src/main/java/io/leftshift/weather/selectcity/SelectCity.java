package io.leftshift.weather.selectcity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import io.leftshift.weather.R;

/**
 * Created by dhirajhimani on 8/17/2016.
 */
public class SelectCity extends AppCompatActivity {

	/**
	 * The constant CITY_SELECTED.
	 */
	public static String CITY_SELECTED = "CITY_SELECTED";
	/**
	 * The constant REQUEST_CITY.
	 */
	public static final int REQUEST_CITY = 101;
	private String TAG = "SelectCity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_city);
		//Setup toolbar
		setupToolbar();
		//Setup RecyclerView and adapter
		setupRecyclerViewContent();

	}

	private void setupToolbar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setNavigationIcon(android.R.drawable.ic_delete);

		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	private void setupRecyclerViewContent() {
		final String[] cities = getResources().getStringArray(R.array.cities);
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyler_select_city);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(new SelectCityAdapter(cities, this, new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
				Intent data = new Intent();
				data.putExtra(CITY_SELECTED, cities[position]);
//				Log.d(TAG, cities[position]);
				setResult(RESULT_OK, data);
				finish();
			}
		}));
	}


}