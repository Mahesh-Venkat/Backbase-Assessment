package com.sunfinder.sunfinder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sunfinder.sunfinder.R;
import com.sunfinder.sunfinder.transferobject.WeatherInfoTO;

import java.util.List;

public class ForecastedWeatherAdapter extends ArrayAdapter<WeatherInfoTO> {

    private final Context context;
    private List<WeatherInfoTO> forecastedWeatherInfoTO = null;
    private String units;


    public ForecastedWeatherAdapter(Context context, List<WeatherInfoTO> forecastedWeather, String units) {
        super(context, -1, forecastedWeather);
        this.context = context;
        this.forecastedWeatherInfoTO = forecastedWeather;
        this.units = units;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.future_weather_item, parent, false);

        TextView dayTextView = (TextView) rowView.findViewById(R.id.text_column_day);
        TextView temperatureTextView = (TextView) rowView.findViewById(R.id.text_column_temperature);
        TextView humidityTextView = (TextView) rowView.findViewById(R.id.text_column_humidity);
        TextView rainChancesTextView = (TextView) rowView.findViewById(R.id.text_column_rain_chance_item);
        TextView windTextView = (TextView) rowView.findViewById(R.id.text_column_wind);

        if (units.equals(getContext().getResources().getString(R.string.list_preference_item_1))) {
            dayTextView.setText(forecastedWeatherInfoTO.get(position).getDayOftheWeek());
            int temperature = (int) Math.floor(forecastedWeatherInfoTO.get(position).getMain().getTemp() + 0.5d);
            temperatureTextView.setText(Integer.toString(temperature) + " \u2103");
            humidityTextView.setText(Double.toString(forecastedWeatherInfoTO.get(position).getMain().getHumidity()) + "%");
            rainChancesTextView.setText(forecastedWeatherInfoTO.get(position).getWeather().get(0).getDescription());
            windTextView.setText(Double.toString(forecastedWeatherInfoTO.get(position).getWind().getSpeed()) + "km/h");
        }  else {
            dayTextView.setText(forecastedWeatherInfoTO.get(position).getDayOftheWeek());
            int temperature = (int) Math.floor(forecastedWeatherInfoTO.get(position).getMain().getTemp() + 0.5d);
            temperatureTextView.setText(Integer.toString(temperature) + " \u2109");
            humidityTextView.setText(Double.toString(forecastedWeatherInfoTO.get(position).getMain().getHumidity()) + "%");
            rainChancesTextView.setText(forecastedWeatherInfoTO.get(position).getWeather().get(0).getDescription());
            windTextView.setText(Double.toString(forecastedWeatherInfoTO.get(position).getWind().getSpeed()) + " m/h");
        }

        return rowView;
    }
}