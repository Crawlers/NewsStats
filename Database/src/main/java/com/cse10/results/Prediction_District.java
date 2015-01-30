package com.cse10.results;


import java.util.Date;

/**
 * Created by root on 1/16/15.
 */
public class Prediction_District implements java.io.Serializable{
    private int id;
    private String crimeDistrict;
    private String crimeYear;
    private String crimeYearQuarter;
    private int crimeCount;

    public Prediction_District() {
    }


    public Prediction_District(int id, String type, String district, String year, String quarter, int count) {
        this.id = id;
        this.crimeDistrict = district;
        this.crimeYear = year;
        this.crimeYearQuarter = quarter;
        this.crimeCount = count;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id;}

    public String getCrimeDistrict() { return this.crimeDistrict;}

    public void setCrimeDistrict(String district) {  this.crimeDistrict=district;}

    public String getCrimeYear() {return this.crimeYear;}

    public void setCrimeYear(String year) {this.crimeYear = year;}

    public String getCrimeYearQuarter(){return this.crimeYearQuarter;}

    public void setCrimeYearQuarter(String quarter){this.crimeYearQuarter = quarter;}

    public int getCrimeCount(){return this.crimeCount;}

    public void setCrimeCount(int count){this.crimeCount = count;}
}
