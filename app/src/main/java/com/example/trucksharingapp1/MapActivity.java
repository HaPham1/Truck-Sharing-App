package com.example.trucksharingapp1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trucksharingapp1.data.DatabaseHelper;
import com.example.trucksharingapp1.databinding.ActivityMapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import com.example.trucksharingapp1.util.PaymentsUtil;
import com.example.trucksharingapp1.util.Constants;
import java.util.Optional;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;




public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    String API_KEY = "AIzaSyDQ3mmN_mrJWQ1s5avPHjmmZJDH1YPWl9w";
    GoogleMap mMap;
    MapView mapView;
    EditText pickupText, destinationText;
    TextView fareView, timeView;
    String stype;
    double lat1 = 0, long1 = 0, lat2 = 0, long2 = 0;
    LatLng latLng1, latLng2;
    int flag = 0;
    String Destination, Source;
    String distance, duration;
    Button orderBtn, callBtn;
    private ActivityMapBinding binding;
    DatabaseHelper db;
    String username;

    //Test Google Pay
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;

    private static final long SHIPPING_COST_CENTS = 90 * PaymentsUtil.CENTS_IN_A_UNIT.longValue();

    // A client for interacting with the Google Pay API.
    private PaymentsClient paymentsClient;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Get username from intent
        Intent getIntent = getIntent();
        username = getIntent.getStringExtra("username");

        //get user database
        db = new DatabaseHelper(this);

        //Initialize mapview
        mapView = findViewById(R.id.mapView);
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);

        //Initialize
        pickupText = findViewById(R.id.pickupText);
        destinationText = findViewById(R.id.destinationText);
        fareView = findViewById(R.id.fareView);
        timeView = findViewById(R.id.timeView);
        orderBtn = findViewById(R.id.button);
        callBtn = findViewById(R.id.button2);

        //Button for call function using phonenumber in database
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phonenum = db.fetchphone(username);
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel: " + phonenum));
                startActivity(intent);
            }
        });


        //Button to request Google Pay Payments
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    requestPayment(v);
                }
                catch (RuntimeException e) {
                    Toast.makeText(getApplicationContext(), "Please fill in Locations and get an estimated fare before booking!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //Test Google Pay
        Wallet.WalletOptions walletOptions = new Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_TEST).build();
        paymentsClient = PaymentsUtil.createPaymentsClient(this);
        final Optional<JSONObject> isReadyToPayJson = PaymentsUtil.getIsReadyToPayRequest();
        IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(isReadyToPayJson.get().toString());
        Task<Boolean> task = paymentsClient.isReadyToPay(request);



        //Code for edit text auto complete for Places API
        Places.initialize(getApplicationContext(), API_KEY);

        pickupText.setFocusable(false);
        pickupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stype = "source";
                List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(MapActivity.this);
                startActivityForResult(intent, 100);
            }
        });

        destinationText.setFocusable(false);
        destinationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stype = "destination";
                List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(MapActivity.this);
                startActivityForResult(intent, 100);
            }
        });
    }

    //Interface for map view
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    //Handle result for Places API
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            if (stype.equals("source")) {
                flag++;
                pickupText.setText(place.getAddress());
                latLng1 = place.getLatLng();

                //Prepare source as String format to send with JSON request
                String sSource = String.valueOf(place.getLatLng());
                sSource = sSource.replaceAll("lat/lng", "");
                sSource = sSource.replace(":", "");
                sSource = sSource.replace("(", "");
                sSource = sSource.replace(")", "");
                Source = sSource.replace(",", ", ");

            }
            else{
                flag++;
                destinationText.setText(place.getAddress());
                latLng2 = place.getLatLng();

                //Prepare destination as String format to send with JSON request
                String sDestination = String.valueOf(place.getLatLng());
                sDestination = sDestination.replaceAll("lat/lng", "");
                sDestination = sDestination.replace(":", "");
                sDestination = sDestination.replace("(", "");
                sDestination = sDestination.replace(")", "");
                Destination = sDestination.replace(",", ", ");
            }

            if(flag >= 2) {
                direction();
            }
        }
        //Handle result for Google Pay API
        else if (requestCode == LOAD_PAYMENT_DATA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentData paymentData = PaymentData.getFromIntent(data);
                handlePaymentSuccess(paymentData);
            }
            else {
                Toast.makeText(MapActivity.this, "Transaction Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(MapActivity.this, "Error with the API", Toast.LENGTH_SHORT).show();
        }

    }

    //Display Price and Duration
    private void distance(String distance, String duration) {

        fareView.setText("Approx. Fare: $" + String.valueOf(Double.valueOf(distance) * 0.55) );
        timeView.setText("Approx. travel time: " + duration);
    }



    // Using google map Directions API to draw Polylines and calculate Distance and duration
    private void direction(){
        //Use volley to send Json request
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = Uri.parse("https://maps.googleapis.com/maps/api/directions/json")
                .buildUpon()
                .appendQueryParameter("destination", Destination)
                .appendQueryParameter("origin", Source)
                .appendQueryParameter("mode", "driving")
                .appendQueryParameter("key", API_KEY)
                .toString();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            //Handle JSON response to get routes, legs, steps, points, polyline
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("OK")) {
                        JSONArray routes = response.getJSONArray("routes");

                        ArrayList<LatLng> points;
                        PolylineOptions polylineOptions = null;

                        for (int i=0;i<routes.length();i++){
                            points = new ArrayList<>();
                            polylineOptions = new PolylineOptions();
                            JSONArray legs = routes.getJSONObject(i).getJSONArray("legs");

                            for (int j=0;j<legs.length();j++) {
                                JSONArray steps = legs.getJSONObject(j).getJSONArray("steps");
                                JSONObject legObj = legs.getJSONObject(0);
                                distance = legObj.getJSONObject("distance").getString("text");
                                duration = legObj.getJSONObject("duration").getString("text");
                                distance = distance.replace(" km", "");
                                distance(distance, duration);


                                for (int k=0;k<steps.length();k++){
                                    String polyline = steps.getJSONObject(k).getJSONObject("polyline").getString("points");
                                    List<LatLng> list = decodePoly(polyline);

                                    for (int l=0;l<list.size();l++){
                                        LatLng position = new LatLng((list.get(l)).latitude, (list.get(l)).longitude);
                                        points.add(position);

                                    }
                                }
                            }
                            //Polyline format, add all points into it then change color
                            polylineOptions.addAll(points);
                            polylineOptions.width(10);
                            polylineOptions.color(ContextCompat.getColor(MapActivity.this, R.color.purple_500));
                            polylineOptions.geodesic(true);

                            //Logging for testing duration and distance
                            Log.d("distance", distance);
                            Log.d("duration", duration);
                        }

                        //Clear map for each time, then draw polyline and add markers, change map zoom
                        mMap.clear();
                        mMap.addPolyline(polylineOptions);
                        mMap.addMarker(new MarkerOptions().position(new LatLng(latLng1.latitude, latLng1.longitude)).title("Marker 1"));
                        mMap.addMarker(new MarkerOptions().position(new LatLng(latLng2.latitude, latLng2.longitude)).title("Marker 1"));

                        LatLngBounds bounds = new LatLngBounds.Builder()
                                .include(new LatLng(latLng1.latitude, latLng1.longitude))
                                .include(new LatLng(latLng2.latitude, latLng2.longitude)).build();
                        Point point = new Point();
                        getWindowManager().getDefaultDisplay().getSize(point);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, point.x, 300, 30));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        //Set retry policy
        RetryPolicy retryPolicy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(jsonObjectRequest);
    }

    //Function to decode polyline from the response
    public List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;

    }
    //Request payment using Google Pay
    public void requestPayment(View view) {


        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.
        try {
            double garmentPrice = Double.valueOf(distance) * 0.55;
            long garmentPriceCents = Math.round(garmentPrice * PaymentsUtil.CENTS_IN_A_UNIT.longValue());
            long priceCents = garmentPriceCents + SHIPPING_COST_CENTS;

            Optional<JSONObject> paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(priceCents);
            if (!paymentDataRequestJson.isPresent()) {
                return;
            }

            PaymentDataRequest request =
                    PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());

            // Since loadPaymentData may show the UI asking the user to select a payment method, we use
            // AutoResolveHelper to wait for the user interacting with it. Once completed,
            // onActivityResult will be called with the result.
            if (request != null) {
                AutoResolveHelper.resolveTask(
                        paymentsClient.loadPaymentData(request),
                        this, LOAD_PAYMENT_DATA_REQUEST_CODE);
            }

        } catch (Exception e) {
            throw new RuntimeException("The price cannot be deserialized from the JSON object.");
        }
    }

    private void handlePaymentSuccess(PaymentData paymentData) {

        // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
        final String paymentInfo = paymentData.toJson();
        if (paymentInfo == null) {
            return;
        }

        try {
            JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");
            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".

            final JSONObject tokenizationData = paymentMethodData.getJSONObject("tokenizationData");
            final String token = tokenizationData.getString("token");
            final JSONObject info = paymentMethodData.getJSONObject("info");
            final String billingName = info.getJSONObject("billingAddress").getString("name");
            Toast.makeText(
                    this, "something",
                    Toast.LENGTH_LONG).show();

            // Logging token string.
            Log.d("Google Pay token: ", token);

        } catch (JSONException e) {
            throw new RuntimeException("The selected garment cannot be parsed from the list of elements");
        }
    }

    //Send error to log
    private void handleError(int statusCode) {
        Log.e("loadPaymentData failed", String.format("Error code: %d", statusCode));
    }




}