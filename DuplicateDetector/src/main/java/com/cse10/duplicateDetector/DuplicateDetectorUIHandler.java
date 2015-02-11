package com.cse10.duplicateDetector;

/**
 * this class combine all the functionality of this module
 * Created by Chamath on 1/19/2015.
 */

import com.cse10.entities.CrimeEntityGroup;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class DuplicateDetectorUIHandler extends Observable implements Runnable {

    SimHashCalculator simHashCalculator;
    DataHandler dataHandler;
    HammingDistanceCalculator hammingDistanceCalculator;

    public DuplicateDetectorUIHandler() {
        simHashCalculator = new SimHashCalculator(new FullWordSegmenter());
        dataHandler=new DataHandler();
        hammingDistanceCalculator=new HammingDistanceCalculator();
    }

    /**
     * calculate sim hash values for each document
     * @param documents
     * @return
     * @throws InterruptedException
     */
    private HashMap<Integer, Long> calculateSimHashValues(HashMap<Integer, String> documents) throws InterruptedException {


        HashMap<Integer, Long> documentSimHashes = new HashMap<>();
        if(documents!=null) {
            int progress = 0;
            File articleHashValues = new File("DuplicateDetector\\src\\main\\resources\\hashValues.txt");
            //clear the file before writing
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(articleHashValues);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            writer.print("");
            writer.close();

            checkInterruption();

            Iterator iterator = documents.keySet().iterator();
            int increment = documents.keySet().size() / 40;
            int value = 0;
            //for each document calculate sim hash value
            while (iterator.hasNext()) {
                // Calculate the sim hash value of document.
                int id = (java.lang.Integer) iterator.next();
                String document = documents.get(id);
                long docHash = simHashCalculator.getSimhash64Value(document);
                System.out.println(Thread.currentThread().getName() + "Duplicate Detector UI Handler->Document=[" + document + "] Hash=[" + docHash + " , " + java.lang.Long.toBinaryString(docHash) + "]" + "Bit Length of Hash:" + java.lang.Long.toBinaryString(docHash).length() + "bits");
                try {
                    Files.append("Document=[" + document + "] Hash=[" + docHash + " , " + java.lang.Long.toBinaryString(docHash) + "]" + "Bit Length of Hash:" + java.lang.Long.toBinaryString(docHash).length() + "bits \n", articleHashValues, Charsets.UTF_8);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                documentSimHashes.put(id, docHash);

                //send updates to GUI
                value++;
                if (value == increment) {
                    progress += 1;
                    notify(progress);
                    value = 0;
                }

                checkInterruption();
            }

        }
        notify(40);
        return documentSimHashes;
    }

    private void findDuplicates() throws InterruptedException {

        int progress = 0;

        // Read the documents from database, Integer is id in crime_entity_group and String is corresponding content
        HashMap<Integer, String> documents = dataHandler.readArticlesFromDB();
        //this will contain all the calculated sim hash value (LONG) with related crime entity group id (Integer)
        HashMap<Integer, Long> documentSimHashes = calculateSimHashValues(documents);
        progress=40;

        //if user stop the thread
        checkInterruption();

        File articleHammingDistances = new File("DuplicateDetector\\src\\main\\resources\\hammingDistances.txt");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(articleHammingDistances);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer.print("");
        writer.close();


        int currentDocumentId = 0;
        Iterator iterator = documents.keySet().iterator();
        int increment = documents.keySet().size() / 60;
        int value = 0;
        //to store all the duplicate article ids, this is used to identify duplicates
        //during iteration.
        List<Integer> duplicateArticleIds = new ArrayList();
        //calculate duplicates for each document
        //add duplicate doc ids to a list
        //for each article
        while (iterator.hasNext()) {
            checkInterruption();

            //calculate current doc's simHash
            currentDocumentId = (Integer) iterator.next();

            //if current article is already detected as duplicate
            if (duplicateArticleIds.contains(currentDocumentId)) {
                continue;
            }

            String document = documents.get(currentDocumentId);
            long docHash = simHashCalculator.getSimhash64Value(document);

            //to store duplicates of this news article, in this iteration articles with this
            //ids will mark as duplicates
            List<Integer> similarDocIds = new ArrayList();
            Map<Integer, Integer> docDistances = new HashMap();
            Iterator iter = documentSimHashes.keySet().iterator();

            //if user stop the thread
            checkInterruption();

            //calculate hamming distance to each article and add it to similarDocs ArrayList
            while (iter.hasNext()) {
                checkInterruption();

                int hashDocId = (Integer) iter.next();
                Long hashValue = documentSimHashes.get(hashDocId);
                int distance = hammingDistanceCalculator.getHammingDistance(docHash, hashValue);
                // System.out.println(distance);
                //check the hamming distance difference
                if (distance <= 0) {
                    similarDocIds.add(hashDocId);
                    duplicateArticleIds.add(hashDocId);
                    docDistances.put(hashDocId, distance);
                }
            }

            //if user stop the thread
            checkInterruption();

            //update db to mark duplicate or write to file
            if (!similarDocIds.isEmpty()) {
                System.out.println((Thread.currentThread().getName() + " Duplicate Detector UI Handler-> Documents similar as [" + document + currentDocumentId + "]:\n"));
                try {
                    Files.append("Documents similar as [" + document + " " + currentDocumentId + "]:\n", articleHammingDistances, Charsets.UTF_8);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int i : similarDocIds) {
                    if (i == currentDocumentId) {
                        continue;
                    }
                    System.out.println((Thread.currentThread().getName() + " Duplicate Detector UI Handler-> [" + i + "]\tDistance=[" + docDistances.get(i) + "]\n"));

                    checkInterruption();

                    //mark duplicate CrimeEntityGroups in DB
                    CrimeEntityGroup crimeEntityGroup = dataHandler.fetchCrimeEntityGroup(i);
                    crimeEntityGroup.setLabel("duplicate");
                    dataHandler.updateCrimeEntityGroup(crimeEntityGroup);

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

            //update current entity's label. this must be done after all the duplicates are marked
            //don't swap the order
            CrimeEntityGroup crimeEntityGroup = dataHandler.fetchCrimeEntityGroup(currentDocumentId);
            crimeEntityGroup.setLabel("unique");
            dataHandler.updateCrimeEntityGroup(crimeEntityGroup);

            //send updates to GUI
            value++;
            if (value == increment) {
                progress += 1;
                if(progress>100)
                    progress=100;
                notify(progress);
                value = 0;
            }

            //if user stop the thread
            checkInterruption();

        }

        //set progress to 100, sometimes it may not be exactly equal to 100
        notify(100);

    }

    /**
     * start the process
     */
    public void startDuplicateDetection() {
        try {
            findDuplicates();
        } catch (InterruptedException e) {
            //if user has stopped the process
            System.out.println(Thread.currentThread().getName() + " Duplicate Detector UI Handler-> STOPPED");
            dataHandler.closeDatabase();
        }
    }


    @Override
    public void run() {
        startDuplicateDetection();
    }


    /**
     * helper function to handle interruption
     */
    private void checkInterruption() throws InterruptedException{
        if(Thread.currentThread().isInterrupted()){
            throw new InterruptedException();
        }
    }

    /**
     * helper function to notify observers
     * @param progress
     */
    private void notify(int progress)throws InterruptedException{
        System.out.println("NOTIFIED  "+progress);
        checkInterruption();
        setChanged();
        notifyObservers(progress);
    }

}
