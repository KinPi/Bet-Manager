package com.kin.betmanager.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kin on 3/14/18.
 */

public class Contact implements Parcelable {
    public long id;
    public String name;
    public int image;

    public Contact (long id, String name, int image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }


    protected Contact(Parcel in) {
        id = in.readLong();
        name = in.readString();
        image = in.readInt();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeInt(image);
    }
}
