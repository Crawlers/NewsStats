package com.cse10.classifier;

import com.cse10.article.TrainingArticle;
import com.cse10.database.DatabaseHandler;
import com.cse10.gate.DocumentContentFilter;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import java.util.List;


/**
 * load training data after filtering the content
 * Created by Chamath on 12/20/2014.
 */
public class DataHandlerWithGate extends DataHandler {

    private DocumentContentFilter documentContentFilter;

    public DataHandlerWithGate() {

        fileName = "dataWithGate";
        documentContentFilter = new DocumentContentFilter();
    }

    @Override
    protected void printDescription() {
        System.out.println("This data handler will load training data and filter nouns,adjectives,verbs and adverbs from article content");
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
        Attribute content = new Attribute("text", (FastVector) null);

        FastVector classVal = new FastVector();
        classVal.addElement("crime");
        classVal.addElement("other");
        Attribute classValue = new Attribute("@@class@@", classVal);

        //add class attribute and news text
        attributeList.addElement(content);
        attributeList.addElement(classValue);
        Instances trainingData = new Instances("TrainingNews", attributeList, 0);
        if (trainingData.classIndex() == -1) {
            trainingData.setClassIndex(trainingData.numAttributes() - 1);
        }

        //load training data using database handler
        List<TrainingArticle> trainingArticles= DatabaseHandler.fetchTrainingArticles();
        for(TrainingArticle trainingArticle:trainingArticles){
            Instance inst = new Instance(trainingData.numAttributes());
            inst.setValue(content, documentContentFilter.getFilterdContent(trainingArticle.getContent()));
            inst.setValue(classValue, trainingArticle.getLabel());
            inst.setDataset(trainingData);
            trainingData.add(inst);
        }
        trainingData.setClassIndex(trainingData.numAttributes() - 1);
        return trainingData;
    }


}
