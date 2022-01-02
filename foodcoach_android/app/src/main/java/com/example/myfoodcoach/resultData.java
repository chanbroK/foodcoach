package com.example.myfoodcoach;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Map;

public class resultData implements Parcelable {
    String eventID;
    String eventTime;
    String longitude;
    String latitude;
    String eventType;
    String action;

    public resultData(Map<String, String> in){
        this.eventID = in.get("eventID");
        this.eventTime = in.get("eventTime");
        this.eventType = in.get("eventType");
        this.action = in.get("action");
        this.longitude = in.get("longitude");
        this.latitude = in.get("latitude");
    }

    protected resultData(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<resultData> CREATOR = new Creator<resultData>() {
        @Override
        public resultData createFromParcel(Parcel in) {
            return new resultData(in);
        }

        @Override
        public resultData[] newArray(int size) {
            return new resultData[size];
        }
    };
}
