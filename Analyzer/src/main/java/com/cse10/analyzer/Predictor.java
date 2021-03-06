package com.cse10.analyzer;

import com.cse10.database.HibernateUtil;
import mltk.core.*;
import mltk.predictor.glm.ElasticNetLearner;
import mltk.predictor.glm.GLM;
import mltk.util.MathUtils;
import org.apache.commons.math.stat.regression.SimpleRegression;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.math.BigDecimal;
import java.util.*;

/*
 * Main predictor class
 */
public class Predictor{

    private String table;
    private String[] fields;
    private String[] quarters;
    private PredictorAlgorithm predictorAlgo;

    public Predictor(PredictorAlgorithm predictorAlgo, String table, String[] fields){
        this.predictorAlgo = predictorAlgo;
        this.table = table;
        this.fields = fields;

        //empty the table
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.createSQLQuery("truncate table "+table).executeUpdate();
        tx.commit();
        session.close();
    }

    //predict
    public void  predict(String[] quarters, String targetQuarter){
        int indexToPredict = quarters.length;
        this.quarters = quarters;
        Session session = HibernateUtil.getSessionFactory().openSession();
        List results = getInput();
        HashMap<String,Integer> series = getSeriesHolder();
        HashMap pre = (HashMap) results.get(0);
        series.put((String) pre.get("crime_yearquarter"),((BigDecimal) pre.get("count")).intValue());
        //for each time series
        for (int i=1; i<results.size(); i++){
            HashMap ele = (HashMap) results.get(i);
            boolean flag = false;
            for (int j=0; j<fields.length; j++){
                if (!ele.get(fields[j]).equals(pre.get(fields[j]))){
                    flag = true;
                    break;
                }
            }
            if (flag){
                int predicted = predictorAlgo.predict(series);
                insertToDB(pre,targetQuarter,predicted);
                series = getSeriesHolder();
                pre = ele;
            }

            series.put((String) ele.get("crime_yearquarter"),((BigDecimal) ele.get("count")).intValue());
        }
    }

    //get mean square error
    public double getMeanSqureError(String predictionTable){
        Session session = HibernateUtil.getSessionFactory().openSession();


        String sql = "SELECT Sum(val) sum," +
                " Count(val) count," +
                " Sum(val) / Count(val) err" +
                " FROM (SELECT";

        for (int i=0; i<fields.length; i++){
            sql+=" pd."+fields[i]+",";
        }

        sql+=" pd.crime_yearquarter, Pow(count1 - Ifnull(count2, 0), 2) val" +
                " FROM (SELECT";

        for (int i=0; i<fields.length; i++){
            sql+=" "+fields[i]+",";
        }

        sql+=   " crime_yearquarter, Sum(`crime_count`) count1" +
                " FROM   " + predictionTable +
                " WHERE  crime_year < 2015" +
                " AND crime_year > 2013" +
                " GROUP  BY ";

        for (int i=0; i<fields.length; i++){
            sql+=" "+fields[i]+",";
        }

        sql+=" `crime_yearquarter`) pd" +
                " LEFT JOIN (SELECT";

        for (int i=0; i<fields.length; i++){
            sql+=" "+fields[i]+",";
        }

        sql+=" crime_yearquarter, Sum(`crime_count`) count2" +
                " FROM   `news_statistics`" +
                " WHERE  crime_year < 2015" +
                " AND crime_year > 2013" +
                " GROUP  BY";

        for (int i=0; i<fields.length; i++){
            sql+=" "+fields[i]+",";
        }

        sql+=" crime_yearquarter) ns" +
                " ON ";

        sql+=" ns."+fields[0]+" = pd."+fields[0];
        for (int i=1; i<fields.length; i++){
            sql+=" AND ns."+fields[i]+" = pd."+fields[i];
        }

        sql+=" AND ns.`crime_yearquarter` = pd.`crime_yearquarter` ) tbl";

        SQLQuery query = session.createSQLQuery(sql);
        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        List results = query.list();
        session.close();
        return (double) ((HashMap) results.get(0)).get("err");
    }

    //get the input
    protected List  getInput(){
        Session session = HibernateUtil.getSessionFactory().openSession();

        String fieldNames = fields[0];
        for (int i=1; i<fields.length; i++){
            fieldNames+=", "+fields[i];
        }

        String sql = "SELECT "+fieldNames+", crime_yearquarter, sum(crime_count) count" +
                " from news_statistics" +
                " where crime_yearquarter >= '" + quarters[0] + "' AND crime_yearquarter <= '" + quarters[quarters.length-1] + "'"+
                " group by "+fieldNames+", crime_yearquarter" +
                " order by "+fieldNames+", crime_yearquarter";
        SQLQuery query = session.createSQLQuery(sql);
        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        List results = query.list();
        session.close();
        return results;
    }

    //setting the predictor algorithm
    public void setPredictorAlgorithm (PredictorAlgorithm predictorAlgo){
        this.predictorAlgo = predictorAlgo;
    }

