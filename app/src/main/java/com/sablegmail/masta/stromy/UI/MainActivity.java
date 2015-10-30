package com.sablegmail.masta.stromy.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcel;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sablegmail.masta.stromy.location.LocationProvider;
import com.sablegmail.masta.stromy.R;
import com.sablegmail.masta.stromy.weather.Current;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public  class MainActivity extends Activity implements LocationProvider.LocationCallback {

    public static final String TAG = MainActivity.class.getSimpleName();
    private Current mCurrent;
    private LocationProvider mLocationProvider;
    private Geocoder mGeocoder;
    private ArrayList adresses;
    private double currentLatitude = 0;
    private double currentLongitude = 0;
    private LocationManager mLocationManager;

    @Bind(R.id.temperatureLabel) TextView mTemperatureLabel;
    @Bind(R.id.timeLabel) TextView mTimeLabel;
    @Bind(R.id.humidityValue) TextView mHumidityValue;
    @Bind(R.id.precipValue) TextView mPrecipValue;
    @Bind(R.id.summaryLabel) TextView mSummaryLabel;
    @Bind(R.id.iconImageView) ImageView mIconImageView;
    @Bind(R.id.refreshImageView) ImageView mRefreshImageView;
    @Bind(R.id.degreeImageView) ImageView mDegreeImageView;
    @Bind(R.id.progressBar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mIconImageView.setVisibility(View.INVISIBLE);
        mDegreeImageView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);

        mLocationProvider = new LocationProvider(this, this);
        mGeocoder = new Geocoder(this);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            buildAlertMessageNoGps();
        }

        //set click listener on refreshButton
        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(currentLatitude, currentLongitude);
                getAddress();
            }
        });

        Log.d(TAG, "Main UI code is running!");
    }

    private void buildAlertMessageNoGps() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create()
                .show();
    }


    private void getAddress() {
        try {
            adresses = (ArrayList) mGeocoder.getFromLocation(currentLatitude, currentLongitude, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (adresses.size() > 0){
            Address address = (Address) adresses.get(0);
            TextView addressTV = (TextView) findViewById(R.id.locationLabel);
            addressTV.setText(address.getAddressLine(0));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocationProvider.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationProvider.disconnect();
    }

    private void getForecast(double latitude, double longitude) {
        String apiKey = "1c737001312c4beec2183c6e19ddc204";
        String forecastURL = "https://api.forecast.io/forecast/" + apiKey
                + "/" + latitude + ", " + longitude;

        if(isNetworkAvailable()) {
            toggleRefresh();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastURL)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutConnectionFailError();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mCurrent = getCurrentDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            alertUserAboutConnectionFailError();
                        }
                    } catch (IOException | JSONException e) {
                        Log.e(TAG, "Exception caught: ");
                    }
                }
            });
        } else {
            alertUserAboutNetworkUnavailableError();
        }
    }

    //toggle between progbar & refresh button
    private void toggleRefresh() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        mTemperatureLabel.setText(mCurrent.getTemperature() + "");
        mTimeLabel.setText(mCurrent.getFormattedTime());
        mHumidityValue.setText(mCurrent.getHumidity() + "%");
        mPrecipValue.setText(mCurrent.getPrecipeChance() + "%");
        mSummaryLabel.setText(mCurrent.getSummary());
        mIconImageView.setImageResource(mCurrent.getIconId());
        mIconImageView.setVisibility(View.VISIBLE);
        mDegreeImageView.setVisibility(View.VISIBLE);
    }

    //parsing JSON
    private Current getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timeZone = forecast.getString("timezone");
        Log.i(TAG, "From JSON: " + timeZone);
        JSONObject currently = forecast.getJSONObject("currently");
        Current current = new Current(Parcel.obtain());
        current.setHumidity(currently.getDouble("humidity"));
        current.setTime(currently.getLong("time"));
        current.setIcon(currently.getString("icon"));
        current.setPrecipeChance(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setTimeZone(timeZone);

        Log.d(TAG, current.getFormattedTime());

        return current;
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUserAboutConnectionFailError() {
        ConnectionFailDialogFragment dialog = new ConnectionFailDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    private void alertUserAboutNetworkUnavailableError(){
        NetworkUnavailableDialog dialog = new NetworkUnavailableDialog();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    @Override
    public void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        currentLongitude = location.getLongitude();
        currentLatitude = location.getLatitude();
    }
}
