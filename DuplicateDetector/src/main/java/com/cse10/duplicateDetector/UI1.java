package com.cse10.duplicateDetector;

/**
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

public class UI1 {

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

    public HashMap<Integer, String> readArticlesFromDB() {

        HashMap<Integer, String> articleContents = new HashMap<>();
        ArrayList<CrimeEntityGroup> crimeEntityGroups = DatabaseHandler.fetchCrimeEntityGroups();
        Iterator iterator = crimeEntityGroups.listIterator();
        System.out.println(crimeEntityGroups.size());
        String content;
        int id;
        while (iterator.hasNext()) {
            CrimeEntityGroup crimeEntityGroup = (CrimeEntityGroup) iterator.next();
            content = "";
            //System.out.println("Crime Entity Details --------------------------------");

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
                String district = locationDistrictMapper.getDistrict();
                //   System.out.println("District " + district);
                if (district != null)
                    content = content.concat(district).concat(" ");
            }
            System.out.println("Content-" + content);
            articleContents.put(id, content);
            System.out.println("-----------------------------------------------------");

        }
        System.out.println("End");
        return articleContents;
    }

    public void findDuplicates() {

        SimHashCalculator simHashCalculator = new SimHashCalculator(new FullWordSegmenter());

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
            System.out.println("Document=[" + document + "] Hash=[" + docHash + " , " + Long.toBinaryString(docHash) + "]" + "Bit Length of Hash:" + Long.toBinaryString(docHash).length() + "bits");
            try {
                Files.append("Document=[" + document + "] Hash=[" + docHash + " , " + Long.toBinaryString(docHash) + "]" + "Bit Length of Hash:" + Long.toBinaryString(docHash).length() + "bits \n", articleHashValues, Charsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
            documentSimHashes.put(id, docHash);
        }

        File articleHammingDistances = new File("C:\\Users\\hp\\Desktop\\DuplicateDetetcionImplementation\\Implementations\\sim hash\\simhash-java-master\\simhash-java-master\\src\\test_out_idea_h");

        try {
            writer = new PrintWriter(articleHammingDistances);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer.print("");
        writer.close();

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
                System.out.println(("Documents similar as [" + document+ currentDocumentId + "]:\n"));
                try {
                    Files.append("Documents similar as [" + document+" " + currentDocumentId+ "]:\n", articleHammingDistances, Charsets.UTF_8);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int i : similarDocs) {
                    if (i == currentDocumentId) {
                        continue;
                    }
                    System.out.println(("[" + i + "]\tDistance=[" + docDistances.get(i) + "]\n"));
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
        }
    }

    public static void main(String[] args) {
        UI1 ui = new UI1();
        /*HashMap<Integer, String> articleContent = ui.readArticlesFromDB();

        Iterator iterator = articleContent.keySet().iterator();
        while (iterator.hasNext()) {
            int id = (Integer) iterator.next();
            String content = (String) articleContent.get(new Integer(id));

            System.out.println("id=" + id + "content=" + content);
        }*/

        ui.findDuplicates();
    }
}