    //save the output
    protected void insertToDB(HashMap ele, String key, int count){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        String fieldNames = fields[0];
        String values = (String) ele.get(fields[0]);
        for (int i=1; i<fields.length; i++){
            fieldNames+=", "+fields[i];
            values+="', '"+(String) ele.get(fields[i]);
        }
        fieldNames+=", crime_year, crime_yearquarter, crime_count";

        session.createSQLQuery(
                "INSERT INTO "+table+" ("+fieldNames+") " +
                        " VALUES ('"+
                        values+"', '"+
                        key.substring(0,4)+"', '"+
                        key+ "', '"+
                        count + "')").executeUpdate();
        tx.commit();
        session.close();
    }

    //get empty series
    protected HashMap<String,Integer> getSeriesHolder(){
        HashMap<String,Integer> series = new HashMap<String,Integer>();
        for (int i=0; i<quarters.length; i++){
            series.put(quarters[i],0);
        }
        return series;
    }


}

/*
 * interface to implement strategy design pattern
 */
interface PredictorAlgorithm{
    public int predict(HashMap<String,Integer> series);
}

/*
 * elastic net regression algorithm class
 */
class ENLPredictorAlgorithm implements PredictorAlgorithm{
    public int predict(HashMap<String,Integer> series){
        int indexToPredict = series.size();
        List keys = new ArrayList(series.keySet());
        Collections.sort(keys);

        List<Attribute> attributes = new ArrayList<>();
        Instances instances = new Instances(attributes);
        int classIndex = 1;
        ElasticNetLearner elasticNetLearner = new ElasticNetLearner();
        double prediction = 0.0;

        int count;
        for ( count=0; count<keys.size(); count++){
            double x = (double) count;
            double y = (double) series.get(keys.get(count));
            String[] data = {Double.toString(x), Double.toString(y)};
            Instance instance = parseDenseInstance(data, classIndex);
            instances.add(instance);
        }

        int numAttributes = instances.get(0).getValues().length;
        for (int i = 0; i < numAttributes; i++) {
            Attribute att = new NumericalAttribute("f" + i);
            att.setIndex(i);
            attributes.add(att);
        }

        if (classIndex >= 0) {
            assignTargetAttribute(instances);
        }

        //end of instance creation
        //build regressor
        GLM glm = elasticNetLearner.buildRegressor(instances, 100, 0.0, 0.0);

        //create new instance for prediction
        int[] indices = {0};
        double[] values = {indexToPredict};
        Instance ins = new Instance(indices, values);

        //predict the value
        prediction = glm.regress(ins);

        int output = (int) Math.round(prediction);
        return (output>0)?output:0;
    }

    protected Instance parseDenseInstance(String[] data, int classIndex) {
        if (classIndex < 0) {
            double[] vector = new double[data.length];
            double classValue = Double.NaN;
            for (int i = 0; i < data.length; i++) {
                vector[i] = Double.parseDouble(data[i]);
            }
            return new Instance(vector, classValue);
        } else {
            double[] vector = new double[data.length - 1];
            double classValue = Double.NaN;
            for (int i = 0; i < data.length; i++) {
                double value = Double.parseDouble(data[i]);
                if (i < classIndex) {
                    vector[i] = value;
                } else if (i > classIndex) {
                    vector[i - 1] = value;
                } else {
                    classValue = value;
                }
            }
            return new Instance(vector, classValue);
        }
    }

    protected void assignTargetAttribute(Instances instances) {
        boolean isInteger = true;
        for (Instance instance : instances) {
            if (!MathUtils.isInteger(instance.getTarget())) {
                isInteger = false;
                break;
            }
        }
        if (isInteger) {
            TreeSet<Integer> set = new TreeSet<>();
            for (Instance instance : instances) {
                double target = instance.getTarget();
                set.add((int) target);
            }
            String[] states = new String[set.size()];
            int i = 0;
            for (Integer v : set) {
                states[i++] = v.toString();
            }
            instances.setTargetAttribute(new NominalAttribute("target", states));
        } else {
            instances.setTargetAttribute(new NumericalAttribute("target"));
        }
    }
}

/*
 * linear regression class
 */
class LRPredictorAlgorithm implements PredictorAlgorithm{
    public int predict(HashMap<String,Integer> series) {
        int indexToPredict = series.size();

        List keys = new ArrayList(series.keySet());
        Collections.sort(keys);

        SimpleRegression simpleRegression = new SimpleRegression();
        simpleRegression.clear();
        int i;
        for (i=0; i<keys.size(); i++){
            simpleRegression.addData(i, series.get(keys.get(i)));
        }
        double intercept = simpleRegression.getIntercept();
        double slope = simpleRegression.getSlope();

        System.out.println(intercept);
        System.out.println(slope);
        double prediction = simpleRegression.predict(indexToPredict);

        int output = (int) Math.round(prediction);
        return (output>0)?output:0;
    }
}