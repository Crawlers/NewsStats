package com.cse10.classifier;

import com.cse10.article.TrainingArticle;
import com.cse10.database.DatabaseHandler;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import org.apache.log4j.Logger;

import java.util.List;


/**
 * load complete training data set
 * Created by Chamath on 12/18/2014.
 */
public class GenericDataHandler extends DataHandler {

    private Logger log;

    public GenericDataHandler() {
        fileName = "generic";
        log = Logger.getLogger(this.getClass());
    }

    @Override
    protected String printDescription() {
        String description = "Data Handler -> This Data Handler will Load all of the Training Data";
        log.info(description);
        return description;
    }

    /**
     * fetch training data
     *
     * @param featureVectorTransformer
     * @return Instances
     * @throws Exception
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
        List<TrainingArticle> trainingArticles = DatabaseHandler.fetchTrainingArticles();
        for (TrainingArticle trainingArticle : trainingArticles) {
            Instance inst = new Instance(trainingData.numAttributes());
            inst.setValue(content, trainingArticle.getContent());
            inst.setValue(classValue, trainingArticle.getLabel());
            inst.setDataset(trainingData);
            trainingData.add(inst);
        }
        trainingData.setClassIndex(trainingData.numAttributes() - 1);
        return trainingData;
    }
}
