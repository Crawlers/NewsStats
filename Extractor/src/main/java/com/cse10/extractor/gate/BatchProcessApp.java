package com.cse10.extractor.gate;

/**
 * Created with IntelliJ IDEA.
 * User: Isuru Jayaweera
 * Date: 10/24/14
 * Extract entities from crime news articles and add those entities to a separate table.
 */

import com.cse10.article.Article;
import com.cse10.article.CeylonTodayArticle;
import com.cse10.article.CrimeArticle;
import com.cse10.article.NewsFirstArticle;
import com.cse10.database.DatabaseHandler;
import com.cse10.entities.CrimeEntityGroup;
import com.cse10.entities.CrimePerson;
import com.cse10.entities.LocationDistrictMapper;
import com.sun.java.util.jar.pack.*;
import gate.*;
import gate.Gate;
import gate.annotation.AnnotationImpl;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.DataException;
import org.jdom.JDOMException;

public class BatchProcessApp {

    // Path to the saved application file.
    private static File gappFile = new File("Extractor/src/main/resources/Complete_v1.gapp");

    // List of annotation types to write out.  If null, write everything as GateXML.
    private static List annotTypesToWrite = new ArrayList<>(Arrays.asList("CrimeLocation", "ArticleType", "Police", "Court", "CrimeDate", "CrimePerson"));

    // fetch district name from google map api response
    private static DistrictExtractor de = new DistrictExtractor();

    // stores entities temporary till they are inserted to the table
    private static ArrayList<CrimeEntityGroup> entityGroupsList = new ArrayList<>();

    // initialize logger
    private static Logger logger = Logger.getLogger(BatchProcessApp.class);

