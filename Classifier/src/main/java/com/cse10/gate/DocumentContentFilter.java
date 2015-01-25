package com.cse10.gate;

import gate.*;
import gate.annotation.AnnotationImpl;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.util.GateException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * use to filter the content the filter
 * Created by chamath on 12/20/2014.
 */
public class DocumentContentFilter {
    private Corpus corpus;
    private CorpusPipeLine cp;
    private List annotationsRequired;

    public DocumentContentFilter() {
        try {

            //set gate home configuration
            String homePath = "\\home";
            File gateHome;
            if (Gate.getGateHome() == null) {
                homePath = System.getenv("GATE_HOME");
                if (homePath == null) {
                    //if environment variable is not set then prompt user to enter the path
                    System.out.print("Enter GATE Home path : ");
                    BufferedReader br =
                            new BufferedReader(new InputStreamReader(System.in));
                    try {
                        homePath = br.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("Home Path= " + homePath);
                //check whether the provided gate path is correct
                File pathCheck = new File(homePath + "\\gate.xml");
                if (pathCheck.exists()) {
                    gateHome = new File(homePath);
                    Gate.setGateHome(gateHome);
                    System.out.println("GATE Home Configured : " + Gate.getGateHome());
                } else {
                    System.out.println("GATE Home Path Incorrect");
                    System.exit(0);
                }
            }


         /*   //set gate home
            if(Gate.getGateHome()==null)
            Gate.setGateHome(new File("D:\\software\\FYP\\gate-8.0-build4825-ALL"));*/

            //initialize gate
            Gate.init();
        } catch (GateException ex) {
            Logger.getLogger(DocumentContentFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        annotationsRequired = new ArrayList();
        ArrayList<String> annotationType = new ArrayList();
        annotationType.add("Token");

        ListIterator iter = annotationType.listIterator();
        while (iter.hasNext()) {
            String annotation = (String) iter.next();
            annotationsRequired.add(annotation);
        }
        try {
            // create corpus
            corpus = Factory.newCorpus("StandAloneAnnie corpus");
        } catch (ResourceInstantiationException ex) {
            Logger.getLogger(DocumentContentFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        cp = new CorpusPipeLine();
        cp.configure(true);
    }

    /**
     * get filtered content of the given document content
     *
     * @param content
     * @return
     */
    public String getFilterdContent(String content) {
        Document doc;
        String filteredContent = "";
        try {
            doc = Factory.newDocument(content); // create new gate document
            corpus.add(doc);
            cp.setCorpus(corpus);
            try {
                cp.execute();
            } catch (ExecutionException ex) {
                Logger.getLogger(DocumentContentFilter.class.getName()).log(Level.SEVERE, null, ex);
            }
            corpus.clear();
            String docXMLString = null;
            Set annotationsToWrite = new HashSet();
            /**
             * if we want to just write out specific annotation types, we must
             * extract the annotations into a Set
             */
            if (annotationsRequired != null) {
                /**
                 * Create a temporary Set to hold the annotations we wish to
                 * write out // we only extract annotations from the default
                 * (unnamed) AnnotationSet in this example *
                 */
                AnnotationSet defaultAnnots = doc.getAnnotations();
                Iterator annotTypesIt = annotationsRequired.iterator();
                while (annotTypesIt.hasNext()) {
                    /**
                     * extract all the annotations of each requested type and
                     * add them to the temporary set*
                     */
                    AnnotationSet annotsOfThisType =
                            defaultAnnots.get((String) annotTypesIt.next());
                    if (annotsOfThisType != null) {
                        annotationsToWrite.addAll(annotsOfThisType);
                    }
                }
            }

            // Release the document, as it is no longer needed
            Factory.deleteResource(doc);

            Iterator annotIt = annotationsToWrite.iterator();
            while (annotIt.hasNext()) {
                // extract all the annotations of each requested type and add them to
                // the temporary set
                AnnotationImpl CurrentAnnot = (AnnotationImpl) annotIt.next();

                if (CurrentAnnot.getType().equalsIgnoreCase("Token") && (CurrentAnnot.getFeatures().get("category") == "NN" || CurrentAnnot.getFeatures().get("category") == "NNS")) {
                    filteredContent = filteredContent.concat(CurrentAnnot.getFeatures().get("string").toString());
                    filteredContent = filteredContent.concat(" ");
                } else if (CurrentAnnot.getType().equalsIgnoreCase("Token") && (CurrentAnnot.getFeatures().get("category") == "JJ" || CurrentAnnot.getFeatures().get("category") == "JJR" || CurrentAnnot.getFeatures().get("category") == "JJS")) {
                    filteredContent = filteredContent.concat(CurrentAnnot.getFeatures().get("string").toString());
                    filteredContent = filteredContent.concat(" ");
                } else if (CurrentAnnot.getType().equalsIgnoreCase("Token") && (CurrentAnnot.getFeatures().get("category") == "RB" || CurrentAnnot.getFeatures().get("category") == "RBR" || CurrentAnnot.getFeatures().get("category") == "RBS")) {
                    filteredContent = filteredContent.concat(CurrentAnnot.getFeatures().get("string").toString());
                    filteredContent = filteredContent.concat(" ");
                } else if (CurrentAnnot.getType().equalsIgnoreCase("Token") && (CurrentAnnot.getFeatures().get("category").equals("VBD") || CurrentAnnot.getFeatures().get("category") == "VBG" || CurrentAnnot.getFeatures().get("category") == "VBN" || CurrentAnnot.getFeatures().get("category") == "VBP" || CurrentAnnot.getFeatures().get("category") == "VB" || CurrentAnnot.getFeatures().get("category") == "VBZ")) {
                    filteredContent = filteredContent.concat(CurrentAnnot.getFeatures().get("string").toString());
                    filteredContent = filteredContent.concat(" ");
                }
            }

        } catch (ResourceInstantiationException ex) {
        }
        return filteredContent;
    }

    public static void main(String[] args) {
        DocumentContentFilter documentContentFilter = new DocumentContentFilter();
        String s = "Brothers killed in Gandara. Two brothers have been killed after being assaulted with an axe in the Nawadunna area in \n" +
                "Gandara. Police said an unidentified group has carried out the alleged murder at around 8.30 on Friday night.\n" +
                "The individuals succumbed to their injuries after being admitted to the Matara Hospital.The deceased are aged \n" +
                "36 and 38 years The suspects are said to have fled the area. Police investigations have been launched to arrest the \n" +
                "suspects.";
        System.out.println(documentContentFilter.getFilterdContent(s));
    }

}

