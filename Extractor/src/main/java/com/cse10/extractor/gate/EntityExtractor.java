package com.cse10.extractor.gate;

/**
 * Created with IntelliJ IDEA.
 * User: Isuru Jayaweera
 * Date: 01/26/15
 * Extract entities from crime news articles and add those entities to a separate table.
 */

import com.cse10.article.Article;
import com.cse10.article.CrimeArticle;
import com.cse10.database.DatabaseHandler;
import com.cse10.entities.CrimeEntityGroup;
import com.cse10.entities.LocationDistrictMapper;
import gate.*;
import gate.annotation.AnnotationImpl;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;
import org.apache.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.exception.DataException;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class EntityExtractor extends Observable {

    // path to the saved application file.
    private File gappFile;

    // path to the configuration file containing ID of the last extracted entity.
    private File configFile;

    // list of annotation types to write out.  If null, write everything as GateXML.
    private List annotTypesToWrite;

    // fetch district name from google map api response
    private DistrictExtractor de;

    // stores entities temporary till they are inserted to the table
    private ArrayList<CrimeEntityGroup> entityGroupsList;

    // ID of the last entity extracted article
    private int endID;

    // declare logger
    private Logger logger;

    // constructor
    EntityExtractor(){
        gappFile = new File("Extractor/src/main/resources/Complete_v1.gapp");
        configFile = new File("Extractor/src/main/resources/Configuration.txt");
        annotTypesToWrite = new ArrayList<>(Arrays.asList("CrimeLocation", "ArticleType", "Police", "Court", "CrimeDate", "CrimePerson"));
        logger = Logger.getLogger(this.getClass());
        de = new DistrictExtractor();
        entityGroupsList = new ArrayList<>();
    }

    public synchronized boolean startExtraction() throws InterruptedException, IOException, GateException, ParseException {
        // to check whether execution was successful or not
        boolean isSuccessful = false;

        //get ID of the article to start entity extraction.
        int startID = getLastID();

        //set ID of the last entity extracted article to starting article.
        endID = startID;

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
        List<Article> articles = DatabaseHandler.fetchArticlesByIdStarting(CrimeArticle.class,startID+1);

        ArrayList<CrimeEntityGroup> resultsList;

        resultsList = executeProcessPipeline(articles, corpus, application);

        if(resultsList != null && !resultsList.isEmpty()){
            isSuccessful = true;
        }

        DatabaseHandler.closeDatabase();
        logger.info("All done");

        return isSuccessful;
    }

    // method to fetch district for the location using google map api, unless it is in the location - district
    // mapping table
    public void resolveLocation(String location, CrimeEntityGroup entityGroupOfArticle, int articleID) {
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

    private int getLastID() throws InterruptedException{
        int theID = 0;

        BufferedReader br = null;

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader(configFile));

            if ((sCurrentLine = br.readLine()) != null) {
                theID = Integer.parseInt(sCurrentLine);
            }

        } catch (IOException e) {
            logger.info("Configuration File Not Found : ",e);
            DatabaseHandler.closeDatabase();
            throw new InterruptedException("Thread interruption forced.");
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return  theID;
    }

    private void writeLastID(){
        FileWriter fooWriter = null; // true to append
        try {
            fooWriter = new FileWriter(configFile, false);
            // false to overwrite.
            fooWriter.write(String.valueOf(endID));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fooWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private File getGATEHome() throws InterruptedException{
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

    protected ArrayList<CrimeEntityGroup> executeProcessPipeline(List<Article> articles, Corpus corpus,  CorpusController application) throws InterruptedException, GateException, ParseException {

        // number of articles has to be entity extracted to progress the progress bar by 1 step.
        int uiStepSize = articles.size()/100;

        // progress of the entity extraction process
        int currentProgress = 1;

        // list of extracted entity groups
        ArrayList<CrimeEntityGroup> crimeEntityGroupList = new ArrayList<CrimeEntityGroup>();

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
                    String antText = Utils.stringFor(doc, CurrentAnnot);

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

                    // add to local list of crime entity sets
                    crimeEntityGroupList.add(entityGroupOfArticle);

                    // insert people involved in the crime to crime etity details and add crime entity and people
                    // involved it into the DB
                    DatabaseHandler.insertCrimeDetails(entityGroupOfArticle, crimePeopleSet);
                }

                endID = articleID;

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

                // check whether this thread is interrupted from out side
                if(Thread.interrupted()) {
                    logger.info("Interruption Identified.");
                    DatabaseHandler.closeDatabase();
                    throw new InterruptedException("Thread interruption forced.");
                }
            }

            // updating the progress of the entity extraction process
            if(uiStepSize != 0) {
                if (i % uiStepSize == 0) {
                    logger.info("Progress updating.");
                    currentProgress = i / uiStepSize;
                    if(currentProgress < 100) {
                        setChanged();
                        notifyObservers(currentProgress);
                    }
                }
            }
        }// for each article

        System.out.println("Progress updating.");
        currentProgress = 100;
        setChanged();
        notifyObservers(currentProgress);

        return  crimeEntityGroupList;
    }

    public synchronized boolean stopExtraction(){
        writeLastID();
        return true;
    }
}

