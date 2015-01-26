package com.cse10.analyzer;


/**
 * Created by Sampath on 1/16/15.
 */
public class Analyzer {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        //generating statistics
        StatGenerator statGen = new StatGenerator();
        statGen.generateStats();

        //predicting
        String table = "predictions_type";
        String[] fields1= {"crime_type", "crime_district"};
        String[] fields2= {"crime_type"};
        String[] fields3= {"crime_district"};
        Predictor predictor = new Predictor("predictions",fields1);
        Predictor predictor_type = new Predictor("predictions_type",fields2);
        Predictor predictor_district = new Predictor("predictions_district",fields3);

        String[] quarters1 = {"2012 - 1", "2012 - 2", "2012 - 3", "2012 - 4", "2013 - 1", "2013 - 2", "2013 - 3", "2013 - 4"};
        predictor.predict(quarters1,"2014 - 1");
        predictor_type.predict(quarters1, "2014 - 1");
        predictor_district.predict(quarters1,"2014 - 1");

        String[] quarters2 = {"2012 - 1", "2012 - 2", "2012 - 3", "2012 - 4", "2013 - 1", "2013 - 2", "2013 - 3", "2013 - 4", "2014 - 1"};
        predictor.predict(quarters2,"2014 - 2");
        predictor_type.predict(quarters2, "2014 - 2");
        predictor_district.predict(quarters2,"2014 - 2");

        String[] quarters3 = {"2012 - 1", "2012 - 2", "2012 - 3", "2012 - 4", "2013 - 1", "2013 - 2", "2013 - 3", "2013 - 4", "2014 - 1", "2014 - 2"};
        predictor.predict(quarters3,"2014 - 3");
        predictor_type.predict(quarters3, "2014 - 3");
        predictor_district.predict(quarters3,"2014 - 3");

        String[] quarters4 = {"2012 - 1", "2012 - 2", "2012 - 3", "2012 - 4", "2013 - 1", "2013 - 2", "2013 - 3", "2013 - 4", "2014 - 1", "2014 - 2", "2014 - 3"};
        predictor.predict(quarters4,"2014 - 4");
        predictor_type.predict(quarters4, "2014 - 4");
        predictor_district.predict(quarters4,"2014 - 4");

        String[] quarters = {"2012 - 1", "2012 - 2", "2012 - 3", "2012 - 4", "2013 - 1", "2013 - 2", "2013 - 3", "2013 - 4", "2014 - 1", "2014 - 2", "2014 - 3", "2014 - 4"};
        predictor.predict(quarters,"2015 - 1");
        predictor_type.predict(quarters, "2015 - 1");
        predictor_district.predict(quarters,"2015 - 1");

        predictor.predict(quarters,"2015 - 2");
        predictor_type.predict(quarters, "2015 - 2");
        predictor_district.predict(quarters,"2015 - 2");

        predictor.predict(quarters,"2015 - 3");
        predictor_type.predict(quarters, "2015 - 3");
        predictor_district.predict(quarters,"2015 - 3");

        predictor.predict(quarters,"2015 - 4");
        predictor_type.predict(quarters, "2015 - 4");
        predictor_district.predict(quarters,"2015 - 4");
    }
}
