package com.embraser01.android.velibtracking.models;


import java.util.ArrayList;

public class City {

    private String name;
    private String commercial_name;
    private String country_code;
    private ArrayList<String> cities;

    public City(ArrayList<String> cities, String commercial_name, String country_code, String name) {
        this.cities = cities;
        this.commercial_name = commercial_name;
        this.country_code = country_code;
        this.name = name;
    }

    public ArrayList<String> getCities() {
        return cities;
    }

    public String getCommercial_name() {
        return commercial_name;
    }

    public String getCountry_code() {
        return country_code;
    }

    public String getName() {
        return name;
    }
}
