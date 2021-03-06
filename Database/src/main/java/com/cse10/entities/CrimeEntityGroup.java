package com.cse10.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by TharinduWijewardane on 2014-12-19.
 */
public class CrimeEntityGroup implements java.io.Serializable {

    private int id;
    private int crimeArticleId;
    private String crimeType;
    private Date crimeDate;
    private String location;
    private String district;
    private String police;
    private String court;
    private String criminal;
    private String victim;
    private int victimCount;
    private String possession;
    private String label;
    private boolean isDuplicate;
    private LocationDistrictMapper locationDistrict;
    private Set<CrimePerson> crimePersonSet =  new HashSet<CrimePerson>(0);

    public CrimeEntityGroup() {
    }


    public CrimeEntityGroup(int id, int crimeArticleId, String crimeType, Date crimeDate, String location, String district, String police, String court, String criminal, String victim, int victimCount, String possession,String label,boolean isDuplicate, LocationDistrictMapper locationDistrict, Set<CrimePerson> crimePersonSet) {
        this.id = id;
        this.crimeArticleId = crimeArticleId;
        this.crimeType = crimeType;
        this.crimeDate = crimeDate;
        this.location = location;
        this.district = district;
        this.police = police;
        this.court = court;
        this.criminal = criminal;
        this.victim = victim;
        this.victimCount = victimCount;
        this.possession = possession;
        this.label=label;
        this.isDuplicate=isDuplicate;
        this.crimePersonSet = crimePersonSet;
        this.locationDistrict = locationDistrict;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id;}

    public int getCrimeArticleId() { return crimeArticleId;}

    public void setCrimeArticleId(int crimeArticleId) { this.crimeArticleId = crimeArticleId;}

    public String getCrimeType() { return crimeType;}

    public void setCrimeType(String crimeType) { this.crimeType = crimeType;}

    public Date getCrimeDate() { return crimeDate;}

    public void setCrimeDate(Date crimeDate) { this.crimeDate = crimeDate;}

    public String getLocation() { return location;}

    public void setLocation(String location) { this.location = location;}

    public String getDistrict() {  return locationDistrict.getDistrict();}

    public void setDistrict(String district) { this.district = district; }

   public LocationDistrictMapper getLocationDistrict() {return locationDistrict;}

    public void setLocationDistrict(LocationDistrictMapper locationDistrict) {this.locationDistrict = locationDistrict;}

    public String getPolice() {
        return police;
    }

    public void setPolice(String police) {
        this.police = police;
    }

    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = court;
    }

    public String getCriminal() {
        return criminal;
    }

    public void setCriminal(String criminal) {
        this.criminal = criminal;
    }

    public String getVictim() {
        return victim;
    }

    public void setVictim(String victim) {
        this.victim = victim;
    }

    public int getVictimCount() {
        return victimCount;
    }

    public void setVictimCount(int victimCount) {
        this.victimCount = victimCount;
    }

    public String getPossession() {
        return possession;
    }

    public void setPossession(String possession) { this.possession = possession;}

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean getIsDuplicate(){
        return isDuplicate;
    }
    public void setIsDuplicate(boolean isDuplicate) {
        this.isDuplicate = isDuplicate;
    }



    public Set<CrimePerson> getCrimePersonSet() { return crimePersonSet;}

    public void setCrimePersonSet(Set<CrimePerson> crimePersonSet) { this.crimePersonSet = crimePersonSet;}
}
