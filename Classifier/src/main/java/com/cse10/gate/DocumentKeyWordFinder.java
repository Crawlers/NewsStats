package com.cse10.gate;

import gate.*;
import gate.annotation.AnnotationImpl;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.util.GateException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * use to filter articles by keywords
 * I have updated gate gazetter lists with crime keyword list
 * Created by chamath on 12/21/2014.
 */
public class DocumentKeyWordFinder {
    private Corpus corpus;
    private CorpusPipeLine cp;
    private List annotationsRequired;

    public DocumentKeyWordFinder() {
        try {
            Gate.init(); //prepare the library
        } catch (GateException ex) {
            Logger.getLogger(DocumentContentFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        annotationsRequired = new ArrayList();
        ArrayList<String> annotationType = new ArrayList();
        annotationType.add("Lookup");

        ListIterator iter = annotationType.listIterator();
        while (iter.hasNext()) {
            String annotation = (String) iter.next();
            annotationsRequired.add(annotation);
        }
        try {
            corpus = Factory.newCorpus("StandAloneAnnie corpus"); // create corpus
        } catch (ResourceInstantiationException ex) {
            Logger.getLogger(DocumentContentFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        cp = new CorpusPipeLine();
        cp.configure();
    }

    /**
     * check given keywords exist in articles
     *
     * @param content
     * @return
     */
    public boolean isKeyWordExist(String content) {
        Document doc;
        boolean exist = false;
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
             * extract the annotations into a Set *
             */
            if (annotationsRequired != null) {
                /**
                 * Create a temporary Set to hold the annotations we wish to
                 * write out we only extract annotations from the default
                 * (unnamed) AnnotationSet in this example *
                 */
                AnnotationSet defaultAnnots = doc.getAnnotations();
                Iterator annotTypesIt = annotationsRequired.iterator();
                while (annotTypesIt.hasNext()) {
                    /**
                     * extract all the annotations of each requested type and
                     * add them to the temporary set *
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
                /**
                 * extract all the annotations of each requested type and add
                 * them to the temporary set *
                 */
                AnnotationImpl CurrentAnnot = (AnnotationImpl) annotIt.next();
                if (CurrentAnnot.getType().equalsIgnoreCase("Lookup") && CurrentAnnot.getFeatures().get("majorType").equals("crime")) {
                    exist = true;
                }
            }

        } catch (ResourceInstantiationException ex) {
        }
        return exist;
    }

    public static void main(String[] args) {
        DocumentKeyWordFinder documentKeyWordFinder=new DocumentKeyWordFinder();
        boolean keyWordExist= documentKeyWordFinder.isKeyWordExist("Brothers killed in Gandara. Two brothers have been killed after being assaulted with an axe in the Nawadunna area in \n" +
                "Gandara. Police said an unidentified group has carried out the alleged murder at around 8.30 on Friday night.\n" +
                "The individuals succumbed to their injuries after being admitted to the Matara Hospital.The deceased are aged \n" +
                "36 and 38 years The suspects are said to have fled the area. Police investigations have been launched to arrest the \n" +
                "suspects.");
        System.out.println(keyWordExist);
    }
}
