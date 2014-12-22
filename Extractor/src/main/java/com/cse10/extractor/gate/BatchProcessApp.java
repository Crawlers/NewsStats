package com.cse10.extractor.gate;

/**
 * Created with IntelliJ IDEA.
 * User: Isuru Jayaweera
 * Date: 10/24/14
 * Extract entities from crime news articles and add those entities to a separate table.
 */

import com.cse10.article.Article;
import com.cse10.article.NewsFirstArticle;
import com.cse10.database.DatabaseHandler;
import com.cse10.entities.CrimeEntityGroup;
import gate.*;
import gate.Gate;
import gate.annotation.AnnotationImpl;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.*;

import org.jdom.JDOMException;

public class BatchProcessApp {

    // Path to the saved application file.
    private static File gappFile = new File("Extractor/src/main/resources/Location_v1.gapp");

    // List of annotation types to write out.  If null, write everything as GateXML.
    private static List annotTypesToWrite = new ArrayList<>(Arrays.asList("CrimeLocation", "ArticleType"));

    // fetch district name from google map api response
    private static DistrictExtractor de = new DistrictExtractor();

    // stores entities temporary till they are inserted to the table
    private static ArrayList<CrimeEntityGroup> entityGroupsList = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        // setting gate.home variable
        String homePath = "\\home";
        File gateHome;

        if (Gate.getGateHome() == null)
        {
            homePath = System.getenv("GATE_HOME");

            if(homePath == null) {
                System.out.print("Enter GATE Home path : ");
                BufferedReader br =
                        new BufferedReader(new InputStreamReader(System.in));
                homePath = br.readLine();
            }
        }

        File pathCheck = new File(homePath+"\\gate.xml");
        if (pathCheck.exists()){
            gateHome = new File(homePath);
            Gate.setGateHome(gateHome);
            System.out.println("GATE Home Configured : "+ Gate.getGateHome());
        }else{
            System.out.println("GATE Home Path Incorrect");
            System.exit(0);
        }

        // initialise GATE
        Gate.init();

        // load the saved application
        CorpusController application;
        application = (CorpusController) PersistenceManager.loadObjectFromFile(gappFile);

        Corpus corpus = Factory.newCorpus("BatchProcessApp Corpus");
        application.setCorpus(corpus);

        // fetches news articles from database
        List<Article> articles = DatabaseHandler.fetchArticlesByIdRange(NewsFirstArticle.class, 0, 756);

        // process the files one by one
        for (int i = 0; i < articles.size(); i++) {
            Article currentArticle = articles.get(i);

            // load the document
            Document doc = null;
            try {
                doc = Factory.newDocument(currentArticle.getContent());
            } catch (OutOfMemoryError e) {
                handleOutOfMemory(doc);
                continue;
            }

            // put the document in the corpus
            corpus.add(doc);

            // run the application
            try {
                application.execute();
            } catch (OutOfMemoryError e) {
                handleOutOfMemory(doc);
                continue;
            }


            // remove the document from the corpus again
            corpus.clear();

            Set annotationsToWrite = new HashSet();

            // extracting the annotations into a Set
            if (annotTypesToWrite != null) {

                // extracting annotations from the default AnnotationSet
                AnnotationSet defaultAnnots = doc.getAnnotations();
                Iterator annotTypesIt = annotTypesToWrite.iterator();
                while (annotTypesIt.hasNext()) {
                    // extracting all the annotations of each requested type and add them to
                    // the temporary set
                    AnnotationSet annotsOfThisType =
                            defaultAnnots.get((String) annotTypesIt.next());
                    if (annotsOfThisType != null) {
                        annotationsToWrite.addAll(annotsOfThisType);
                    }
                }
            }

            System.out.println("Article : " + i + " -Begins Here-");

            // Release the document
            Factory.deleteResource(doc);

            // location related entities
            String district = "NULL";
            String location = "NULL";
            String crimeType = "NULL";
            CrimeEntityGroup entityGroupOfArticle = new CrimeEntityGroup();

            // iterate through each annotation
            Iterator annotIt = annotationsToWrite.iterator();
            while (annotIt.hasNext()) {
                // extract all the annotations of each requested type and add them to
                // the temporary set
                AnnotationImpl CurrentAnnot = (AnnotationImpl) annotIt.next();
                String antText = gate.Utils.stringFor(doc, CurrentAnnot);

                // check for crime location annotation
                if (CurrentAnnot.getType().equalsIgnoreCase("CrimeLocation")) {
                    System.out.println("CrimeLocation : " + antText);
                    location = antText;

                    // fetch district for the location using google map api
                    district = de.getDistrict(location);
                    if (!district.equalsIgnoreCase("NULL")) {
                        entityGroupOfArticle.setCrimeArticleId(currentArticle.getId());
                        entityGroupOfArticle.setLocation(location);
                        entityGroupOfArticle.setDistrict(district);
                    }
                    System.out.println("District : " + district);
                }

                if (CurrentAnnot.getType().equalsIgnoreCase("ArticleType")) {
                    crimeType = CurrentAnnot.getFeatures().get("article_type").toString();
                    System.out.println("CrimeType : " + crimeType);

                    entityGroupOfArticle.setCrimeType(crimeType);
                }
            }
            System.out.println("Article : " + i + " -Ends Here-");

            if (entityGroupOfArticle.getDistrict() != null){
                entityGroupsList.add(entityGroupOfArticle);
            }

            System.out.println("done");
        } // for each file

        // if any entity has been extracted add them into database table
        if (entityGroupsList.size() > 0) {
            DatabaseHandler.insertCrimeEntityGroups(entityGroupsList);
        }

        System.out.println("All done");
    }

    private static void handleOutOfMemory(Document document){
        Factory.deleteResource(document);
        System.out.println("Memory Full - Leaving the Article");
    }
}