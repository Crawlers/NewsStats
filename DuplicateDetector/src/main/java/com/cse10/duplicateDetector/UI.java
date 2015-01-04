package com.cse10.duplicateDetector;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * Created by chamath on 1/3/2015.
 */
public class UI {

    /**
     * currently this method takes strings(documents) from file, should be changed to get from data base
     * @return
     */
    private List<String> readDocs() {

        List<String> documents;
        try {
            documents= Files.readLines(new File("C:\\Users\\hp\\Desktop\\DuplicateDetetcionImplementation\\Implementations\\sim hash\\simhash-java-master\\simhash-java-master\\src\\test_in"), Charsets.UTF_8);
        } catch (IOException e) {
            documents=new ArrayList<String>();
            e.printStackTrace();
        }
        return documents;
    }

    public void findDuplicates() {

        SimHashCalculator simHashCalculator = new SimHashCalculator(new FullWordSegmenter());
        // DocHashes is a list that will contain all of the calculated hashes.
        ArrayList<Long> documentSimHashes = new ArrayList<Long>();
        // Read the documents. (Each line represents a document).
        List<String> documents = readDocs();

        for (String document : documents) {
            // Calculate the document sim hash.
            long docHash = simHashCalculator.simhash64(document);
            System.out.println("Document=[" + document + "] Hash=[" + docHash + " , " + Long.toBinaryString(docHash) + "]" + "Bit Length of Hash:" + Long.toBinaryString(docHash).length() + "bits");
            // Store the document hash in a list.
            documentSimHashes.add(docHash);
        }

        int currentDocumentId = 0;
        for (String document : documents) {
            long docHash = simHashCalculator.simhash64(document);
            List<Integer> similarDocs = Lists.newLinkedList();
            Map<Integer, Integer> docDistances = Maps.newHashMap();

            for (int i = 0; i < documentSimHashes.size(); i++) {
                int dist = simHashCalculator.hammingDistance(docHash, documentSimHashes.get(i));
                // System.out.println(dist);
                if (dist <= 12) {
                    similarDocs.add(i);
                    docDistances.put(i, dist);
                }
            }

            if (!similarDocs.isEmpty()) {
                System.out.println(("Documents similar as [" + document + "]:\n"));
                for (int i : similarDocs) {
                    if (i == currentDocumentId) {
                        continue;
                    }
                    System.out.println(("[" + i + "]\tDistance=[" + docDistances.get(i) + "]\n"));
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
