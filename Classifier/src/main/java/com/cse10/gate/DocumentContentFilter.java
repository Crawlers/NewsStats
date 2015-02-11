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
 * Created by Chamath on 12/20/2014.
 */
public class DocumentContentFilter {

    private Corpus corpus;
    private CorpusPipeLine corpusPipeLine;
    private List annotationTypesRequired;
    private boolean isGateHomeConfigured;
    private boolean isCorpusPipeLineConfigured;

    public DocumentContentFilter() {
        isGateHomeConfigured = false;
        isCorpusPipeLineConfigured = false;
    }

    /**
     * configure gate home
     */
    private void configureGateHome() {
        if (!isGateHomeConfigured) {
            String homePath = "\\home";
            File gateHome;
            if (Gate.getGateHome() == null) {
                homePath = System.getenv("GATE_HOME");
                if (homePath == null) {
                    //if environment variable is null, then prompt user to enter the path
                    System.out.print("Enter GATE Home path : ");
                    BufferedReader br =
                            new BufferedReader(new InputStreamReader(System.in));
                    try {
                        homePath = br.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("Gate Home Path= " + homePath);
                //check whether the provided path is correct
                File pathCheck = new File(homePath + "\\gate.xml");
                if (pathCheck.exists()) {
                    gateHome = new File(homePath);
                    Gate.setGateHome(gateHome);
                    System.out.println("GATE Home has been Configured : " + Gate.getGateHome());
                } else {
                    System.out.println("GATE Home Path is Incorrect");
                    System.exit(0);
                }
            }
            isGateHomeConfigured = true;
        }
    }

    /**
     * configure corpus pipe line
     */
    private void configureCorpusPipeLine() {
        if (!isCorpusPipeLineConfigured) {
            try {
                configureGateHome();
                Gate.init();
            } catch (GateException ex) {
                Logger.getLogger(DocumentContentFilter.class.getName()).log(Level.SEVERE, null, ex);
            }
            annotationTypesRequired = new ArrayList();
            ArrayList<String> annotationTypes = new ArrayList();
            annotationTypes.add("Token");

            ListIterator iter = annotationTypes.listIterator();
            while (iter.hasNext()) {
                String annotation = (String) iter.next();
                annotationTypesRequired.add(annotation);
            }
            try {
                // create corpus
                corpus = Factory.newCorpus("StandAloneAnnie corpus");
            } catch (ResourceInstantiationException ex) {
                Logger.getLogger(DocumentContentFilter.class.getName()).log(Level.SEVERE, null, ex);
            }
            corpusPipeLine = new CorpusPipeLine();
            corpusPipeLine.configure(true);
            isCorpusPipeLineConfigured = true;
        }
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
        configureCorpusPipeLine();
        try {
            doc = Factory.newDocument(content); // create new gate document
            corpus.add(doc);
            corpusPipeLine.setCorpus(corpus);
            try {
                corpusPipeLine.execute();
            } catch (ExecutionException ex) {
                Logger.getLogger(DocumentContentFilter.class.getName()).log(Level.SEVERE, null, ex);
            }
            corpus.clear();
            //temporary collection to add annotations of required type
            Set annotationsRequired = new HashSet();

            /**
             * extract the  required annotations into a Set
             */
            if (this.annotationTypesRequired != null) {

                AnnotationSet defaultAnnotations = doc.getAnnotations();
                Iterator annotationsTypesRequiredIterator = this.annotationTypesRequired.iterator();
                while (annotationsTypesRequiredIterator.hasNext()) {
                    /**
                     * extract all the annotations of each required type and
                     * add them to collection
                     */
                    AnnotationSet annotationsOfThisType =
                            defaultAnnotations.get((String) annotationsTypesRequiredIterator.next());
                    if (annotationsOfThisType != null) {
                        annotationsRequired.addAll(annotationsOfThisType);
                    }
                }
            }

            // Release the document, as it is no longer needed
            Factory.deleteResource(doc);

            Iterator annotationsRequiredIterator = annotationsRequired.iterator();
            while (annotationsRequiredIterator.hasNext()) {

                AnnotationImpl currentAnnotation = (AnnotationImpl) annotationsRequiredIterator.next();
                if (currentAnnotation.getType().equalsIgnoreCase("Token") && (currentAnnotation.getFeatures().get("category") == "NN" || currentAnnotation.getFeatures().get("category") == "NNS")) {
                    filteredContent = filteredContent.concat(currentAnnotation.getFeatures().get("string").toString());
                    filteredContent = filteredContent.concat(" ");
                } else if (currentAnnotation.getType().equalsIgnoreCase("Token") && (currentAnnotation.getFeatures().get("category") == "JJ" || currentAnnotation.getFeatures().get("category") == "JJR" || currentAnnotation.getFeatures().get("category") == "JJS")) {
                    filteredContent = filteredContent.concat(currentAnnotation.getFeatures().get("string").toString());
                    filteredContent = filteredContent.concat(" ");
                } else if (currentAnnotation.getType().equalsIgnoreCase("Token") && (currentAnnotation.getFeatures().get("category") == "RB" || currentAnnotation.getFeatures().get("category") == "RBR" || currentAnnotation.getFeatures().get("category") == "RBS")) {
                    filteredContent = filteredContent.concat(currentAnnotation.getFeatures().get("string").toString());
                    filteredContent = filteredContent.concat(" ");
                } else if (currentAnnotation.getType().equalsIgnoreCase("Token") && (currentAnnotation.getFeatures().get("category").equals("VBD") || currentAnnotation.getFeatures().get("category") == "VBG" || currentAnnotation.getFeatures().get("category") == "VBN" || currentAnnotation.getFeatures().get("category") == "VBP" || currentAnnotation.getFeatures().get("category") == "VB" || currentAnnotation.getFeatures().get("category") == "VBZ")) {
                    filteredContent = filteredContent.concat(currentAnnotation.getFeatures().get("string").toString());
                    filteredContent = filteredContent.concat(" ");
                }
            }

        } catch (ResourceInstantiationException ex) {
        }
        return filteredContent;
    }

}

