package com.demo.ahmed.weather.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.ahmed.weather.models.WeatherItem;
import com.demo.ahmed.weather.view_holder.WeatherHolder;
import com.weather.ahmed.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by BUFFON on 9/27/2016.
 */
public class WeatherAdapter extends RecyclerView.Adapter<WeatherHolder> {

    private Activity mContext;
    private List<WeatherItem> weatherItems;

    public WeatherAdapter(Activity context, List<WeatherItem> weatherItems) {
        this.weatherItems = new ArrayList<>(weatherItems);
        mContext=context;
    }

    @Override
    public WeatherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(mContext).inflate(R.layout.weather_item, parent, false);
        return new WeatherHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherHolder holder, int position) {
        holder.bind(weatherItems.get(position));
    }

    @Override
    public int getItemCount() {
        return weatherItems.size() ;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}

