package com.cse10.analyzer;

import com.cse10.database.HibernateUtil;
import mltk.core.*;
import mltk.core.io.InstancesReader;
import mltk.predictor.glm.ElasticNetLearner;
import mltk.predictor.glm.GLM;
import mltk.util.MathUtils;
import org.apache.commons.math.stat.regression.SimpleRegression;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
//import java.sql.*;
import java.util.*;

public class Predictor {
    public void  predict(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.createSQLQuery("truncate table predictions").executeUpdate();
        tx.commit();
        session.close();
        List results = getInput();
        HashMap<String,Integer> series = getSeriesHolder();
        HashMap pre = (HashMap) results.get(0);
        series.put((String) pre.get("crime_yearquarter"),((BigDecimal) pre.get("count")).intValue());
        for (int i=0; i<results.size(); i++){
            HashMap ele = (HashMap) results.get(i);
            if (!ele.get("crime_type").equals(pre.get("crime_type")) || !ele.get("crime_district").equals(pre.get("crime_district"))){
                int predicted = predictValue(series);
                insertToDB(ele,"2014 - 1",predicted);
                series = getSeriesHolder();
                pre = ele;
                continue;
            }
            series.put((String) ele.get("crime_yearquarter"),((BigDecimal) ele.get("count")).intValue());
        }
    }

    public List  getInput(){
        Session session = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = session.beginTransaction();
//        session.createSQLQuery("truncate table news_statistics").executeUpdate();
//        session.createSQLQuery(
//                "INSERT INTO news_statistics (crime_type, crime_district, crime_date, crime_year, crime_yearquarter, crime_count) " +
//                        "SELECT crime_type,(SELECT district FROM location_district_mapper ldm WHERE ceg.location = ldm.location) , crime_date, YEAR (crime_date), CONCAT(YEAR (crime_date), ' - ', QUARTER(crime_date)), count(id) " +
//                        "FROM crime_entity_group ceg " +
//                        "WHERE crime_date >= '2012-01-01' AND crime_date <= '2014-12-31' " +
//                        "GROUP BY crime_type, district, crime_date " +
//                        "ORDER BY YEAR (crime_date)").executeUpdate();
//        session.createSQLQuery("UPDATE news_statistics SET crime_type = 'Other' WHERE crime_type IS NULL OR crime_type = ''").executeUpdate();
//        tx.commit();
//        session.close();

        String sql = "SELECT crime_type, crime_district, crime_yearquarter, sum(crime_count) count" +
                " from news_statistics " +
                " where YEAR(crime_date) < 2014" +
                " group by crime_type, crime_district, crime_yearquarter" +
                " order by crime_type, crime_district, crime_yearquarter";
        SQLQuery query = session.createSQLQuery(sql);
        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        List results = query.list();
        session.close();
        return results;
    }

    public int predictValue(HashMap<String,Integer> series){
        return predictUsingLR(series);
    }

    public void insertToDB(HashMap ele, String key, int count){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.createSQLQuery(
                "INSERT INTO predictions (crime_type, crime_district, crime_yearquarter, crime_count) " +
                        " VALUES ('"+
                        ele.get("crime_type")+ "', '"+
                        ele.get("crime_district")+"', '"+
                        key+ "', '"+
                        count + "')").executeUpdate();
        tx.commit();
        session.close();
    }

    public HashMap<String,Integer> getSeriesHolder(){
        HashMap<String,Integer> series = new HashMap<String,Integer>();
        for (int i=1; i<4; i++){
            series.put("2012 - "+i,0);
        }
        for (int i=1; i<4; i++){
            series.put("2013 - "+i,0);
        }
        return series;
    }

    public int predictUsingLR(HashMap<String,Integer> series) {

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
        double prediction = simpleRegression.predict(i);
        return (int) prediction;


    }

    public double predictUsingENL() {

        ElasticNetLearner elasticNetLearner = new ElasticNetLearner();
        double prediction = 0.0;
        try {
            Instances instances = InstancesReader.read("C:\\Users\\hp\\Desktop\\PredictorIm\\dataFile.txt", 1, " ");
            System.out.println("ddd");
            for (Instance i : instances) {
                System.out.print(i.getValue(0) + " ");
                System.out.println(i.getTarget());
            }
            //build regressor
            GLM glm = elasticNetLearner.buildRegressor(instances, 100, 0.0, 0.0);

            //create new instance for prediction
            int[] indices = {0, 1};
            double[] values = {100, 0};
            Instance i = new Instance(indices, values);

            //perform prediction
            prediction = glm.regress(i);

        } catch (IOException e) {
        }
        return prediction;

    }
}
