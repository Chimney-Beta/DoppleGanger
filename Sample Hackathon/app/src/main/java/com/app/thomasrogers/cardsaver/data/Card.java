package com.app.thomasrogers.cardsaver.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thomasrogers on 12/26/16.
 */

public class Card implements Parcelable {

    private String[] mNames;
    private String[] mMainAddresses;
    private String[] mAltAddresses;
    private String[] mCSZAddress;
    private String[] mWebsites;
    private String[] mEmails;
    private String[] mPhones;
    private String mJSON;
    private String mValues;
    private byte[] mImage;

    public Card()
    {

    }

    private Card(Parcel in) {
        mNames = in.createStringArray();
        mMainAddresses = in.createStringArray();
        mAltAddresses = in.createStringArray();
        mCSZAddress = in.createStringArray();
        mWebsites = in.createStringArray();
        mEmails = in.createStringArray();
        mPhones = in.createStringArray();
        mJSON = in.readString();
        mValues = in.readString();
        mImage = in.createByteArray();
    }

    public String[] getNames()
    {
        return mNames;
    }

    public String[] getMainAddresses() {
        return mMainAddresses;
    }

    public String[] getAltAddresses() {
        return mAltAddresses;
    }

    public String[] getCSZAddresses() {
        return mCSZAddress;
    }

    public String[] getWebsites()
    {
        return mWebsites;
    }

    public String[] getEmails()
    {
        return mEmails;
    }

    public String[] getPhones()
    {
        return mPhones;
    }

    public String getJSON() {
        return mJSON;
    }

    public String getValues() {
        return mValues;
    }

    public byte[] getImage()
    {
        return mImage;
    }

    public void setNames(String[] names) {
        mNames = names;
    }

    public void setMainAddresses(String[] n)
    {
        mMainAddresses = n;
    }

    public void setAltAddresses(String[] n)
    {
        mAltAddresses = n;
    }

    public void setCSZAddresses(String[] n)
    {
        mCSZAddress = n;
    }

    public void setWebsite(String[] n)
    {
        mWebsites = n;
    }

    public void setEmails(String[] n)
    {
        mEmails = n;
    }

    public void setPhones(String[] n)
    {
        mPhones = n;
    }

    public void setJSON(String mJSON) {
        this.mJSON = mJSON;
    }

    public void setValues(String mValues) {
        this.mValues = mValues;
    }

    public void setImage(byte[] i)
    {
        mImage = i;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeStringArray(mNames);
        out.writeStringArray(mMainAddresses);
        out.writeStringArray(mAltAddresses);
        out.writeStringArray(mCSZAddress);
        out.writeStringArray(mWebsites);
        out.writeStringArray(mEmails);
        out.writeStringArray(mPhones);
        out.writeString(mJSON);
        out.writeString(mValues);
        out.writeByteArray(mImage);
    }

    public static final Parcelable.Creator<Card> CREATOR
            = new Parcelable.Creator<Card>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };
}
