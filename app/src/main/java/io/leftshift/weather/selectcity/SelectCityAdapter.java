package io.leftshift.weather.selectcity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import io.leftshift.weather.R;

/**
 * Created by dhirajhimani on 8/17/2016.
 */
public class SelectCityAdapter extends RecyclerView.Adapter<SelectCityAdapter.SelectCityViewHolder> {

	private String[] mCityList;

	private AdapterView.OnItemClickListener mItemClickListener;

	public SelectCityAdapter(String[] cityList, Context context, AdapterView.OnItemClickListener itemClickListener) {
		this.mCityList = cityList;
		this.mItemClickListener = itemClickListener;
	}

	@Override
	public SelectCityAdapter.SelectCityViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_city_item, parent, false);
		SelectCityViewHolder viewHolder = new SelectCityViewHolder(view);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(SelectCityAdapter.SelectCityViewHolder holder, final int position) {
		holder.mCityName.setText(mCityList[position]);
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mItemClickListener.onItemClick(null, null, position, position);
			}
		});
	}

	@Override
	public int getItemCount() {
		return mCityList.length;
	}

	public static class SelectCityViewHolder extends RecyclerView.ViewHolder{

		protected TextView mCityName;

		public SelectCityViewHolder(View itemView) {
			super(itemView);
			mCityName= (TextView) itemView.findViewById(R.id.city_name);
		}
	}

}
