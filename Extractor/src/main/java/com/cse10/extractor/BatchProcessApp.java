package com.cse10.extractor;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 7/24/14
 * Time: 6:34 PM
 * To change this template use File | Settings | File Templates.
 */
import com.cse10.article.Article;
import gate.Document;
import gate.Corpus;
import gate.CorpusController;
import gate.AnnotationSet;
import gate.Gate;
import gate.Factory;
import gate.annotation.AnnotationImpl;
import gate.creole.ResourceInstantiationException;
import gate.util.*;
import gate.util.persistence.PersistenceManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStreamWriter;
import com.cse10.database.DatabaseHandler;

public class BatchProcessApp {

   public static void main(String[] args) throws Exception {

        File gateHome = new File("/home/isuru/Programs/GATE/gate-7.1-build4485-ALL");
        gate.Gate.setGateHome(gateHome);

        // initialise GATE - this must be done before calling any GATE APIs
        Gate.init();

        // load the saved application
        CorpusController application;
        application = (CorpusController) PersistenceManager.loadObjectFromFile(gappFile);

        Corpus corpus = Factory.newCorpus("BatchProcessApp Corpus");
        application.setCorpus(corpus);

       ArrayList<Article> articles = DatabaseHandler.fettchArticle();

        // process the files one by one
        for(int i = 0; i < articles.size(); i++) {
            Article currentArticle = articles.get(i);

            // load the document (using the specified encoding if one was given)
            Document doc = Factory.newDocument(currentArticle.getContent());

            // put the document in the corpus
            corpus.add(doc);

            // run the application
            application.execute();

            // remove the document from the corpus again
            corpus.clear();

            String docXMLString = null;
            Set annotationsToWrite = new HashSet();
            // if we want to just write out specific annotation types, we must
            // extract the annotations into a Set
            if(annotTypesToWrite != null) {
                // Create a temporary Set to hold the annotations we wish to write out

                // we only extract annotations from the default (unnamed) AnnotationSet
                // in this example
                AnnotationSet defaultAnnots = doc.getAnnotations();
                Iterator annotTypesIt = annotTypesToWrite.iterator();
                while(annotTypesIt.hasNext()) {
                    // extract all the annotations of each requested type and add them to
                    // the temporary set
                    AnnotationSet annotsOfThisType =
                            defaultAnnots.get((String)annotTypesIt.next());
                    if(annotsOfThisType != null) {
                        annotationsToWrite.addAll(annotsOfThisType);
                    }
                }
            }


            System.out.println("Article : "+i+" -Begins Here-");
            // Release the document, as it is no longer needed
            Factory.deleteResource(doc);

            Iterator annotIt = annotationsToWrite.iterator();
            while(annotIt.hasNext()) {
                // extract all the annotations of each requested type and add them to
                // the temporary set
                AnnotationImpl CurrentAnnot = (AnnotationImpl)annotIt.next();
                String antText = gate.Utils.stringFor(doc,CurrentAnnot);
                if(CurrentAnnot.getType().equalsIgnoreCase("Person")){
                    System.out.println("Person : "+antText);
                }   else if(CurrentAnnot.getType().equalsIgnoreCase("Organization")){
                    System.out.println("Organization : "+antText);
                }  else if(CurrentAnnot.getType().equalsIgnoreCase("Date")){
                    System.out.println("Date : "+antText);
                }
            }
            System.out.println("Article : "+i+" -Ends Here-");

            System.out.println("done");
        } // for each file

        System.out.println("All done");
    } // void main(String[] args)


    /** Path to the saved application file. */
    private static File gappFile = new File("Extractor/src/main/resources/PersonSearch.gapp");

    /**
     * List of annotation types to write out.  If null, write everything as
     * GateXML.
     */
    private static List annotTypesToWrite = new ArrayList<String>(Arrays.asList("Person","Organization","Date"));

    /**
     * The character encoding to use when loading the docments.  If null, the
     * platform default encoding is used.
     */
    private static String encoding = null;

}