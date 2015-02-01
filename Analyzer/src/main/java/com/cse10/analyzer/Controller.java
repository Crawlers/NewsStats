package com.cse10.analyzer;


/**
 * Created by Sampath on 1/16/15.
 */
public class Controller {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){

        //generating statistics
        Analyzer statGen = new Analyzer();
        statGen.generateStats();

        //predicting
        String table = "predictions_type";
        String[] fields1= {"crime_type", "crime_district"};
        String[] fields2= {"crime_type"};
        String[] fields3= {"crime_district"};
        PredictorAlgorithm enlAlgo = new ENLPredictorAlgorithm();
        Predictor predictor = new Predictor(enlAlgo,"predictions",fields1);
        Predictor predictor_type = new Predictor(enlAlgo,"predictions_type",fields2);
        Predictor predictor_district = new Predictor(enlAlgo,"predictions_district",fields3);

        String[] quarters1 = {"2012 - 1", "2012 - 2", "2012 - 3", "2012 - 4", "2013 - 1", "2013 - 2", "2013 - 3", "2013 - 4"};
        predictor.predict(quarters1,"2014 - 1",8);
        predictor_type.predict(quarters1, "2014 - 1",8);
        predictor_district.predict(quarters1,"2014 - 1",8);

        String[] quarters2 = {"2012 - 1", "2012 - 2", "2012 - 3", "2012 - 4", "2013 - 1", "2013 - 2", "2013 - 3", "2013 - 4", "2014 - 1"};
        predictor.predict(quarters2,"2014 - 2",9);
        predictor_type.predict(quarters2, "2014 - 2",9);
        predictor_district.predict(quarters2,"2014 - 2",9);

        String[] quarters3 = {"2012 - 1", "2012 - 2", "2012 - 3", "2012 - 4", "2013 - 1", "2013 - 2", "2013 - 3", "2013 - 4", "2014 - 1", "2014 - 2"};
        predictor.predict(quarters3,"2014 - 3",10);
        predictor_type.predict(quarters3, "2014 - 3",10);
        predictor_district.predict(quarters3,"2014 - 3",10);

        String[] quarters4 = {"2012 - 1", "2012 - 2", "2012 - 3", "2012 - 4", "2013 - 1", "2013 - 2", "2013 - 3", "2013 - 4", "2014 - 1", "2014 - 2", "2014 - 3"};
        predictor.predict(quarters4,"2014 - 4",11);
        predictor_type.predict(quarters4, "2014 - 4",11);
        predictor_district.predict(quarters4,"2014 - 4",11);

        String[] quarters = {"2012 - 1", "2012 - 2", "2012 - 3", "2012 - 4", "2013 - 1", "2013 - 2", "2013 - 3", "2013 - 4", "2014 - 1", "2014 - 2", "2014 - 3", "2014 - 4"};
        predictor.predict(quarters,"2015 - 1",12);
        predictor_type.predict(quarters, "2015 - 1",12);
        predictor_district.predict(quarters,"2015 - 1",12);

        predictor.predict(quarters,"2015 - 2",13);
        predictor_type.predict(quarters, "2015 - 2",13);
        predictor_district.predict(quarters,"2015 - 2",13);

        predictor.predict(quarters,"2015 - 3",14);
        predictor_type.predict(quarters, "2015 - 3",14);
        predictor_district.predict(quarters,"2015 - 3",14);

        predictor.predict(quarters,"2015 - 4",15);
        predictor_type.predict(quarters, "2015 - 4",15);
        predictor_district.predict(quarters,"2015 - 4",15);

        //uploading
        Uploader uploader = new Uploader("user", "pass", "fyp","ds049219.mongolab.com",49219);
        uploader.update("news_statistics","crimes");
        uploader.update("predictions","predictions");
    }
}
