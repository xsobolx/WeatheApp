package com.sablegmail.masta.stromy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private CurrentWeather mCurrentWeather;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String apiKey = "1c737001312c4beec2183c6e19ddc204";
        double latitude = 55.488091;
        double longitude = 36.033576;
        String forecastURL = "https://api.forecast.io/forecast/" + apiKey
                + "/" + latitude + ", " + longitude;
        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastURL)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mCurrentWeather = getCurrentDetails(jsonData);
                        } else {
                            alertUserAboutConnectionFailError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: ");
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ");
                    }
                }
            });
        } else {
            alertUserAboutNetworkUnavailableError();
        }
        Log.d(TAG, "Main UI code is running!");
    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timeZone = forecast.getString("timezone");
        Log.i(TAG, "From JSON: " + timeZone);

        JSONObject currently = forecast.getJSONObject("currently");

        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setPrecipeChance(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setTimeZone(timeZone);

        Log.d(TAG, currentWeather.getFormatedTime());

        return currentWeather;
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
}
