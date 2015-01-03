package com.cse10.duplicateDetector;

import com.cse10.database.DatabaseHandler;
import com.cse10.entities.CrimeEntityGroup;
import java.util.List;

/**
 * Created by chamath on 1/3/2015.
 */
public class FetchCrimeEntityGroups {
    public static void main(String[] args) {
        List<CrimeEntityGroup> crimeEntityGroups=DatabaseHandler.fetchCrimeEntityGroups();
        System.out.println(crimeEntityGroups.size());
    }
}
