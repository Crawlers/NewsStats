package com.cse10.classifier;

import com.cse10.database.DatabaseConstants;
import com.cse10.database.DatabaseHandler;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * handle data base transactions
 * Created by Chamath on 12/16/2014.
 */
public abstract class DataHandler {

    protected HashMap<Integer, Integer> articleIds;
    protected String fileName;

    public DataHandler() {
        articleIds = new HashMap<Integer, Integer>();
        fileName = "file.arff";
    }

    protected abstract void printDescription();

    /**
     * fetch training data from database
     * @param featureVectorTransformer
     */
    public abstract Instances loadTrainingData(FeatureVectorTransformer featureVectorTransformer);

    /**
     * fetch test data under given conditions
     *
     * @param articleClass which type of articles that need to be classified (ex:- CeylonTodayArticle.class)
     * @param constrain    specify WHERE clause including 'where '
     * @return
     * @throws Exception
     */
    public Instances loadTestData(Class articleClass, String constrain, boolean isApplyingKeyWordFilter) {

        FastVector attributeList = new FastVector(2);
        KeyWordClassifierHandler keyWordClassifierHandler=new KeyWordClassifierHandler();
        keyWordClassifierHandler.configure(1,1,"\\W");
        articleIds.clear();
        Attribute a1 = new Attribute("text", (FastVector) null);

        FastVector classVal = new FastVector();
        classVal.addElement("crime");
        classVal.addElement("other");
        Attribute c = new Attribute("@@class@@", classVal);

        //add class attribute and news text
        attributeList.addElement(a1);
        attributeList.addElement(c);

        Instances testData = new Instances("TestNews", attributeList, 0);
        if (testData.classIndex() == -1) {
            testData.setClassIndex(testData.numAttributes() - 1);
        }

        String tableName = new DatabaseConstants().classToTableName.get(articleClass);
        String q = "SELECT id,content FROM " + tableName + " " + constrain;
        ResultSet rs = DatabaseHandler.executeQuery(q);

        int instNumber = 0;
        try {
            while (rs.next()) {

                int id = rs.getInt("id");
                String news = rs.getString("content");
                Instance inst = new Instance(testData.numAttributes());
                inst.setValue(a1, news);
                inst.setDataset(testData);
                inst.setClassMissing();
                //if we apply key word filter, we remove obvious non-crime articles first
                if (isApplyingKeyWordFilter) {
                   double value=keyWordClassifierHandler.classifyInstance(inst);
                    if(value==0.0){
                        testData.add(inst);
                        articleIds.put(instNumber, id); //in order to keep track of ID
                        instNumber++;
                    }
                } else {
                    testData.add(inst);
                    articleIds.put(instNumber, id); //in order to keep track of ID
                    instNumber++;
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return testData;
    }

    public HashMap<Integer, Integer> getArticleIds() {
        return articleIds;
    }

    public String getFileName() {
        return fileName;
    }

}