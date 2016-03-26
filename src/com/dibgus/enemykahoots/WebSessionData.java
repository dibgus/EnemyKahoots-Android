package com.dibgus.enemykahoots;

import android.os.Parcel;
import android.os.Parcelable;
import com.dibgus.enemykahoots.user.UserType;

/**
 * Created by Ivan on 3/24/2016.
 * Creates a parcelable package for SubmissionActivity to send the initialization variables to WebSessionActivity
 */
public class WebSessionData implements Parcelable {
    public String userName;
    public String gameID;
    public UserType type;
    public WebSessionData(String userName, String gameID, UserType type)
    {
        this.userName = userName;
        this.gameID = gameID;
        this.type = type;
    }


    protected WebSessionData(Parcel in) {
        userName = in.readString();
        gameID = in.readString();
        type = (UserType) in.readValue(UserType.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(gameID);
        dest.writeValue(type);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WebSessionData> CREATOR = new Parcelable.Creator<WebSessionData>() {
        @Override
        public WebSessionData createFromParcel(Parcel in) {
            return new WebSessionData(in);
        }

        @Override
        public WebSessionData[] newArray(int size) {
            return new WebSessionData[size];
        }
    };
}