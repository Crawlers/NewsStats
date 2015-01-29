package com.cse10.results;


import java.util.Date;

/**
 * Created by root on 1/16/15.
 */
public class Prediction_Type implements java.io.Serializable{
    private int id;
    private String crimeType;
    private String crimeYear;
    private String crimeYearQuarter;
    private int crimeCount;

    public Prediction_Type() {
    }


    public Prediction_Type(int id, String type, String district, String year, String quarter, int count) {
        this.id = id;
        this.crimeType = type;
        this.crimeYear = year;
        this.crimeYearQuarter = quarter;
        this.crimeCount = count;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id;}

    public String getCrimeType() { return crimeType;}

    public void setCrimeType(String crimeType) { this.crimeType = crimeType;}

    public String getCrimeYear() {return this.crimeYear;}

    public void setCrimeYear(String year) {this.crimeYear = year;}

    public String getCrimeYearQuarter(){return this.crimeYearQuarter;}

    public void setCrimeYearQuarter(String quarter){this.crimeYearQuarter = quarter;}

    public int getCrimeCount(){return this.crimeCount;}

    public void setCrimeCount(int count){this.crimeCount = count;}
}
