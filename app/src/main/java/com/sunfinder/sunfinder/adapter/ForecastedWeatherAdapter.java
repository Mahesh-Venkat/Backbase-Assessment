package com.sunfinder.sunfinder.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sunfinder.sunfinder.R;
import com.sunfinder.sunfinder.activity.MainActivity;
import com.sunfinder.sunfinder.transferobject.CityInfoTO;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ForecastedWeatherAdapter extends ArrayAdapter<String> {
    SharedPreferences mSharedPreferences;
    private final Context context;
    private final List<String> citiesList;
    private final List<String>colorList;

    public CitiesAdapter(Context context, List<String> cities) {
        super(context, -1, cities);
        this.context = context;
        this.citiesList = cities;
        this.colorList = getColors();

        Activity activity = (Activity) context;

        mSharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.city_row_item, parent, false);

        RelativeLayout relativeLayout = (RelativeLayout) rowView.findViewById(R.id.city_item_layout);
        relativeLayout.setBackgroundColor(Color.parseColor(colorList.get(position%5)));

        TextView textView = (TextView) rowView.findViewById(R.id.city_name);

        ImageButton deleteButton = (ImageButton) rowView.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                citiesList.remove(position);
                deleteTheCityFromSharedPReferences(position);
                notifyDataSetChanged();
            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context instanceof MainActivity){
                    ((MainActivity)context).onCitySelected(position);
                }
            }
        });

        textView.setText(citiesList.get(position));

        return rowView;
    }

    private void deleteTheCityFromSharedPReferences(int position) {
        List<CityInfoTO> citiesList = getCities();
        citiesList.remove(position);

        updateSharedPreferences(citiesList);

    }

    private void updateSharedPreferences(List<CityInfoTO> citiesList) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<CityInfoTO>>() {}.getType();
        String json = gson.toJson(citiesList, type);

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.shared_preference_key), json);
        editor.commit();
    }

    private List<CityInfoTO> getCities() {
        Gson gson = new Gson();

        String json = mSharedPreferences.getString(context.getResources().getString(R.string.shared_preference_key),"");
        Type type = new TypeToken<List<CityInfoTO>>() {}.getType();

        List<CityInfoTO> cityInfoTOList = gson.fromJson(json, type);


        return cityInfoTOList;
    }

    private List<String> getColors() {
        List<String> colors = new ArrayList<>();
        colors.add("#CD5C5C");
        colors.add("#17A589");
        colors.add("#5499C7");
        colors.add("#839192");
        colors.add("#FFC300");

        return colors;
    }
}