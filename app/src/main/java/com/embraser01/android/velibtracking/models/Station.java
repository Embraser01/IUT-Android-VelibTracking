package com.embraser01.android.velibtracking.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;


public class Station implements Parcelable {
    private int number;
    private String contract_name = null;
    private String name = null;
    private String address = null;
    private double position_lat;
    private double position_lng;
    private boolean banking;
    private boolean bonus;
    private String status;
    private int bike_stands;
    private int available_bike_stands;
    private int available_bikes;
    private Timestamp last_update;


    private Station(Parcel in) {
        this.number = in.readInt();
        this.contract_name = in.readString();
        this.name = in.readString();
        this.address = in.readString();
        this.position_lat = in.readDouble();
        this.position_lng = in.readDouble();
        this.banking = Boolean.getBoolean(in.readString());
        this.bonus = Boolean.getBoolean(in.readString());
        this.status = in.readString();
        this.bike_stands = in.readInt();
        this.available_bike_stands = in.readInt();
        this.available_bikes = in.readInt();
        this.last_update = new Timestamp(in.readLong());
    }

    public Station(int number, String contract_name, String name, String address, double position_lat, double position_lng, boolean banking, boolean bonus, String status, int bike_stands, int available_bike_stands, int available_bikes, Timestamp last_update) {
        this.number = number;
        this.contract_name = contract_name;
        this.name = name;
        this.address = address;
        this.position_lat = position_lat;
        this.position_lng = position_lng;
        this.banking = banking;
        this.bonus = bonus;
        this.status = status;
        this.bike_stands = bike_stands;
        this.available_bike_stands = available_bike_stands;
        this.available_bikes = available_bikes;
        this.last_update = last_update;
    }

    public int getNumber() {
        return number;
    }

    public String getContract_name() {
        return contract_name;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getPosition_lat() {
        return position_lat;
    }

    public double getPosition_lng() {
        return position_lng;
    }

    public Timestamp getLast_update() {
        return last_update;
    }

    public void setLast_update(Timestamp last_update) {
        this.last_update = last_update;
    }

    public int getAvailable_bikes() {
        return available_bikes;
    }

    public void setAvailable_bikes(int available_bikes) {
        this.available_bikes = available_bikes;
    }

    public int getAvailable_bike_stands() {
        return available_bike_stands;
    }

    public void setAvailable_bike_stands(int available_bike_stands) {
        this.available_bike_stands = available_bike_stands;
    }

    public int getBike_stands() {
        return bike_stands;
    }

    public void setBike_stands(int bike_stands) {
        this.bike_stands = bike_stands;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isBonus() {
        return bonus;
    }

    public void setBonus(boolean bonus) {
        this.bonus = bonus;
    }

    public boolean isBanking() {
        return banking;
    }

    public void setBanking(boolean banking) {
        this.banking = banking;
    }


    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof Station && this.number == ((Station) o).getNumber();
    }

    @Override
    public String toString() {
        return "Station{" +
                "number=" + number +
                ", contract_name='" + contract_name + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", position_lat=" + position_lat +
                ", position_lng=" + position_lng +
                ", banking=" + banking +
                ", bonus=" + bonus +
                ", status='" + status + '\'' +
                ", bike_stands=" + bike_stands +
                ", available_bike_stands=" + available_bike_stands +
                ", available_bikes=" + available_bikes +
                ", last_update=" + last_update +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(number);
        dest.writeString(contract_name);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeDouble(position_lat);
        dest.writeDouble(position_lng);
        dest.writeString(Boolean.toString(banking));
        dest.writeString(Boolean.toString(bonus));
        dest.writeString(status);
        dest.writeInt(bike_stands);
        dest.writeInt(available_bike_stands);
        dest.writeInt(available_bikes);
        dest.writeString(last_update.toString());
    }

    public static final Parcelable.Creator<Station> CREATOR = new Parcelable.Creator<Station>() {
        public Station createFromParcel(Parcel in) {
            return new Station(in);
        }

        public Station[] newArray(int size) {
            return new Station[size];
        }

    };
}
