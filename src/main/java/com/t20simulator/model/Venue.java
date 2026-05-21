package com.t20simulator.model;

public class Venue {
    private int venueId;
    private String name;
    private String city;
    private int capacity;

    public Venue() {
    }

    public Venue(int venueId, String name, String city, int capacity) {
        this.venueId = venueId;
        this.name = name;
        this.city = city;
        this.capacity = capacity;
    }

    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return name + " (" + city + ")";
    }
}
