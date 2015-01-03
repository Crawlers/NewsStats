package com.cse10.extractor.gate;

/**
 * Created with IntelliJ IDEA.
 * User: Isuru Jayaweera
 * Date: 01/02/15
 * Test whether all the required entities are extracted and properly stored in the tables.
 */

import com.cse10.database.DatabaseHandler;
import com.cse10.entities.CrimeEntityGroup;
import com.cse10.entities.CrimePerson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

public class GateTest {

    public static void main(String[] args) throws Exception {

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        ArrayList<CrimeEntityGroup> entityGroupList = (ArrayList<CrimeEntityGroup>) DatabaseHandler.fetchCrimeEntityGroupsByIdRange(1,10);
        int i = 0;

        for(CrimeEntityGroup entity : entityGroupList){
            i++;

            // check whether all crime details are properly stored
            System.out.println("Entity Set : " + i + " -Begins Here-");

            // check type of the crime
            if (entity.getCrimeType() != null) {
                System.out.println("Crime Type : " + entity.getCrimeType());
            }

            // check date of the crime
            if (entity.getCrimeDate() != null) {
                System.out.println("Crime Date : " + format.format(entity.getCrimeDate()));
            }

            // check location of the crime
            if (entity.getLocationDistrict().getLocation() != null) {
                System.out.println("Crime Location : " + entity.getLocationDistrict().getLocation());
            }

            // check district of the crime
            if (entity.getLocationDistrict() != null) {
                System.out.println("District : " + entity.getLocationDistrict().getDistrict());
            }

            // check police location related with the crime
            if (entity.getPolice() != null) {
                System.out.println("Police Location : " + entity.getPolice());
            }

            // check court location related with the crime
            if (entity.getCourt() != null) {
                System.out.println("Court Location : " + entity.getCourt());
            }

            // check people who involved in the crime
            if(entity.getCrimePersonSet() != null && !entity.getCrimePersonSet().isEmpty()){
                Set<CrimePerson> people = entity.getCrimePersonSet();

                System.out.print("Crime People");

                for(CrimePerson person : people){
                    System.out.print(" : " + person.getName());
                }

                System.out.println();
            }

            System.out.println("Entity Set : " + i + " -Ends Here-");
            System.out.println();
        }

        System.out.println("- Test Finishes Here -");

        System.exit(0);
    }
}

