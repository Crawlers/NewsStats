package com.cse10.entities;

import java.util.Date;

/**
 * Created by Isuru Jayaweera on 2014-12-28.
 */
public class LocationDistrictMapper implements java.io.Serializable {

    private String location;
    private String district;

    public LocationDistrictMapper() {

    }


    public LocationDistrictMapper(String location, String district) {
        this.location = location;
        this.district = district;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) { this.district = district; }

}
