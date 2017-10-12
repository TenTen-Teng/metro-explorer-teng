package edu.gwu.metrotest.model

import android.os.Parcelable
import android.os.Parcel;


/**
 * Created by liteng on 10/1/17.
 */



data class Landmark(val name: String, val imageUrl: String,
                    val address1:String, val address2: String, val distance: Int): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(imageUrl)
        parcel.writeString(address1)
        parcel.writeString(address2)
        parcel.writeInt(distance)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Landmark> {
        override fun createFromParcel(parcel: Parcel): Landmark {
            return Landmark(parcel)
        }

        override fun newArray(size: Int): Array<Landmark?> {
            return arrayOfNulls(size)
        }
    }

}
