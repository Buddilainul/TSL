package com.buddila.tsl.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;

public class PlaceAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
    private static final int MAX_RESULTS = 10;
    private PlacesClient placesClient;
    private AutocompleteSessionToken sessionToken;

    public PlaceAutocompleteAdapter(Context context, PlacesClient placesClient) {
        super(context, android.R.layout.simple_dropdown_item_1line);
        this.placesClient = placesClient;
        this.sessionToken = AutocompleteSessionToken.newInstance();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        String item = getItem(position);
        if (item != null) {
            textView.setText(item);
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return super.getCount() > MAX_RESULTS ? MAX_RESULTS : super.getCount();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint != null) {
                    ArrayList<AutocompletePrediction> predictions = getAutocompletePredictions(constraint.toString());
                    results.values = predictions;
                    results.count = predictions.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();

                if (results != null && results.count > 0) {
                    addAll((ArrayList<String>) results.values);
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };

        return filter;
    }

    private ArrayList<AutocompletePrediction> getAutocompletePredictions(String query) {
        ArrayList<AutocompletePrediction> predictions = new ArrayList<>();

        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setSessionToken(sessionToken)
                .setQuery(query)
                .build();

        Task<FindAutocompletePredictionsResponse> task = placesClient.findAutocompletePredictions(request);
        try {
            FindAutocompletePredictionsResponse response = Tasks.await(task);
            if (response != null) {
                predictions.addAll(response.getAutocompletePredictions());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return predictions;
    }
}