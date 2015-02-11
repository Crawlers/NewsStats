package com.cse10.duplicateDetector;

import com.cse10.database.DatabaseHandler;
import com.cse10.entities.CrimeEntityGroup;
import com.cse10.entities.LocationDistrictMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Chamath on 2/4/2015.
 */
public class DataHandler {


    /**
     * read articles from file
     *
     * @return
     */
    public List<String> readArticlesFromFile() {

        List<String> documents;
        try {
            documents = Files.readLines(new File("DuplicateDetector\\src\\main\\resources\\test\\test_in.txt"), Charsets.UTF_8);
        } catch (IOException e) {
            documents = new ArrayList<String>();
            e.printStackTrace();
        }
        return documents;
    }

    /**
     * read articles(article entities) from data base and create article content
     *
     * @return
     * @throws InterruptedException
     */
    public HashMap<Integer, String> readArticlesFromDB() throws InterruptedException {

        HashMap<Integer, String> articleContents = new HashMap<>();
        //only fetch crime entity groups with null label and unique labels
        ArrayList<CrimeEntityGroup> crimeEntityGroups = DatabaseHandler.fetchCrimeEntityGroupsWithNullOrUniqueLabels();
        int counter=0;
        for(CrimeEntityGroup crimeEntityGroup:crimeEntityGroups){
            if(crimeEntityGroup.getLabel()==null){
                counter++;
            }
        }
        if(counter>0) {

            Iterator iterator = crimeEntityGroups.listIterator();
            System.out.println(crimeEntityGroups.size());
            String content;
            int id;

            System.out.println(Thread.currentThread().getName() + " Duplicate Detector UI Handler -> Start Loading Data from Database");
            //create article content from entities
            while (iterator.hasNext()) {
                CrimeEntityGroup crimeEntityGroup = (CrimeEntityGroup) iterator.next();
                content = "";
                System.out.println(Thread.currentThread().getName() + " Duplicate Detector UI Handler -> Crime Entity Details --------------------------------");
                id = crimeEntityGroup.getId();

                String crimeType = crimeEntityGroup.getCrimeType();
                if (crimeType != null) {
                    String[] crimeTypeElements = crimeType.split("_");
                    crimeType = "";
                    for (int i = 0; i < crimeTypeElements.length; i++) {
                        crimeType = crimeType.concat(crimeTypeElements[i]).concat(" ");
                    }
                    content = content.concat(crimeType);
                }


                Date crimeDate = crimeEntityGroup.getCrimeDate();
                if (crimeDate != null) {
                    String[] crimeDateElements = crimeDate.toString().split("-");
                    String crimeDateString = "";
                    for (int i = 0; i < crimeDateElements.length; i++) {
                        crimeDateString = crimeDateString.concat(crimeDateElements[i]);
                    }
                    content = content.concat(crimeDateString);
                }

                content = content.concat(" ");

                LocationDistrictMapper locationDistrictMapper = crimeEntityGroup.getLocationDistrict();
                if (locationDistrictMapper != null) {
                    String location = locationDistrictMapper.getLocation();
                    if (location != null)
                        content = content.concat(location).concat(" ");

                    String district = crimeEntityGroup.getDistrict();
                    if (district != null)
                        content = content.concat(district);
                }
                System.out.println(Thread.currentThread().getName() + " Duplicate Detector UI Handler ->  Content---" + content);
                articleContents.put(id, content);

                //if user stop the thread
                checkInterruption();

            }

        }
        //close data base
        DatabaseHandler.closeDatabase();
        System.out.println(Thread.currentThread().getName() + " Duplicate Detector UI Handler -> Finish Loading Data from Database");
        return articleContents;
    }

    /**
     * helper function to handle interruption
     */
    private void checkInterruption() throws InterruptedException{
        if(Thread.currentThread().isInterrupted()){
            throw new InterruptedException();
        }
    }

    //wrapper methods for data base handler class methods
    public CrimeEntityGroup fetchCrimeEntityGroup(int i){
        return DatabaseHandler.fetchCrimeEntityGroup(i);
    }

    public void updateCrimeEntityGroup(CrimeEntityGroup crimeEntityGroup){
        DatabaseHandler.updateCrimeEntityGroup(crimeEntityGroup);
    }

    public void closeDatabase(){
        DatabaseHandler.closeDatabase();
    }

}
