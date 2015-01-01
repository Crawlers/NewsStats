package com.cse10.classifier;

import com.cse10.database.DatabaseConstants;
import com.cse10.gate.DocumentContentFilter;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.DatabaseLoader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Created by chamath on 12/20/2014.
 */
public class DataHandlerWithGate extends DataHandler {

    private DocumentContentFilter documentContentFilter;

    public DataHandlerWithGate() {

        fileName = "dataWithGate";
        documentContentFilter = new DocumentContentFilter();
    }

    @Override
    protected void printDescription() {
        System.out.println("This data handler will load training data and filter out nouns,adjectives,verbs and adverbs from article content");
    }

    /**
     * fetch training data
     *
     * @return Instances
     * @throws Exception
     * @param featureVectorTransformer
     */
    public Instances loadTrainingData(FeatureVectorTransformer featureVectorTransformer) {
        printDescription();
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
        DatabaseLoader databaseLoader = null;
        try {
            databaseLoader = new DatabaseLoader();
        } catch (Exception e) {
            e.printStackTrace();
        }
        databaseLoader.setUser(DatabaseConstants.DB_USERNAME);
        databaseLoader.setPassword(DatabaseConstants.DB_PASSWORD);

        ArrayList<String> queries = new ArrayList<String>();
        queries.add("SELECT content, label FROM article_ceylon_today_2013 where `label` IS NOT NULL");
        queries.add("SELECT content, label FROM article_daily_mirror_2012 where `label` IS NOT NULL");
        queries.add("SELECT content, label FROM article_daily_mirror_2013 where `label` IS NOT NULL");
        queries.add("SELECT content, label FROM article_the_island_2012 where `label` IS NOT NULL");
        queries.add("SELECT content, label FROM article_the_island_2013 where `label` IS NOT NULL");

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

        ListIterator instanceIterator = instances.listIterator();
        while (instanceIterator.hasNext()) {
            Instances ins = (Instances) instanceIterator.next();
            //for each instance
            for (int i = 0; i < ins.numInstances(); i++) {
                String news = ins.instance(i).stringValue(0);
                String label = ins.instance(i).stringValue(1);
                Instance inst = new Instance(trainingData.numAttributes());
                inst.setValue(a1, documentContentFilter.getFilterdContent(news));
                inst.setValue(c, label);
                inst.setDataset(trainingData);
                trainingData.add(inst);
            }
        }
        trainingData.setClassIndex(trainingData.numAttributes() - 1);
        return trainingData;
    }


}
