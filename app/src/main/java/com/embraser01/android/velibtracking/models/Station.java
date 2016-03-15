package com.embraser01.android.velibtracking.models;


import java.sql.Timestamp;

/**
 * Created by Marc-Antoine on 15/03/2016.
 */
public class Station {
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
}
