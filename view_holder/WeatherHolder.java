package com.demo.ahmed.weather.view_holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.demo.ahmed.weather.models.WeatherItem;
import com.weather.ahmed.R;


/**
 * Created by BUFFON on 9/28/2016.
 */
public class WeatherHolder extends RecyclerView.ViewHolder {

    private TextView mTitle;
    private TextView temp_max, temp_min;

    public WeatherHolder(View itemView) {
        super(itemView);
        mTitle = itemView.findViewById(R.id.title);
        temp_max = itemView.findViewById(R.id.temp_max);
        temp_min = itemView.findViewById(R.id.temp_min);
    }

    public void bind(WeatherItem nyt_item) {
        mTitle.setText(nyt_item.getname());
        temp_max.setText(nyt_item.getTemp_max());
        temp_min.setText(nyt_item.getTemp_min());
    }

}