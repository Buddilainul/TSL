package com.buddila.tsl.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.PopupWindow;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.buddila.tsl.Adapters.AutoCompleteAdapter;
import com.buddila.tsl.Adapters.PlaceAutocompleteAdapter;
import com.buddila.tsl.FindACabFirstScreen;
import com.buddila.tsl.HomeScreen;
import com.buddila.tsl.MapsActivity;
import com.buddila.tsl.R;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;



public class PickupAndDestination  extends BottomSheetDialogFragment {
    public static final String TAG = "PickupAndDestination";

    Button pickAVehicle;
    private AutoCompleteTextView autoCompleteTextView;
    private AutoCompleteAdapter autoCompleteAdapter;
    PlacesClient placesClient;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflating the layout for this fragment
        View view = inflater.inflate(R.layout.pickup_and_desination, container, false);

        autoCompleteTextView = view.findViewById(R.id.pickUpLocation);
        autoCompleteAdapter = new AutoCompleteAdapter(getContext(), placesClient);
        autoCompleteTextView.setAdapter(autoCompleteAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AutoCompleteAdapter.PlaceAutocomplete place = autoCompleteAdapter.getItem(position);
                if (place != null) {

                    // Use the placeId as needed
                }
            }
        });

        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Initialize Places API
        if (!Places.isInitialized()) {
            Places.initialize(requireContext().getApplicationContext(), "AIzaSyDZ8pH8Ez-hHnphjj2_0JHqNAJTqaLxUcQ");
        }

        // Create the PlacesClient
        placesClient = Places.createClient(requireContext());

        // Create the AutoCompleteTextView
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.pickUpLocation);

        // Set up the AutoCompleteTextView
        autoCompleteTextView.setAdapter(new AutoCompleteAdapter(requireContext(), placesClient));

        // Set the AutoCompleteTextView item click listener
        autoCompleteTextView.setOnItemClickListener((parent, view1, position, id) -> {
            // Handle the selected item
            AutoCompleteAdapter.PlaceAutocomplete place = autoCompleteAdapter.getItem(position);
            if (place != null) {
                // Use the placeId as needed
            }
        });


        pickAVehicle =view.findViewById(R.id.buttonPickACab);

        pickAVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityMap();
            }
        });


    }
    public void openActivityMap(){
        Intent intent = new Intent (getActivity(), MapsActivity.class);
        startActivity(intent);
    }

}


/**
 * package com.buddila.tsl.Fragments;
 *
 * import android.app.Activity;
 * import android.content.Intent;
 * import android.os.Bundle;
 * import android.util.Log;
 * import android.view.LayoutInflater;
 * import android.view.View;
 * import android.view.ViewGroup;
 * import android.widget.AutoCompleteTextView;
 * import android.widget.Button;
 * import android.widget.PopupWindow;
 *
 *
 * import androidx.activity.result.ActivityResultLauncher;
 * import androidx.activity.result.contract.ActivityResultContracts;
 * import androidx.annotation.NonNull;
 * import androidx.annotation.Nullable;
 * import androidx.fragment.app.FragmentManager;
 *
 * import com.buddila.tsl.FindACabFirstScreen;
 * import com.buddila.tsl.HomeScreen;
 * import com.buddila.tsl.MapsActivity;
 * import com.buddila.tsl.R;
 * import com.google.android.libraries.places.api.model.Place;
 * import com.google.android.libraries.places.api.model.TypeFilter;
 * import com.google.android.libraries.places.widget.Autocomplete;
 * import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
 * import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
 *
 * import java.util.ArrayList;
 * import java.util.Arrays;
 * import java.util.List;
 *
 *
 * public class PickupAndDestination  extends BottomSheetDialogFragment {
 *     public static final String TAG = "PickupAndDestination";
 *
 *     Button pickAVehicle;
 *     AutoCompleteTextView pickUp;
 *
 *     View.OnClickListener startAutocompleteIntentListener = view -> {
 *         view.setOnClickListener(null);
 *         startAutocompleteIntent();
 *     };
 *
 *     private final ActivityResultLauncher<Intent> startAutocomplete = registerForActivityResult(
 *             new ActivityResultContracts.StartActivityForResult(),
 *             result -> {
 *                 if (result.getResultCode() == Activity.RESULT_OK) {
 *                     Intent intent = result.getData();
 *                     if (intent != null) {
 *                         Place place = Autocomplete.getPlaceFromIntent(intent);
 *
 *                         // Write a method to read the address components from the Place
 *                         // and populate the form with the address components
 *                         Log.d(TAG, "Place: " + place.getAddressComponents());
 * //                        fillInAddress(place);
 *                     }
 *                 } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
 *                     // The user canceled the operation.
 *                     Log.i(TAG, "User canceled autocomplete");
 *                 }
 *             }
 *     );
 *
 *     @Nullable
 *     @Override
 *     public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
 *
 *         // Inflating the layout for this fragment
 *         View view = inflater.inflate(R.layout.pickup_and_desination, container, false);
 *         return view;
 *     }
 *     @Override
 *     public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
 *         super.onViewCreated(view, savedInstanceState);
 *
 *         pickAVehicle =view.findViewById(R.id.buttonPickACab);
 *
 *         pickAVehicle.setOnClickListener(new View.OnClickListener() {
 *             @Override
 *             public void onClick(View v) {
 *                 openActivityMap();
 *             }
 *         });
 *     }
 *
 *     private void startAutocompleteIntent() {
 *
 *         // Set the fields to specify which types of place data to
 *         // return after the user has made a selection.
 *         List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS_COMPONENTS,
 *                 Place.Field.LAT_LNG, Place.Field.VIEWPORT);
 *
 *         // Build the autocomplete intent with field, country, and type filters applied
 *         Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
 *                 .setCountries(Arrays.asList("US"))
 *                 .setTypesFilter(new ArrayList<String>() {{
 *                     add(TypeFilter.ADDRESS.toString().toLowerCase());
 *                 }})
 *                 .build(getContext());
 *         startAutocomplete.launch(intent);
 *     }
 *
 *
 *
 *
 *
 *     public void openActivityMap(){
 *         Intent intent = new Intent (getActivity(), MapsActivity.class);
 *         startActivity(intent);
 *     }
 *
 *
 *
 * }
 */