    public static void main(String[] args) throws Exception {

        // setting gate.home variable
        File gateHome = getGATEHome();

        // initialise GATE
        Gate.init();

        // load the saved application
        CorpusController application;
        application = (CorpusController) PersistenceManager.loadObjectFromFile(gappFile);

        Corpus corpus = Factory.newCorpus("BatchProcessApp Corpus");
        application.setCorpus(corpus);

        // fetches news articles from database
        // List<Article> articles = DatabaseHandler.fetchArticles(CrimeArticle.class);
        List<Article> articles = DatabaseHandler.fetchArticlesByIdRange(CrimeArticle.class, 12290, 12291);


        // process the files one by one
        for (int i = 0; i < articles.size(); i++) {
            Article currentArticle = articles.get(i);
            String articleLabel;

            try {
                articleLabel = currentArticle.getLabel();
            } catch (NullPointerException e) {
                continue;
            }

            if (articleLabel != null && articleLabel.equalsIgnoreCase("crime")) {
                String articleContent;
                Date articleDate;
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

                try {
                    articleContent = currentArticle.getContent();
                    articleDate = currentArticle.getCreatedDate();
                } catch (NullPointerException e) {
                    continue;
                }

                // append created date to the processing document
                articleContent = articleContent + ".::" + articleDate + "::.";

                int articleLength = articleContent.length();
                logger.info("New Article size : " + articleLength);

                if (articleLength > 2500) {
                    articleContent = currentArticle.getTitle();
                }

                // load the document
                Document doc = Factory.newDocument(articleContent);


                // put the document in the corpus
                corpus.add(doc);

                // run the application
                application.execute();

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

                // Release the document
                Factory.deleteResource(doc);

                logger.info("Article : " + i + " -Begins Here-");

                // crime entity details
                String district = "NULL";
                String location = "NULL";
                String police = "NULL";
                String court = "NULL";
                String crimeType = "other";
                String crimePeople = "NULL";
                HashSet<String> crimePeopleSet = new HashSet<>();
                int articleID = currentArticle.getId();
                Date crimeDate = articleDate;
                LocationDistrictMapper locationDistrict;
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
                        location = antText;

                        // fetch district for the location using google map api, unless it is in the location - district
                        // mapping table
                        resolveLocation(location, entityGroupOfArticle, articleID);

                    }

                    // check for crime type annotation and set crime type on crime entity details
                    if (CurrentAnnot.getType().equalsIgnoreCase("ArticleType")) {
                        try {
                            crimeType = CurrentAnnot.getFeatures().get("article_type").toString();
                            entityGroupOfArticle.setCrimeType(crimeType);
                        } catch (NullPointerException e) {
                            logger.info("****** Not normalized : " + currentArticle.getTitle() + " **********");
                        }

                    }

                    // check for crime person annotation and add into a HashSet of crime people
                    if (CurrentAnnot.getType().equalsIgnoreCase("CrimePerson")) {
                        if (!crimePeopleSet.contains(antText)) {
                            crimePeopleSet.add(antText);
                        }
                    }

                    // check for police annotation and set police location on crime entity details
                    if (CurrentAnnot.getType().equalsIgnoreCase("Police")) {
                        police = antText;
                        entityGroupOfArticle.setPolice(police);
                    }

                    // check for court annotation and set court location on crime entity details
                    if (CurrentAnnot.getType().equalsIgnoreCase("Court")) {
                        court = antText;
                        entityGroupOfArticle.setCourt(court);
                    }

                    // check for crime date annotation, get normalized date and parse it to the required date format
                    if (CurrentAnnot.getType().equalsIgnoreCase("CrimeDate")) {
                        try {
                            crimeDate = format.parse(CurrentAnnot.getFeatures().get("normalized").toString());
                        } catch (NullPointerException e) {
                            logger.info("****** Not normalized : " + antText + " **********");
                        }

                    }
                }

                if (entityGroupOfArticle.getLocationDistrict() == null && entityGroupOfArticle.getPolice() != null) {
                    police = entityGroupOfArticle.getPolice();

                    // fetch district for the location using google map api, unless it is in the location - district
                    // mapping table
                    resolveLocation(police, entityGroupOfArticle, articleID);
                }


                if (entityGroupOfArticle.getLocationDistrict() == null && entityGroupOfArticle.getCourt() != null) {
                    court = entityGroupOfArticle.getCourt();

                    // fetch district for the location using google map api, unless it is in the location - district
                    // mapping table
                    resolveLocation(court, entityGroupOfArticle, articleID);
                }

                if (entityGroupOfArticle.getLocationDistrict() != null) {

                    // set crime date on crime entity details
                    entityGroupOfArticle.setCrimeDate(crimeDate);

                    // insert people involved in the crime to crime etity details and add crime entity and people
                    // involved it into the DB
                    //DatabaseHandler.insertCrimeDetails(entityGroupOfArticle, crimePeopleSet);
                }

                // check all crime details are properly entered
                logger.info("CrimeType : " + crimeType);
                logger.info("Crime Date : " + format.format(crimeDate));
                logger.info("Article Title : " + currentArticle.getTitle());
                logger.info("Crime Location : " + location);

                if (entityGroupOfArticle.getLocationDistrict() != null) {
                    logger.info("District : " + entityGroupOfArticle.getLocationDistrict().getDistrict());
                }

                logger.info("Crime People : " + crimePeople);
                logger.info("Police Location : " + police);
                logger.info("Court Location : " + court);

                logger.info("Article : " + i + " -Ends Here-");
                logger.info("");
            }
        }// for each article

        logger.info("All done");

        System.exit(0);
    }

    // method to fetch district for the location using google map api, unless it is in the location - district
    // mapping table
    public static void resolveLocation(String location, CrimeEntityGroup entityGroupOfArticle, int articleID) {
        LocationDistrictMapper locationDistrict;
        String district = "NULL";

        try {

            // try to retrieve district of the location from the location - district mapping table
            locationDistrict = DatabaseHandler.fetchLocation(location);
            district = locationDistrict.getDistrict();
            entityGroupOfArticle.setCrimeArticleId(articleID);
            entityGroupOfArticle.setLocation(location);
            entityGroupOfArticle.setLocationDistrict(locationDistrict);
        } catch (ObjectNotFoundException e) {

            // unless district is present in the location - district mapping table
            // fetch district for the location using google map api and relevant location - district mapping data into
            // the location - district mapping table for future reference
            district = de.getDistrict(location);
            if (!district.equalsIgnoreCase("NULL")) {
                locationDistrict = new LocationDistrictMapper(location, district);
                try {
                    DatabaseHandler.insertLocationDistrict(locationDistrict);
                    entityGroupOfArticle.setCrimeArticleId(articleID);
                    entityGroupOfArticle.setLocation(location);
                    entityGroupOfArticle.setLocationDistrict(locationDistrict);
                }catch (DataException dataE){
                    logger.info("Long district name : "+district+" for location : "+location);
                    district = null;
                }
            }
        }
    }

    private static File getGATEHome() throws InterruptedException{
        String homePath = null;
        File gateHome;

        if (Gate.getGateHome() == null) {
            homePath = System.getenv("GATE_HOME");

            if (homePath == null) {
                System.out.print("Enter GATE Home path : ");
                BufferedReader br =
                        new BufferedReader(new InputStreamReader(System.in));
                try {
                    homePath = br.readLine();
                } catch (IOException e) {
                    logger.info("Incorrect Path");
                    DatabaseHandler.closeDatabase();
                    throw new InterruptedException("Thread interruption forced.");
                }
            }
        }else{
            homePath = Gate.getGateHome().getPath();
        }

        File pathCheck = new File(homePath + "\\gate.xml");
        if (pathCheck.isFile() && Gate.getGateHome() == null) {
            gateHome = new File(homePath);
            Gate.setGateHome(gateHome);
            logger.info("GATE Home Configured : " + Gate.getGateHome());
        } else if(!pathCheck.isFile() && Gate.getGateHome() == null) {
            logger.info("GATE Home Path Incorrect : "+homePath);
            DatabaseHandler.closeDatabase();
            throw new InterruptedException("Thread interruption forced.");
        } else {
            gateHome = new File(homePath);
        }

        return gateHome;
    }
}

