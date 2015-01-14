package com.cse10.duplicateDetector;

import com.cse10.database.DatabaseHandler;
import com.cse10.entities.CrimeEntityGroup;
import com.cse10.entities.LocationDistrictMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


/**
 * Created by chamath on 1/3/2015.
 */
public class UI {

    /**
     * read articles from file
     * @return
     */
    private List<String> readArticlesFromFile() {

        List<String> documents;
        try {
            documents= Files.readLines(new File("C:\\Users\\hp\\Desktop\\DuplicateDetetcionImplementation\\Implementations\\sim hash\\simhash-java-master\\simhash-java-master\\src\\test_in"), Charsets.UTF_8);
        } catch (IOException e) {
            documents=new ArrayList<String>();
            e.printStackTrace();
        }
        return documents;
    }

    public List<String> readArticlesFromDB(){
        ArrayList<String>articleContents=new ArrayList<String>();
        ArrayList<CrimeEntityGroup> crimeEntityGroups = DatabaseHandler.fetchCrimeEntityGroups();
        Iterator iterator = crimeEntityGroups.listIterator();
        System.out.println(crimeEntityGroups.size());
        String content;
        while (iterator.hasNext()) {
            CrimeEntityGroup crimeEntityGroup = (CrimeEntityGroup) iterator.next();
            content="";
            //System.out.println("Crime Entity Details --------------------------------");

            //System.out.println("ID " + crimeEntityGroup.getCrimeArticleId());

            String crimeType=crimeEntityGroup.getCrimeType();
           // System.out.println("Crime Type " + crimeType);
            if(crimeType!=null)
            content=content.concat(crimeType).concat(" ");


            Date crimeDate=crimeEntityGroup.getCrimeDate();
           // System.out.println("Crime Date " + crimeDate);
            if(crimeDate!=null)
            content=content.concat(crimeDate.toString()).concat(" ");

            String police=crimeEntityGroup.getPolice();
           // System.out.println("Police " + police);
            if(police!=null)
            content=content.concat(police).concat(" ");

            String court = crimeEntityGroup.getCourt();
           // System.out.println("Court " + court);
            if(court!=null)
            content=content.concat(court).concat(" ");

            LocationDistrictMapper locationDistrictMapper = crimeEntityGroup.getLocationDistrict();
            if (locationDistrictMapper != null) {
                String location = locationDistrictMapper.getLocation();
              //  System.out.println("Location " + location);
                if(location!=null)
                content=content.concat(location).concat(" ");
                String district= locationDistrictMapper.getDistrict();
             //   System.out.println("District " + district);
                if(district!=null)
                content=content.concat(district).concat(" ");
            }
            System.out.println("Content-"+content);
            articleContents.add(content);
            System.out.println("-----------------------------------------------------");

        }
        System.out.println("End");
        return articleContents;
    }

    public void findDuplicates() {

        SimHashCalculator simHashCalculator = new SimHashCalculator(new FullWordSegmenter());
        //this will contain all the calculated sim hash value
        ArrayList<Long> documentSimHashes = new ArrayList<Long>();
        // Read the documents from database
        List<String> documents = readArticlesFromDB();


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

        for (String document : documents) {
            // Calculate the sim hash value of document.
            long docHash = simHashCalculator.simhash64(document);
            System.out.println("Document=[" + document + "] Hash=[" + docHash + " , " + Long.toBinaryString(docHash) + "]" + "Bit Length of Hash:" + Long.toBinaryString(docHash).length() + "bits");
            try {
                Files.append("Document=[" + document + "] Hash=[" + docHash + " , " + Long.toBinaryString(docHash) + "]" + "Bit Length of Hash:" + Long.toBinaryString(docHash).length() + "bits \n", articleHashValues, Charsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
            documentSimHashes.add(docHash);
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
        for (String document : documents) {
            long docHash = simHashCalculator.simhash64(document);
            List<Integer> similarDocs = Lists.newLinkedList();
            Map<Integer, Integer> docDistances = Maps.newHashMap();

            for (int i = 0; i < documentSimHashes.size(); i++) {
                int dist = simHashCalculator.hammingDistance(docHash, documentSimHashes.get(i));
                // System.out.println(dist);
                //check the hamming distance difference
                if (dist <= 3) {
                    similarDocs.add(i);
                    docDistances.put(i, dist);
                }
            }

            if (!similarDocs.isEmpty()) {
                System.out.println(("Documents similar as [" + document + "]:\n"));
                try {
                    Files.append("Documents similar as [" + document + "]:\n", articleHammingDistances, Charsets.UTF_8);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int i : similarDocs) {
                    if (i == currentDocumentId) {
                        continue;
                    }
                    System.out.println(("[" + i + "]\tDistance=[" + docDistances.get(i) + "]\n"));
                    try {
                        Files.append("[" + documents.get(i) + "]\tDistance=[" + docDistances.get(i) + "]\n",  articleHammingDistances, Charsets.UTF_8);
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

            ++currentDocumentId;
        }
    }

    public static void main(String[] args) {
        UI ui=new UI();
        ui.findDuplicates();
    }


}
