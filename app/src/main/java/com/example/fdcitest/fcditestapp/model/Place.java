package com.example.fdcitest.fcditestapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Place implements Parcelable {

    String name;
    String capital;


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCapital()
    {
        return capital;
    }

    public void setCapital(String capital)
    {
        this.capital = name;
    }

    @NonNull
    @Override
    public String toString()
    {
        return getName();
    }

    protected Place(Parcel in)
    {
        name = in.readString();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>()
    {
        @Override
        public Place createFromParcel(Parcel in)
        {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size)
        {
            return new Place[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(name);
    }
}
