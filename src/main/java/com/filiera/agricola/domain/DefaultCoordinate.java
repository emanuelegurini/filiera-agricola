package com.filiera.agricola.domain;

import com.filiera.agricola.model.interfaces.Coordinate;

import java.util.Objects;

public class DefaultCoordinate implements Coordinate {

    private Float latitude;
    private Float longitude;

    public DefaultCoordinate(Float latitude, Float longitude) {
        this.latitude = Objects.requireNonNull(latitude,"Latitude cannot be null");
        this.longitude = Objects.requireNonNull(longitude,"Longitude cannot be null");
    }

    @Override
    public Float getLat() {
        return this.latitude;
    }

    @Override
    public Float getLng() {
        return this.longitude;
    }

    @Override
    public void setLat(Float lat) {
        this.latitude = Objects.requireNonNull(lat,"Latitude cannot be null");
    }

    @Override
    public void setLng(Float lng) {
        this.longitude = Objects.requireNonNull(lng,"Longitude cannot be null");
    }
}
