package edu.gwu.metrotest.model

import android.os.Parcelable
import android.os.Parcel;


/**
 * Created by liteng on 10/8/17.
 */


data class MetroStation(val name:String, val address: String, val lat: String, val lon: String,
                        val lineCode1: String, val lineCode2: String, val lineCode3: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(address)
        parcel.writeString(lat)
        parcel.writeString(lon)
        parcel.writeString(lineCode1)
        parcel.writeString(lineCode2)
        parcel.writeString(lineCode3)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MetroStation> {
        override fun createFromParcel(parcel: Parcel): MetroStation {
            return MetroStation(parcel)
        }

        override fun newArray(size: Int): Array<MetroStation?> {
            return arrayOfNulls(size)
        }
    }
}