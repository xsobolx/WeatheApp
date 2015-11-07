package com.sablegmail.masta.stromy.weather;

import android.os.Parcel;
import android.os.Parcelable;

import com.sablegmail.masta.stromy.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by admin on 29.09.2015.
 */
public class Current implements Parcelable{
    private String mIcon;
    private long mTime;
    private double mTemperature;
    private double mHumidity;
    private double mPrecipeChance;
    private String mSummary;
    private String mTimeZone;

    public Current(Parcel in) {
        mIcon = in.readString();
        mTime = in.readLong();
        mTemperature = in.readDouble();
        mHumidity = in.readDouble();
        mPrecipeChance = in.readDouble();
        mSummary = in.readString();
        mTimeZone = in.readString();
    }

    public static final Creator<Current> CREATOR = new Creator<Current>() {
        @Override
        public Current createFromParcel(Parcel in) {
            return new Current(in);
        }

        @Override
        public Current[] newArray(int size) {
            return new Current[size];
        }
    };

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public int getIconId(){
        return Forecast.getIconId(mIcon);
    }

    public long getTime() {
        return mTime;
    }

    public String getFormattedTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        Date dateTime = new Date(getTime());
        String timeString = formatter.format(dateTime);
        return timeString;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public int getTemperature() {
        return (int) Math.round(((mTemperature - 32) * 5/9));
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public double getHumidity() {
        return Math.round(mHumidity * 100);
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public int getPrecipeChance() {
        return (int) Math.round(mPrecipeChance * 100);
    }

    public void setPrecipeChance(double precipeChance) {
        mPrecipeChance = precipeChance;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mIcon);
        dest.writeLong(mTime);
        dest.writeDouble(mTemperature);
        dest.writeDouble(mHumidity);
        dest.writeDouble(mPrecipeChance);
        dest.writeString(mSummary);
        dest.writeString(mTimeZone);
    }

    public void readFromParcel(Parcel in){
        mIcon = in.readString();
        mTime = in.readLong();
        mTemperature = in.readDouble();
        mHumidity = in.readDouble();
        mPrecipeChance = in.readDouble();
        mSummary = in.readString();
        mTimeZone = in.readString();
    }
}
