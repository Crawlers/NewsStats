package com.cse10.results;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by root on 1/16/15.
 */
public class NewsStatistic implements java.io.Serializable{
    private int id;
    private String crimeType;
    private String crimeDistrict;
    private String crimeYear;
    private String crimeYearQuarter;
    private Date crimeDate;
    private int crimeCount;

    public NewsStatistic() {
    }


    public NewsStatistic(int id, String type, String district, Date date, String year, String quarter, int count) {
        this.id = id;
        this.crimeType = type;
        this.crimeDate = date;
        this.crimeDistrict = district;
        this.crimeYear = year;
        this.crimeYearQuarter = quarter;
        this.crimeCount = count;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id;}

    public String getCrimeType() { return crimeType;}

    public void setCrimeType(String crimeType) { this.crimeType = crimeType;}

    public Date getCrimeDate() { return crimeDate;}

    public void setCrimeDate(Date crimeDate) { this.crimeDate = crimeDate;}

    public String getCrimeDistrict() { return this.crimeDistrict;}

    public void setCrimeDistrict(String district) {  this.crimeDistrict=district;}

    public String getCrimeYear() {return this.crimeYear;}

    public void setCrimeYear(String year) {this.crimeYear = year;}

    public String getCrimeYearQuarter(){return this.crimeYearQuarter;}

    public void setCrimeYearQuarter(String quarter){this.crimeYearQuarter = quarter;}

    public int getCrimeCount(){return this.crimeCount;}

    public void setCrimeCount(int count){this.crimeCount = count;}
}
