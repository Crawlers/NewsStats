package com.cse10.duplicateDetector;

/**
 * this class combine all the functionality of this module
 * Created by chamath on 1/19/2015.
 */


import com.cse10.database.DatabaseHandler;
import com.cse10.entities.CrimeEntityGroup;
import com.cse10.entities.LocationDistrictMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class DuplicateDetectorUIHandler implements Runnable{

    SimHashCalculator simHashCalculator;

    public DuplicateDetectorUIHandler(){
        simHashCalculator = new SimHashCalculator(new FullWordSegmenter());
    }
    /**
     * read articles from file
     *
     * @return
     */
    private List<String> readArticlesFromFile() {

        List<String> documents;
        try {
            documents = Files.readLines(new File("C:\\Users\\hp\\Desktop\\DuplicateDetetcionImplementation\\Implementations\\sim hash\\simhash-java-master\\simhash-java-master\\src\\test_in"), Charsets.UTF_8);
        } catch (IOException e) {
            documents = new ArrayList<String>();
            e.printStackTrace();
        }
        return documents;
    }

    private HashMap<Integer, String> readArticlesFromDB() throws InterruptedException{

        HashMap<Integer, String> articleContents = new HashMap<>();
        ArrayList<CrimeEntityGroup> crimeEntityGroups = DatabaseHandler.fetchCrimeEntityGroups();
        Iterator iterator = crimeEntityGroups.listIterator();
        System.out.println(crimeEntityGroups.size());
        String content;
        int id;
        while (iterator.hasNext()) {
            CrimeEntityGroup crimeEntityGroup = (CrimeEntityGroup) iterator.next();
            content = "";
            System.out.println(Thread.currentThread().getName()+" -> Crime Entity Details --------------------------------");
            //System.out.println("ID " + crimeEntityGroup.getCrimeArticleId());
            id = crimeEntityGroup.getId();

            String crimeType = crimeEntityGroup.getCrimeType();
            if (crimeType != null) {
                String[] crimeTypeElements = crimeType.split("_");
                crimeType = "";
                for (int i = 0; i < crimeTypeElements.length; i++) {
                    crimeType = crimeType.concat(crimeTypeElements[i]).concat(" ");
                }
                // System.out.println("Crime Type " + crimeType);
                content = content.concat(crimeType);
            }


            Date crimeDate = crimeEntityGroup.getCrimeDate();
            // System.out.println("Crime Date " + crimeDate);
            if (crimeDate != null) {
                String[] crimeDateElements=crimeDate.toString().split("-");
                String crimeDateString="";
                for (int i = 0; i < crimeDateElements.length; i++) {
                    crimeDateString = crimeDateString.concat(crimeDateElements[i]).concat(" ");
                }
                content = content.concat(crimeDateString);
            }

            String police = crimeEntityGroup.getPolice();
            // System.out.println("Police " + police);
            if (police != null)
                content = content.concat(police).concat(" ");

            String court = crimeEntityGroup.getCourt();
            // System.out.println("Court " + court);
            if (court != null)
                content = content.concat(court).concat(" ");

            LocationDistrictMapper locationDistrictMapper = crimeEntityGroup.getLocationDistrict();
            if (locationDistrictMapper != null) {
                String location = locationDistrictMapper.getLocation();
                //  System.out.println("Location " + location);
                if (location != null)
                    content = content.concat(location).concat(" ");

                String district="";//locationDistrictMapper.getDistrict();
                //   System.out.println("District " + district);
                if (district != null)
                    content = content.concat(district).concat(" ");
            }
            System.out.println(Thread.currentThread().getName()+"-> Content---" + content);
            articleContents.put(id, content);
            System.out.println(Thread.currentThread().getName()+"-> -----------------------------------------------------");

            //if user stop the thread
            if(Thread.currentThread().isInterrupted()){
                throw new InterruptedException();
            }

        }
        //close data base
        DatabaseHandler.closeDatabase();

        System.out.println("End");
        return articleContents;
    }

    private void findDuplicates() throws InterruptedException {



        //this will contain all the calculated sim hash value (LONG) with related crime entity group id (Integer)
        HashMap<Integer, Long> documentSimHashes = new HashMap<>();

        // Read the documents from database, Integer is id in crime_entity_group and String is corresponding content
        HashMap<Integer, String> documents = readArticlesFromDB();

        File articleHashValues = new File("C:\\Users\\hp\\Desktop\\DuplicateDetetcionImplementation\\Implementations\\sim hash\\simhash-java-master\\simhash-java-master\\src\\test_out_idea_s");
        //clear the file before writing
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(articleHashValues);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer.print("");
        writer.close();

        //for each document calculate sim hash value
        Iterator iterator = documents.keySet().iterator();
        while (iterator.hasNext()) {
            // Calculate the sim hash value of document.
            int id = (Integer) iterator.next();
            String document = documents.get(id);
            long docHash = simHashCalculator.simhash64(document);
            System.out.println(Thread.currentThread().getName()+"->Document=[" + document + "] Hash=[" + docHash + " , " + Long.toBinaryString(docHash) + "]" + "Bit Length of Hash:" + Long.toBinaryString(docHash).length() + "bits");
            try {
                Files.append("Document=[" + document + "] Hash=[" + docHash + " , " + Long.toBinaryString(docHash) + "]" + "Bit Length of Hash:" + Long.toBinaryString(docHash).length() + "bits \n", articleHashValues, Charsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
            documentSimHashes.put(id, docHash);

            //if user stop the thread
            if(Thread.currentThread().isInterrupted()){
                throw new InterruptedException();
            }
        }

        //if user stop the thread
        if(Thread.currentThread().isInterrupted()){
                throw new InterruptedException();
        }

        File articleHammingDistances = new File("C:\\Users\\hp\\Desktop\\DuplicateDetetcionImplementation\\Implementations\\sim hash\\simhash-java-master\\simhash-java-master\\src\\test_out_idea_h");

        try {
            writer = new PrintWriter(articleHammingDistances);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer.print("");
        writer.close();

        //calculate duplicates for each document
        //add duplicate doc ids to a list
        int currentDocumentId = 0;
        iterator = documents.keySet().iterator();
        while (iterator.hasNext()) {
            currentDocumentId = (Integer) iterator.next();
            String document = documents.get(currentDocumentId);
            long docHash = simHashCalculator.simhash64(document);

            List<Integer> similarDocs = new ArrayList();
            Map<Integer, Integer> docDistances = new HashMap();
            Iterator iter = documentSimHashes.keySet().iterator();

            while (iter.hasNext()) {
                int hashDocId = (Integer) iter.next();
                Long hashValue = documentSimHashes.get(hashDocId);
                int distance = simHashCalculator.hammingDistance(docHash, hashValue);
                // System.out.println(distance);
                //check the hamming distance difference
                if (distance <= 0) {
                    similarDocs.add(hashDocId);
                    docDistances.put(hashDocId, distance);
                }
            }
            if (!similarDocs.isEmpty()) {
                System.out.println((Thread.currentThread().getName()+" Documents similar as [" + document+ currentDocumentId + "]:\n"));
                try {
                    Files.append("Documents similar as [" + document+" " + currentDocumentId+ "]:\n", articleHammingDistances, Charsets.UTF_8);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int i : similarDocs) {
                    if (i == currentDocumentId) {
                        continue;
                    }
                    System.out.println((Thread.currentThread().getName()+"-> [" + i + "]\tDistance=[" + docDistances.get(i) + "]\n"));
                    try {
                        Files.append("[" + i + "]\tDistance=[" + docDistances.get(i) + "]\n", articleHammingDistances, Charsets.UTF_8);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Files.append("End\n", articleHammingDistances, Charsets.UTF_8);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            //if user stop the thread
            if(Thread.currentThread().isInterrupted()){
                throw new InterruptedException();
            }

        }

    }

    /**
     * start the process
     */
    public void startDuplicateDetection(){
        try {
            findDuplicates();
        } catch (InterruptedException e) {
            //user has stopped the process
            System.out.println(Thread.currentThread().getName()+"-> STOPPED");
            DatabaseHandler.closeDatabase();
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        startDuplicateDetection();
    }

    //testing the functionality
    public static void main(String[] args) {
        //TODO do not delete duplicate articles. maintain a list of duplicate ids

        DuplicateDetectorUIHandler ui = new DuplicateDetectorUIHandler();
        Thread t=new Thread(ui);
        t.setName("Duplicate Detector");
        System.out.println(Thread.currentThread().getName()+"-> Start");
        t.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"-> Finish");
        t.interrupt();
    }


}
