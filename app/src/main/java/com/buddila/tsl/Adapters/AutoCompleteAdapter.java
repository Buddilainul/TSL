package com.buddila.tsl.Adapters;

import static com.buddila.tsl.Fragments.PickupAndDestination.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteAdapter extends ArrayAdapter<AutoCompleteAdapter.PlaceAutocomplete> implements Filterable {
    private List<PlaceAutocomplete> resultList;
    private Context context;
    private PlacesClient placesClient;

    public AutoCompleteAdapter(Context context, PlacesClient placesClient) {
        super(context, android.R.layout.simple_dropdown_item_1line);
        this.context = context;
        this.placesClient = placesClient;
    }

    @Override
    public int getCount() {
        return resultList != null ? resultList.size() : 0;
    }

    @Override
    public PlaceAutocomplete getItem(int position) {
        return resultList != null ? resultList.get(position) : null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        PlaceAutocomplete item = getItem(position);
        if (item != null) {
            textView.setText(item.getDescription());
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // Query the Places API for autocomplete suggestions
                    resultList = getAutocompleteResults(constraint.toString());
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    private List<PlaceAutocomplete> getAutocompleteResults(String query) {
        List<PlaceAutocomplete> suggestions = new ArrayList<>();

        if (context != null && placesClient != null) {
            AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

            FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                    .setQuery(query)
                    .setSessionToken(token)
                    .build();

            placesClient.findAutocompletePredictions(request)
                    .addOnSuccessListener((response) -> {
                        for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                            String suggestion = prediction.getPrimaryText(null).toString();
                            String placeId = prediction.getPlaceId();
                            suggestions.add(new PlaceAutocomplete(suggestion, placeId));
                        }

                        // Notify the adapter that the data has changed
                        notifyDataSetChanged();
                    })
                    .addOnFailureListener((exception) -> {
                        Log.e(TAG, "Error getting autocomplete predictions: " + exception.getMessage());
                    });
        }

        return suggestions;
    }

    public static class PlaceAutocomplete {
        private String description;
        private String placeId;

        public PlaceAutocomplete(String description, String placeId) {
            this.description = description;
            this.placeId = placeId;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return description;
        }
    }
}