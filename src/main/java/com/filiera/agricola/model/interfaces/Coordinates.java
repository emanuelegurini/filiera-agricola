package com.filiera.agricola.model.interfaces;

/**
 * This interface is used to model the Coordinates Object
 */
public interface Coordinates {

    /**
     * This function is used to return the lat
     *
     * @return the latitude
     */
    Float getLat();


    /**
     * This function is used to return the lng
     *
     * @return the longitude
     */
    Float getLng();


    /**
     * This function is used to set the latitude
     *
     * @param lat as a float number
     */
    void setLat(Float lat);


    /**
     * This function is used to set the longitude
     *
     * @param lng as a float number
     */
    void setLng(Float lng);
}
