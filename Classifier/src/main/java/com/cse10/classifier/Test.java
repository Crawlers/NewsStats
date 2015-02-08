package com.cse10.classifier;

import com.cse10.article.TrainingArticle;
import com.cse10.database.DatabaseConstants;
import com.cse10.database.DatabaseHandler;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.DatabaseLoader;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by hp on 2/3/2015.
 */
public class Test {

    public static void main(String[] args) {
        /*FeatureVectorTransformer featureVectorTransformer=new FeatureVectorTransformer();
        featureVectorTransformer.configure(1,1,true);
        Instances instances=new GenericDataHandler().loadTrainingData(featureVectorTransformer);
        featureVectorTransformer.setInputFormat(instances);
        featureVectorTransformer.getTransformedArticles(instances,"testData");*/

       /* DatabaseLoader databaseLoader = null;
        try {
            databaseLoader = new DatabaseLoader();
        } catch (Exception e) {
            e.printStackTrace();
        }
        databaseLoader.setUser(DatabaseConstants.DB_USERNAME);
        databaseLoader.setPassword(DatabaseConstants.DB_PASSWORD);
        databaseLoader.setUrl(DatabaseConstants.DB_URL);

        ArrayList<String> queries = new ArrayList<String>();
        //queries.add("SELECT title,content,author,created_date,label FROM article_ceylon_today_2013 where `label` IS NOT NULL");
        //queries.add("SELECT title,content,author,created_date,label FROM article_daily_mirror_2012 where `label` IS NOT NULL");
        //queries.add("SELECT title,content,author,created_date,label FROM article_daily_mirror_2013 where `label` IS NOT NULL");
        //queries.add("SELECT title,content,author,created_date,label FROM article_the_island_2012 where `label` IS NOT NULL");
        queries.add("SELECT title,content,author,created_date,label FROM article_the_island_2013 where `label` IS NOT NULL");

        ListIterator queryIterator = queries.listIterator();
        ArrayList<Instances> instances = new ArrayList<Instances>();
        while (queryIterator.hasNext()) {
            databaseLoader.setQuery((String) queryIterator.next());
            Instances ins = null;
            try {
                ins = databaseLoader.getDataSet();
            } catch (IOException e) {
                e.printStackTrace();
            }
            instances.add(ins);
        }

        //int id, String title, String content, String author,
       // Date createdDate, String newspaper

        ListIterator instanceIterator = instances.listIterator();
        while (instanceIterator.hasNext()) {
            Instances ins = (Instances) instanceIterator.next();
            //for each instance
            for (int i = 0; i < ins.numInstances(); i++) {
                String title=ins.instance(i).stringValue(0);
                String content = ins.instance(i).stringValue(1);
                String author = ins.instance(i).stringValue(2);
                String createdDate =ins.instance(i).stringValue(3);
                String[] date=createdDate.split("T");
                System.out.println(date[0]);
                String label=ins.instance(i).stringValue(4);
                System.out.println(title+" "+content+" "+author);
                TrainingArticle trainingArticle=new TrainingArticle();
                trainingArticle.setTitle(title);
                trainingArticle.setContent(content);
                trainingArticle.setAuthor(author);
                trainingArticle.setCreatedDate(Date.valueOf(date[0]));
                trainingArticle.setLabel(label);
                trainingArticle.setNewspaper("article_the_island");
                DatabaseHandler.insertArticle(trainingArticle);

            }
        }
*/

        FastVector attributeList = new FastVector(2);
        Attribute a1 = new Attribute("text", (FastVector) null);

        FastVector classVal = new FastVector();
        classVal.addElement("crime");
        classVal.addElement("other");
        Attribute c = new Attribute("@@class@@", classVal);

        //add class attribute and news text
        attributeList.addElement(a1);
        attributeList.addElement(c);
        Instances trainingData = new Instances("TrainingNews", attributeList, 0);
        if (trainingData.classIndex() == -1) {
            trainingData.setClassIndex(trainingData.numAttributes() - 1);
        }

        //load training data using database handler
        List<TrainingArticle> trainingArticles=DatabaseHandler.fetchTrainingArticles();
        for(TrainingArticle trainingArticle:trainingArticles){
            Instance inst = new Instance(trainingData.numAttributes());
            inst.setValue(a1, trainingArticle.getContent());
            inst.setValue(c, trainingArticle.getLabel());
            inst.setDataset(trainingData);
            trainingData.add(inst);
        }
        trainingData.setClassIndex(trainingData.numAttributes() - 1);
        System.out.println(trainingData);

        ArffSaver saver = new ArffSaver();
        saver.setInstances(trainingData);
        try {
            String path="Classifier\\src\\main\\resources\\testData\\rawTestData.txt";
            saver.setFile(new File(path));
            saver.writeBatch();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
