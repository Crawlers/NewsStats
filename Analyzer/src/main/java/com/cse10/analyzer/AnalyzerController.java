package com.cse10.analyzer;


import com.cse10.database.DatabaseConstants;

import java.util.Observable;

/**
 * Created by Sampath on 1/16/15.
 */
public class AnalyzerController extends Observable {

    private static AnalyzerController analyzerController;

    private AnalyzerController(){
    }

    public static AnalyzerController getInstance(){
        if (analyzerController == null)
            analyzerController = new AnalyzerController();
        return analyzerController;
    }

    /*
     * initiate analysis process
     */
    public void analyze(){
        setProgress(10);
        Analyzer statGen = new Analyzer();
        setProgress(25);
        statGen.generateStats("2012-01-01", "2014-12-31");
        setProgress(100);
    }

    /*
     * initiate prediction process
     */
    public void predict(){
        //Initializing the prediction
        String[] fields1= {"crime_type", "crime_district"};
        String[] fields2= {"crime_type"};
        String[] fields3= {"crime_district"};
        PredictorAlgorithm enlAlgo = new ENLPredictorAlgorithm();
        Predictor predictor = new Predictor(enlAlgo,"predictions",fields1);
        Predictor predictor_type = new Predictor(enlAlgo,"predictions_type",fields2);
        Predictor predictor_district = new Predictor(enlAlgo,"predictions_district",fields3);
        setProgress(10);

        //predictions
        String[] quarters1 = {"2012 - 1", "2012 - 2", "2012 - 3", "2012 - 4", "2013 - 1", "2013 - 2", "2013 - 3", "2013 - 4"};
        predictor.predict(quarters1,"2014 - 1");
        predictor_type.predict(quarters1, "2014 - 1");
        predictor_district.predict(quarters1,"2014 - 1");
        setProgress(20);

        String[] quarters2 = { "2012 - 2", "2012 - 3", "2012 - 4", "2013 - 1", "2013 - 2", "2013 - 3", "2013 - 4", "2014 - 1"};
        predictor.predict(quarters2,"2014 - 2");
        predictor_type.predict(quarters2, "2014 - 2");
        predictor_district.predict(quarters2,"2014 - 2");
        setProgress(30);

        String[] quarters3 = { "2012 - 3", "2012 - 4", "2013 - 1", "2013 - 2", "2013 - 3", "2013 - 4", "2014 - 1", "2014 - 2"};
        predictor.predict(quarters3,"2014 - 3");
        predictor_type.predict(quarters3, "2014 - 3");
        predictor_district.predict(quarters3,"2014 - 3");
        setProgress(40);

        String[] quarters4 = { "2012 - 4", "2013 - 1", "2013 - 2", "2013 - 3", "2013 - 4", "2014 - 1", "2014 - 2", "2014 - 3"};
        predictor.predict(quarters4,"2014 - 4");
        predictor_type.predict(quarters4, "2014 - 4");
        predictor_district.predict(quarters4,"2014 - 4");
        setProgress(50);

        String[] quarters = {"2013 - 1", "2013 - 2", "2013 - 3", "2013 - 4", "2014 - 1", "2014 - 2", "2014 - 3", "2014 - 4"};
        predictor.predict(quarters,"2015 - 1");
        predictor_type.predict(quarters, "2015 - 1");
        predictor_district.predict(quarters,"2015 - 1");
        setProgress(60);

        predictor.predict(quarters,"2015 - 2");
        //predictor_type.predict(quarters, "2015 - 2",13);
        predictor_district.predict(quarters,"2015 - 2");
        setProgress(70);

        predictor.predict(quarters,"2015 - 3");
        //predictor_type.predict(quarters, "2015 - 3",8);
        predictor_district.predict(quarters,"2015 - 3");
        setProgress(80);

        predictor.predict(quarters,"2015 - 4");
        //predictor_type.predict(quarters, "2015 - 4",8);
        predictor_district.predict(quarters,"2015 - 4");
        setProgress(90);

        //measuring accuracy
        System.out.println("mean square error is: "+predictor.getMeanSqureError("predictions"));
        setProgress(100);
    }

    /*
     * initiation upload process
     */
    public void upload(){
        Uploader uploader = new Uploader(DatabaseConstants.WEBGUIDB_USERNAME,
                DatabaseConstants.WEBGUIDB_PASSWORD,
                DatabaseConstants.WEBGUIDB_DATABASE,
                DatabaseConstants.WEBGUIDB_HOST,
                DatabaseConstants.WEBGUIDB_PORT);
        setProgress(10);
        uploader.upload("news_statistics", "crimes");
        setProgress(40);
        uploader.upload("predictions", "predictions");
        setProgress(70);
        uploader.upload("predictions_type", "predictions_type");
        setProgress(100);
    }

    /*
    public static void main(String[] args){
        AnalyzerController ac = AnalyzerController.getInstance();
        ac.analyze();
        ac.predict();
        ac.upload();
    }
    */

    /*
     * notify the progress to admin gui
     */
    private void setProgress(int progress){
        System.out.println(progress);
        setChanged();
        notifyObservers(progress);
    }

}
