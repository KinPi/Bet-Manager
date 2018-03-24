package com.kin.betmanager.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kin on 3/14/18.
 */

public class Bet implements Parcelable{
    public long id;
    public String title;
    public long bettingAgainst;
    public String opponentsBet;
    public String yourBet;
    public String termsAndConditions;
    public boolean isCompleted;

    public Bet (long id, String title, long bettingAgainst, String opponentsBet, String yourBet, String termsAndConditions, boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.bettingAgainst = bettingAgainst;
        this.opponentsBet = opponentsBet;
        this.yourBet = yourBet;
        this.termsAndConditions = termsAndConditions;
        this.isCompleted = isCompleted;
    }

    protected Bet (Parcel in) {
        id = in.readLong();
        title = in.readString();
        bettingAgainst = in.readLong();
        opponentsBet = in.readString();
        yourBet = in.readString();
        termsAndConditions = in.readString();
        isCompleted = in.readByte() == 1;
    }

    public static final Creator<Bet> CREATOR = new Creator<Bet>() {
        @Override
        public Bet createFromParcel(Parcel in) {
            return new Bet(in);
        }

        @Override
        public Bet[] newArray(int size) {
            return new Bet[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeLong(bettingAgainst);
        parcel.writeString(opponentsBet);
        parcel.writeString(yourBet);
        parcel.writeString(termsAndConditions);
        parcel.writeByte((byte) (isCompleted ? 1 : 0));
    }
}
