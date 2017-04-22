package com.fome.planster.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Alex on 05.04.2017.
 */
public class Place {

    private String id;
    private String name;
    private LatLng latLng;
    private String address;

    public Place(String id, String name, LatLng latLng, String address) {
        this.id = id;
        this.name = name;
        this.latLng = latLng;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
