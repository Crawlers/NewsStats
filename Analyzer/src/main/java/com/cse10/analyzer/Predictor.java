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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class Predictor {
    public void  predict(){
        List results = getInput();
        HashMap<String,Integer> series = getSeriesHolder();
        HashMap pre = (HashMap) results.get(0);
        series.put((String) pre.get("crime_yearquarter"),((BigDecimal) pre.get("count")).intValue());
        for (int i=0; i<results.size(); i++){
            HashMap ele = (HashMap) results.get(i);
            if (!ele.get("crime_type").equals(pre.get("crime_type")) || !ele.get("crime_district").equals(pre.get("crime_district"))){
                insertToDB(predictValue(series));
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

    public HashMap predictValue(HashMap series){
        return null;
    }

    public void insertToDB(HashMap ele){

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
}
