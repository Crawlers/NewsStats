package com.cse10.entities;

/**
 * Created by Isuru Jayaweera on 2014-12-30.
 */
public class CrimePerson implements java.io.Serializable {

    private int crimePersonId;
    private String name;
    private CrimeEntityGroup entityGroup;

    public CrimePerson() {

    }

    public CrimePerson(int crimePersonId, String name, CrimeEntityGroup entityGroup) {
        this.crimePersonId = crimePersonId;
        this.name = name;
        this.entityGroup = entityGroup;
    }

    public int getCrimePersonId() { return crimePersonId;}

    public void setCrimePersonId(int crimePersonId) { this.crimePersonId = crimePersonId;}

    public String getName() { return name;}

    public void setName(String name) { this.name = name;}

    public CrimeEntityGroup getEntityGroup() { return entityGroup;}

    public void setEntityGroup(CrimeEntityGroup entityGroup) { this.entityGroup = entityGroup;}
}
