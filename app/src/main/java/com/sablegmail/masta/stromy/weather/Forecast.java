package com.sablegmail.masta.stromy.weather;

import com.sablegmail.masta.stromy.R;

/**
 * Created by xsobolx on 30.10.2015.
 */
public class Forecast {
    private Current mCurrent;
    private Hour[] mHourlyForecast;
    private Day[] mDailyForecast;

    public Hour[] getHourlyForecast() {
        return mHourlyForecast;
    }

    public void setHourlyForecast(Hour[] hourlyForecast) {
        mHourlyForecast = hourlyForecast;
    }

    public Current getCurrent() {
        return mCurrent;
    }

    public void setCurrent(Current current) {
        mCurrent = current;
    }

    public Day[] getDailyForecast() {
        return mDailyForecast;
    }

    public void setDailyForecast(Day[] dailyForecast) {
        mDailyForecast = dailyForecast;
    }

    public static int getIconId(String iconString){
        int iconId = R.drawable.clear_day;

        switch (iconString){
            case "clear-day":
                iconId = R.drawable.clear_day;
                break;
            case "clear-night":
                iconId = R.drawable.clear_night;
                break;
            case "rain":
                iconId = R.drawable.rain;
                break;
            case "snow":
                iconId = R.drawable.snow;
                break;
            case "sleet":
                iconId = R.drawable.sleet;
                break;
            case "wind":
                iconId = R.drawable.wind;
                break;
            case "fog":
                iconId = R.drawable.fog;
                break;
            case "cloudy":
                iconId = R.drawable.cloudy;
                break;
            case "partly-cloudy-day":
                iconId = R.drawable.partly_cloudy;
                break;
            case "partly-cloudy-night":
                iconId = R.drawable.cloudy_night;
                break;
            default:
                break;
        }
        return iconId;
    }
}
